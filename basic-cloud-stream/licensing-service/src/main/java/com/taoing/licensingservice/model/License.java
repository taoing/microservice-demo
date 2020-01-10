package com.taoing.licensingservice.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "license")
@Data
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
