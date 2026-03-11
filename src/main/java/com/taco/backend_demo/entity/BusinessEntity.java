package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("business")
public class BusinessEntity extends BaseEntity {
    private Integer businessId;
    private String name;
    private String address;
}
