package com.taoing.authentication.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * role
 * @author 
 */
@Entity
@Table(name = "role")
@Data
public class Role implements Serializable {
    @Id
    private Integer id;

    private String name;

    private static final long serialVersionUID = 1L;
}