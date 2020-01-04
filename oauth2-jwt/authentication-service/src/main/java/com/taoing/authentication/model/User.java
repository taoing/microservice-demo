package com.taoing.authentication.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * user
 * @author 
 */
@Entity
@Table(name = "user")
@Data
public class User implements Serializable {
    @Id
    private Integer id;

    private String userName;

    private String password;

    private Boolean enabled;

    private static final long serialVersionUID = 1L;
}