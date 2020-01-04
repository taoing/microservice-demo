package com.taoing.authentication.mapper;

import com.taoing.authentication.model.UserOrg;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserOrgMapper extends Mapper<UserOrg> {
}