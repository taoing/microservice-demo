package com.taoing.authentication.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * user_role
 * @author 
 */
@Entity
@Table(name = "user_role")
@Data
public class UserRole implements Serializable {
    @Id
    private Integer userId;

    @Id
    private Integer roleId;
}