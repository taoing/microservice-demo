package com.taoing.organizationservice.events.source;

import com.taoing.organizationservice.events.models.OrgChangeModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleSourceBean {

    private Source source;

    /**
     * 注入Soruce接口实现, 供服务使用
     * @param source
     */
    @Autowired
    public SimpleSourceBean(Source source) {
        this.source = source;
    }

    public void publishOrgChange(String action, Integer orgId) {
        log.info("Sending kafka message: {} for orgId: {}", action, orgId);
        OrgChangeModel change = new OrgChangeModel(
                OrgChangeModel.class.getTypeName(),
                action,
                orgId,
                "none"
        );
        // 使用Source类中定义的输出通道发送消息
        source.output().send(MessageBuilder.withPayload(change).build());
    }
}
