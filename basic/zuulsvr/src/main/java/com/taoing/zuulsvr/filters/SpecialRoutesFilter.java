package com.taoing.zuulsvr.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.taoing.zuulsvr.model.AbTestingRoute;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * zuul的路由过滤器, 可对zuul的路由过程进行操作
 */
@Slf4j
@Component
public class SpecialRoutesFilter extends ZuulFilter {
    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;

    @Autowired
    private FilterUtils filterUtils;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String filterType() {
        return FilterUtils.ROUTE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    private ProxyRequestHelper helper = new ProxyRequestHelper(new ZuulProperties());

    /**
     * 查询路由配置记录
     * @param serviceName
     * @return
     */
    private AbTestingRoute getAbRoutingInfo(String serviceName) {
        ResponseEntity<AbTestingRoute> restExchange;
        try {
            restExchange = restTemplate.exchange(
                    "http://specialroutesservice/v1/route/abtesting/{serviceName}",
                    HttpMethod.GET, null, AbTestingRoute.class, serviceName);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }
            throw ex;
        }
        return restExchange.getBody();
    }

    /**
     * 构建新的路由uri: http://ip:port/...
     * @param oldEndpoint
     * @param newEndpoint
     * @param serviceName
     * @return
     */
    private String buildRouteString(String oldEndpoint, String newEndpoint, String serviceName) {
        int index = oldEndpoint.indexOf(serviceName);

        String strippedRoute = oldEndpoint.substring(index + serviceName.length());
        log.info("Target route: {}/{}", newEndpoint, strippedRoute);
        return String.format("%s/%s", newEndpoint, strippedRoute);
    }

    /**
     * 获取请求的标准动词
     * @param request
     * @return
     */
    private String getVerb(HttpServletRequest request) {
        String sMethod = request.getMethod();
        return sMethod.toUpperCase();
    }

    /**
     * 构建http协议主机头
     * @param host
     * @return
     */
    private HttpHost getHttpHost(URL host) {
        HttpHost httpHost = new HttpHost(host.getHost(), host.getPort(), host.getProtocol());
        return httpHost;
    }

    /**
     * 获取请求url中的查询参数为Header[]
     * @param headers
     * @return
     */
    private Header[] convertHeaders(MultiValueMap<String, String> headers) {
        List<Header> list = new ArrayList<>();
        for (String name : headers.keySet()) {
            for (String value : headers.get(name)) {
                list.add(new BasicHeader(name, value));
            }
        }
        return list.toArray(new BasicHeader[0]);
    }

    /**
     * 使用httpClient转发请求
     * @param httpClient
     * @param httpHost
     * @param httpRequest
     * @return
     * @throws IOException
     */
    private HttpResponse forwardRequest(HttpClient httpClient, HttpHost httpHost,
                                        HttpRequest httpRequest) throws IOException {
        return httpClient.execute(httpHost, httpRequest);
    }

