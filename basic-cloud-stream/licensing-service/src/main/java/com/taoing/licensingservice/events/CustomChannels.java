package com.taoing.licensingservice.events;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @description: 自定义消息输入通道接口
 * @author: mian.tao
 * @date: 2020-01-10 9:56
 */
public interface CustomChannels {

    /**
     * 使用自定义通道名称
     */
    String INPUT = "inboundOrgChanges";

    @Input(CustomChannels.INPUT)
    SubscribableChannel orgs();
}
