package com.taoing.organizationservice.service.impl;

import brave.ScopedSpan;
import brave.Tracer;
import com.taoing.organizationservice.events.source.SimpleSourceBean;
import com.taoing.organizationservice.mapper.OrganizationMapper;
import com.taoing.organizationservice.model.Organization;
import com.taoing.organizationservice.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    private OrganizationMapper mapper;

    private SimpleSourceBean simpleSourceBean;

    /**
     * 使用Tracer以编程方式向zipkin添加sleuth跟踪信息
     */
    private Tracer tracer;

    @Autowired
    public void setMapper(OrganizationMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setSimpleSourceBean(SimpleSourceBean simpleSourceBean) {
        this.simpleSourceBean = simpleSourceBean;
    }

    @Autowired
    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public Organization getOrg(Integer orgId) {
        ScopedSpan span = this.tracer.startScopedSpan("getOrgMysqlCall");
        span.annotate("mysql.query.start");

        log.info("In the OrganizationService.getOrg() call");
        Organization org;
        try {
            org = this.mapper.selectByPrimaryKey(orgId);
        } catch (RuntimeException e) {
            span.error(e);
            org = null;
        } finally {
            span.annotate("mysql.query.end");

            span.tag("sql.args", String.valueOf(orgId));
            span.tag("peer.service", "mysql");
            span.finish();
        }

        this.simpleSourceBean.publishOrgChange("GET", org == null ? null : org.getId());
        return org;
    }

    @Override
    public Organization saveOrg(Organization org) {
        this.mapper.insert(org);

        this.simpleSourceBean.publishOrgChange("SAVE", org.getId());
        return org;
    }

    @Override
    public void updateOrg(Organization org) {
        this.mapper.updateByPrimaryKey(org);

        this.simpleSourceBean.publishOrgChange("UPDATE", org.getId());
    }

    @Override
    public void deleteOrg(Integer orgId) {
        this.mapper.deleteByPrimaryKey(orgId);

        this.simpleSourceBean.publishOrgChange("DELETE", orgId);
    }
}
