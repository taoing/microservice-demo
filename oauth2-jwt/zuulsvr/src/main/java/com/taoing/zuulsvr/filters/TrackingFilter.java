package com.taoing.zuulsvr.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.taoing.zuulsvr.config.ServiceConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @description: zuul路由前置过滤器, 路由前过滤请求
 * @author: mian.tao
 * @date: 2019-12-31 14:21
 */
@Slf4j
@Component
public class TrackingFilter extends ZuulFilter {
    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;

    @Autowired
    private FilterUtils filterUtils;

    @Autowired
    private ServiceConfig serviceConfig;

    @Override
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    private boolean isCorrelationIdPresent() {
        if (filterUtils.getCorrelationId() != null) {
            return true;
        }
        return false;
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    private Integer getOrgId() {
        Integer result = null;
        if (filterUtils.getAuthToken() != null) {
            String authToken = filterUtils.getAuthToken().replace("Bearer ", "");
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(serviceConfig.getJwtSigningKey().getBytes(StandardCharsets.UTF_8))
                        .parseClaimsJws(authToken).getBody();
                result = (Integer) claims.get("orgId");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }

    @Override
    public Object run() {
        if (this.isCorrelationIdPresent()) {
            log.info("tmx-correlation-id found in tracking filter: {}.", filterUtils.getCorrelationId());
        } else {
            filterUtils.setCorrelationId(this.generateCorrelationId());
            log.info("tmx-correlation-id generated in tracking filter {}.", filterUtils.getCorrelationId());
        }
        Integer orgId = this.getOrgId();
        log.info("The organization id from the token is: {}", orgId);
        filterUtils.setOrgId("" + (orgId == null ? "" : orgId));

        RequestContext ctx = RequestContext.getCurrentContext();
        log.info("Processing incoming request for {}.", ctx.getRequest().getRequestURI());
        return null;
    }
}