    /**
     * 反转Header[]为MultiValueMap
     * @param headers
     * @return
     */
    private MultiValueMap<String, String> revertHeaders(Header[] headers) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (Header header : headers) {
            String name = header.getName();
            if (!map.containsKey(name)) {
                map.put(name, new ArrayList<>());
            }
            map.get(name).add(header.getValue());
        }
        return map;
    }

    /**
     * 获取代表请求body的输入流
     * @param request
     * @return
     */
    private InputStream getRequestBody(HttpServletRequest request) {
        InputStream requestEntity = null;
        try {
            requestEntity = request.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return requestEntity;
    }

    /**
     * 将响应写入辅助对象
     * @param response
     * @throws IOException
     */
    private void setResponse(HttpResponse response) throws IOException {
        RequestContext.getCurrentContext().set("zuulResponse", response);
        this.helper.setResponse(response.getStatusLine().getStatusCode(),
                response.getEntity() == null ? null : response.getEntity().getContent(),
                revertHeaders(response.getAllHeaders()));
    }

    /**
     * 转发请求
     * @param httpClient
     * @param verb
     * @param uri
     * @param request
     * @param headers
     * @param params
     * @param requestEntity
     * @return
     * @throws Exception
     */
    private HttpResponse forward(HttpClient httpClient, String verb, String uri,
                                HttpServletRequest request, MultiValueMap<String, String> headers,
                                MultiValueMap<String, String> params, InputStream requestEntity) throws Exception {
        Map<String, Object> info = this.helper.debug(verb, uri, headers, params, requestEntity);
        URL host = new URL(uri);
        HttpHost httpHost = getHttpHost(host);

        HttpRequest httpRequest;
        int contentLength = request.getContentLength();
        InputStreamEntity entity = new InputStreamEntity(requestEntity, contentLength,
                request.getContentType() != null
                        ? ContentType.create(request.getContentType()) : null);
        switch (verb.toUpperCase()) {
            case "POST":
                HttpPost httpPost = new HttpPost(uri);
                httpRequest = httpPost;
                httpPost.setEntity(entity);
                break;
            case "PUT":
                HttpPut httpPut = new HttpPut(uri);
                httpRequest = httpPut;
                httpPut.setEntity(entity);
                break;
            case "PATCH":
                HttpPatch httpPatch = new HttpPatch(uri);
                httpRequest = httpPatch;
                httpPatch.setEntity(entity);
                break;
            default:
                httpRequest = new BasicHttpRequest(verb, uri);
        }
        httpRequest.setHeaders(convertHeaders(headers));
        HttpResponse zuulResponse = forwardRequest(httpClient, httpHost, httpRequest);
        this.helper.appendDebug(info, zuulResponse.getStatusLine().getStatusCode(),
                revertHeaders(zuulResponse.getAllHeaders()));

        return zuulResponse;
    }

    /**
     * 使用随机数比较权重, 判断是否使用指定路由
     * @param testRoute
     * @return
     */
    public boolean useSpecialRoute(AbTestingRoute testRoute) {
        Random random = new Random();

        if (testRoute.getActive().equals("N")) {
            return false;
        }
        int value = random.nextInt(10) + 1;
        if (testRoute.getWeight() < value) {
            return true;
        }
        return false;
    }

    /**
     * 转发请求到指定路由
     * @param route
     */
    private void forwardToSpecialRoute(String route) {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        MultiValueMap<String, String> headers = this.helper.buildZuulRequestHeaders(request);
        MultiValueMap<String, String> params = this.helper.buildZuulRequestQueryParams(request);
        String verb = getVerb(request);
        InputStream requestEntity = getRequestBody(request);
        if (request.getContentLength() < 0) {
            context.setChunkedRequestBody();
        }

        this.helper.addIgnoredHeaders();
        HttpResponse response = null;

        /**
         * httpClient用完后立即关闭, 将response写到zuul的返回响应时会抛出异常,不能完成响应
         * 参照zuul的SimpleHostRoutingFilter, 不在此处关闭httpClient
         */
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            response = forward(httpClient, verb, route, request, headers, params, requestEntity);
            setResponse(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();

        AbTestingRoute abTestRoute = getAbRoutingInfo(filterUtils.getServiceId());
        if (abTestRoute != null && useSpecialRoute(abTestRoute)) {
            String route = buildRouteString(ctx.getRequest().getRequestURI(),
                    abTestRoute.getEndpoint(),
                    ctx.get("serviceId").toString());
            // serviceId置为null, 防止zuul后续RibbonRoutingFilter的路由
            ctx.set("serviceId", null);
            forwardToSpecialRoute(route);

            // ctx的serviceId决定后续往哪里路由
            // 更新RequestContext中的serviceId, zuul的RibbonRoutingFilter过滤器自动路由请求
            // ctx.set("serviceId", abTestRoute.getEndpoint());
        }
        return null;
    }
}
