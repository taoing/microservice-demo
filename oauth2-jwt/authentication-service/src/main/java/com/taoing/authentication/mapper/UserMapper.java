package com.taoing.authentication.mapper;

import com.taoing.authentication.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface UserMapper extends Mapper<User> {

    /**
     * 查询用户角色名称列表
     * @param userId
     * @return
     */
    List<String> getUserRoles(@Param("userId") Integer userId);

    /**
     * 查询用户orgId
     * @param username
     * @return
     */
    Integer getUserOrgId(@Param("username") String username);
}