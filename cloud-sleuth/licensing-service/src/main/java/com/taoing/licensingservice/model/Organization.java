package com.taoing.licensingservice.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
}
