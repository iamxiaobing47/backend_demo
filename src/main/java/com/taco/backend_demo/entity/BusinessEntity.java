package com.taco.backend_demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessEntity extends BaseEntity {
    private Long businessId;
    private Long corporationId;
    private String name;
    private String businessNumber;
}
