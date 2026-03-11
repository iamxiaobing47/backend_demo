package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("staff_location")
public class LocationEntity extends BaseEntity {
    private Long locationId;
    private String name;
    private String address;
}
