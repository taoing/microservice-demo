package com.taoing.specialroutes.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "abtesting")
@Getter
@Setter
public class AbTestingRoute {
    @Id
    private Integer id;

    private String serviceName;

    private String active;

    private String endpoint;

    private Integer weight;
}
