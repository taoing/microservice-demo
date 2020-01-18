package com.taoing.zuulsvr.filters;

import brave.Tracer;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: zuul路由后置过滤器, 调用目标服务返回后过滤
 * @author: mian.tao
 * @date: 2019-12-31 14:40
 */
@Slf4j
@Component
public class ResponseFilter extends ZuulFilter {
    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;

    /**
     * 使用Tracer以编程方式向zipkin添加sleuth跟踪信息
     */
    @Autowired
    private Tracer tracer;

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    @Override
    public Object run() {
        String traceId = tracer.currentSpan().context().traceIdString();

        RequestContext ctx = RequestContext.getCurrentContext();
        log.info("Adding the correlation id to the outbound headers. {}", traceId);
        ctx.getResponse().addHeader("tmx-correlation-id", traceId);
        log.info("Completing outgoing request for {}.", ctx.getRequest().getRequestURI());

        return null;
    }
}
