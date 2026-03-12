package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 1. 用户信息视图实体：聚合业务用户和员工用户的完整信息
 * 2. 视图映射：对应数据库中的view_user_info视图
 * 3. 统一用户模型：提供统一的用户信息访问接口
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("view_user_info")
public class UserInfoEntity {
    /**
     * 1. 用户ID：用户的唯一标识符
     */
    private String userId;
    
    /**
     * 2. 用户类型：区分业务用户(BUSINESS_USER)和员工用户(STAFF_USER)
     */
    private String userType;
    
    /**
     * 3. 邮箱地址：用户的登录邮箱
     */
    private String email;
    
    /**
     * 4. 用户姓名：用户的显示名称
     */
    private String userName;
    
    /**
     * 5. 组织ID：用户所属组织的标识符
     */
    private String orgId;
    
    /**
     * 6. 组织名称：用户所属组织的显示名称
     */
    private String orgName;
    
    /**
     * 7. 组织类型：用户所属组织的类型
     */
    private String orgType;

    /**
     * 1. 拷贝构造函数：创建UserInfoEntity的深拷贝实例
     * @param userInfo 源用户信息对象
     */
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
