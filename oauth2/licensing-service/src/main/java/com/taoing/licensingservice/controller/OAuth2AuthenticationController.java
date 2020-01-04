package com.taoing.licensingservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 测试如何使用资源服务器从OAuth2认证服务获取的用户授权信息
 */
@RequestMapping(value = "/test/oauth2/authentication")
@RestController
public class OAuth2AuthenticationController {

    /**
     * 可以获取到OAuth2Authentication对象, 信息很丰富
     * @param oauth2Authentication
     * @return
     */
    @RequestMapping(value = "/queryOAuth2", method = RequestMethod.GET)
    public Object queryOAuth2(OAuth2Authentication oauth2Authentication) {
        if (oauth2Authentication != null) {
            return oauth2Authentication;
        }
        return new Object();
    }

    /**
     * 此处的Principal就是OAuth2Authentication对象, 同queryOAuth2方法
     * @param principal
     * @return
     */
    @RequestMapping(value = "/queryPrincipal", method = RequestMethod.GET)
    public Object queryPrincipal(Principal principal) {
        if (principal != null) {
            return principal;
        }
        return new Object();
    }

    /**
     * 从security的SecurityContext获取
     * @return
     */
    @RequestMapping(value = "/securityContext", method = RequestMethod.GET)
    public Object securityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        // 此处authentication就是OAuth2Authentication对象
        if (authentication instanceof OAuth2Authentication) {
            return authentication.getPrincipal();
        }
        return new Object();
    }
}
