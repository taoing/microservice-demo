package com.taoing.authentication.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * user_org
 * @author 
 */
@Entity
@Table(name = "user_org")
@Data
public class UserOrg implements Serializable {
    @Id
    private Integer userId;

    @Id
    private Integer orgId;
}