package com.taoing.specialroutes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 抛出注解异常NoRouteFound的控制器方法应用此通知, 指定响应状态码
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoRouteFound extends RuntimeException {
}
