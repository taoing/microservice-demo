package com.taoing.authentication.userdetails;

import com.taoing.authentication.mapper.UserMapper;
import com.taoing.authentication.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 实现UserDetailsService接口, 检索用户详细信息
 */
@Slf4j
@Component
public class MyUserDetailsService implements UserDetailsService {

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User();
        user.setUserName(username);
        user = this.userMapper.selectOne(user);
        if (user == null) {
            log.warn("User: {} not found", username);
            return null;
        }
        List<String> roles = this.userMapper.getUserRoles(user.getId());
        MyUserDetails userDetails = new MyUserDetails();
        userDetails.setUsername(user.getUserName());
        userDetails.setPassword(user.getPassword());
        userDetails.setEnabled(user.getEnabled());
        userDetails.setAccountNonExpired(true);
        userDetails.setAccountNonLocked(true);
        userDetails.setCredentialsNonExpired(true);
        Set<SimpleGrantedAuthority> authorites = new HashSet<>();
        if (!roles.isEmpty()) {
            for (String role : roles) {
                authorites.add(new SimpleGrantedAuthority(role));
            }
        }
        userDetails.setAuthorities(authorites);
        log.info("Looking up user: {} done", username);

        return userDetails;
    }
}
