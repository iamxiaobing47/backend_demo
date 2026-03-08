package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("corporations")
public class CorporationEntity extends BaseEntity {
    private Long corporationId;
    private String name;
    private String registrationNumber;
    private String address;
}
