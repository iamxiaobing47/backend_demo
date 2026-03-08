package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("business_users")
public class BusinessUserEntity extends BaseEntity {
    private Long businessUserId;
    private Long businessId;
    private String email;
    private String name;
    private String position;
}
