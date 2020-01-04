package com.taoing.authentication.security;

import com.taoing.authentication.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义jwt token加强策略
 */
public class JWTTokenEnhancer implements TokenEnhancer {

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        // authentication.getName()返回username
        Integer orgId = this.getOrgId(authentication.getName());

        additionalInfo.put("orgId", orgId);

        DefaultOAuth2AccessToken defaultOAuth2AccessToken = (DefaultOAuth2AccessToken) accessToken;
        // 向accessToken添加额外信息
        defaultOAuth2AccessToken.setAdditionalInformation(additionalInfo);
        return defaultOAuth2AccessToken;
    }

    private Integer getOrgId(String username) {
        return this.userMapper.getUserOrgId(username);
    }
}
