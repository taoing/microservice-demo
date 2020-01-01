package com.taoing.authentication.security;

import com.taoing.authentication.userdetails.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

/**
 * 配置OAuth2认证服务
 */
@Configuration
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 注册可访问OAuth2认证服务的客户端应用程序,
     * 此处使用基于内存的应用程序信息服务
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                // 应用程序名称(id)
                .withClient("app")
                // 应用密钥, 使用使用配置的passwordEncoder加密密钥
                .secret(passwordEncoder.encode("thisissecret"))
                // 支持的授权类型
                .authorizedGrantTypes("refresh_token", "password", "client", "client_credentials")
                // 有效作用域
                .scopes("webclient", "mobileclient");
    }

    /**
     * 配置OAuth2认证端点使用的用户详细信息服务和认证管理器
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }
}
