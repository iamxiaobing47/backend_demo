package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("view_user_info")
public class UserInfoEntity  extends BaseEntity {
    private Long userId;
    private String userType;
    private String email;
    private String userName;
    private Long orgId;
    private String orgName;
    private String orgType;
}
