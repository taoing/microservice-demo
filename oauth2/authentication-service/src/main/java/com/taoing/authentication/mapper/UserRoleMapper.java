package com.taoing.authentication.mapper;

import com.taoing.authentication.model.UserRole;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserRoleMapper extends Mapper<UserRole> {
}