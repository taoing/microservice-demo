package com.taoing.licensingservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;

import java.io.IOException;

/**
 * @description: RestTemplate出站调用请求拦截器, 从security的上下文中拿到token添加到请求
 * @author: mian.tao
 * @date: 2020-01-03 10:00
 */
@Slf4j
public class OAuth2TokenInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.info("OAuth2TokenInterceptor authentication is null");
            return execution.execute(request, body);
        }
        if (!(authentication.getCredentials() instanceof AbstractOAuth2Token)) {
            log.info("OAuth2TokenInterceptor authentication no oauth2Token");
            return execution.execute(request, body);
        }
        AbstractOAuth2Token oauth2Token = (AbstractOAuth2Token) authentication.getCredentials();
        log.info("OAuth2TokenInterceptor authentication has oauth2Token: {}", oauth2Token.getTokenValue());
        request.getHeaders().setBearerAuth(oauth2Token.getTokenValue());
        return execution.execute(request, body);
    }
}
