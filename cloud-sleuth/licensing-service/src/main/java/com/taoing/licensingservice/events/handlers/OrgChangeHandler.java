package com.taoing.licensingservice.events.handlers;

import com.taoing.licensingservice.events.CustomChannels;
import com.taoing.licensingservice.events.models.OrgChangeModel;
import com.taoing.licensingservice.repository.OrgRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * @description: 消息消费处理器
 * @author: mian.tao
 * @date: 2020-01-10 9:43
 */
@Slf4j
@EnableBinding(CustomChannels.class)
public class OrgChangeHandler {

    @Autowired
    private OrgRedisRepository orgRedisRepository;

    /**
     * 注解方法监听输入通道传入的消息, 消费消息
     * @param orgChange
     */
    @StreamListener(CustomChannels.INPUT)
    public void loggerSink(OrgChangeModel orgChange) {
        log.info("Received a message of type: {}", orgChange.getType());
        switch (orgChange.getAction()) {
            case "GET":
                log.info("Received a GET event from the organization service for org id {}", orgChange.getOrgId());
                break;
            case "SAVE":
                log.info("Received a SAVE event from the organization service for org id {}", orgChange.getOrgId());
                break;
            case "UPDATE":
                log.info("Received a UPDATE event from the organization service for org id {}", orgChange.getOrgId());
                this.orgRedisRepository.deleteOrg(orgChange.getOrgId());
                break;
            case "DELETE":
                log.info("Received a DELETE event from the organization service for org id {}", orgChange.getOrgId());
                this.orgRedisRepository.deleteOrg(orgChange.getOrgId());
                break;
            default:
                log.info("Received a UNKNOWN event from the organization service of type {}", orgChange.getType());
        }
    }
}
