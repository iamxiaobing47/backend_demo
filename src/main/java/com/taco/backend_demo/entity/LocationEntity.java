package com.taco.backend_demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationEntity extends BaseEntity {
    private Long locationId;
    private String name;
    private String address;
}
