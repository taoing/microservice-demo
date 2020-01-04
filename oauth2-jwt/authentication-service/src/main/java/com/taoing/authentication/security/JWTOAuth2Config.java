package com.taoing.authentication.security;

import com.taoing.authentication.userdetails.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

/**
 * 配置使用jwt的OAuth2认证服务
 */
@Configuration
public class JWTOAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private DefaultTokenServices tokenServices;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Bean
    public TokenEnhancer jwtTokenEnhancer() {
        return new JWTTokenEnhancer();
    }

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
                .scopes("webclient", "mobileclient")
                // token有效期
                .accessTokenValiditySeconds(43200)
                // refreshToken有效期
                .refreshTokenValiditySeconds(864000)
                .and()
                .withClient("webapp")
                .secret(passwordEncoder.encode("thisissecret"))
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("webclient", "mobileclient")
                // token有效期
                .accessTokenValiditySeconds(60)
                // refreshToken有效期
                .refreshTokenValiditySeconds(864000);
    }

    /**
     * 配置OAuth2认证端点使用的用户详细信息服务和认证管理器
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer(), jwtAccessTokenConverter));

        endpoints
                // jwt
                .tokenStore(tokenStore)
                // jwt
                .accessTokenConverter(jwtAccessTokenConverter)
                // jwt
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }
}
