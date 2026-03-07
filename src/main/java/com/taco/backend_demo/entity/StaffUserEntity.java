package com.taco.backend_demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StaffUserEntity extends BaseEntity {
    private Long staffUserId;
    private Long locationId;
    private String email;
    private String name;
    private String position;
}
