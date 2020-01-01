package com.taoing.organizationservice.mapper;

import com.taoing.organizationservice.model.Organization;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface OrganizationMapper extends Mapper<Organization> {
}