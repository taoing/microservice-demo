package com.taoing.licensingserver.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Organization {
    private Integer id;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
}
