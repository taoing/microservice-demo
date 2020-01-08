package com.taoing.organizationservice.events.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgChangeModel {
    private String type;
    private String action;
    private Integer orgId;
    private String correlationId;

    public OrgChangeModel() {

    }

    public OrgChangeModel(String type, String action, Integer orgId, String correlationId) {
        this.type = type;
        this.action = action;
        this.orgId = orgId;
        this.correlationId = correlationId;
    }
}
