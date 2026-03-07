package com.taco.backend_demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessUserEntity extends BaseEntity {
    private Long businessUserId;
    private Long businessId;
    private String email;
    private String name;
    private String position;
}
