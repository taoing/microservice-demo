package com.taoing.licensingservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "license")
@Getter
@Setter
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer organizationId;

    private String licenseType;

    private String productName;

    private Integer licenseMax;

    private Integer licenseAllocated;

    private String remark;

    @Transient
    private String organizationName;

    @Transient
    private String contactName;

    @Transient
    private String contactPhone;

    @Transient
    private String contactEmail;
}
