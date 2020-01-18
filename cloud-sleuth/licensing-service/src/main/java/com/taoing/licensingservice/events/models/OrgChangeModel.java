package com.taoing.licensingservice.events.models;

import lombok.Data;

@Data
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
