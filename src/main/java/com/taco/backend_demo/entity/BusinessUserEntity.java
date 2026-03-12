package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 1. 业务用户实体类：表示业务用户的基本信息
 * 2. 继承基础实体：包含主键和时间戳字段
 * 3. 用户标识：使用businessUserId作为业务用户唯一标识符
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("business_user")
public class BusinessUserEntity extends BaseEntity {
    /**
     * 1. 业务用户ID：业务用户的唯一标识符
     */
    private String businessUserId;
    
    /**
     * 2. 业务ID：业务用户所属业务组织的标识符
     */
    private String businessId;
    
    /**
     * 3. 邮箱地址：业务用户的登录邮箱
     */
    private String email;
    
    /**
     * 4. 用户姓名：业务用户的显示名称
     */
    private String name;
}
