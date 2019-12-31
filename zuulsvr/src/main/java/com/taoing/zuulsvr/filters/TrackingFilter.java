package com.taoing.zuulsvr.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @description: zuul路由前置过滤器, 路由前过滤请求
 * @author: mian.tao
 * @date: 2019-12-31 14:21
 */
@Slf4j
@Component
public class TrackingFilter extends ZuulFilter {
    private static final int FILTER_ORDER  = 1;
    private static final boolean SHOULD_FILTER = true;

    @Autowired
    private FilterUtils filterUtils;

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

    @Override
    public Object run() {
        if (this.isCorrelationIdPresent()) {
            log.info("tmx-correlation-id found in tracking filter: {}.", filterUtils.getCorrelationId());
        } else {
            filterUtils.setCorrelationId(this.generateCorrelationId());
            log.info("tmx-correlation-id generated in tracking filter {}.", filterUtils.getCorrelationId());
        }
        RequestContext ctx = RequestContext.getCurrentContext();
        log.info("Processing incoming request for {}.", ctx.getRequest().getRequestURI());
        return null;
    }
}
