package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("staff_user")
public class StaffUserEntity extends BaseEntity {
    private Long staffUserId;
    private Integer locationId;
    private String email;
    private String name;
}
