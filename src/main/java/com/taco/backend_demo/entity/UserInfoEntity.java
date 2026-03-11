package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("view_user_info")
public class UserInfoEntity {
    private String userId;
    private String userType;
    private String email;
    private String userName;
    private Long orgId;
    private String orgName;
    private String orgType;


    public UserInfoEntity(UserInfoEntity userInfo) {
        this.userId = userInfo.getUserId();
        this.userType = userInfo.getUserType();
        this.email = userInfo.getEmail();
        this.userName = userInfo.getUserName();
        this.orgId = userInfo.getOrgId();
        this.orgName = userInfo.getOrgName();
        this.orgType = userInfo.getOrgType();
    }
}
