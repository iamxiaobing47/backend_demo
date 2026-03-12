package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 1. 员工用户实体类：表示员工用户的基本信息
 * 2. 继承基础实体：包含主键和时间戳字段
 * 3. 用户标识：使用staffUserId作为员工用户唯一标识符
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("staff_user")
public class StaffUserEntity extends BaseEntity {
    /**
     * 1. 员工用户ID：员工用户的唯一标识符
     */
    private String staffUserId;
    
    /**
     * 2. 位置ID：员工所属位置的标识符
     */
    private String locationId;
    
    /**
     * 3. 邮箱地址：员工用户的登录邮箱
     */
    private String email;
    
    /**
     * 4. 员工姓名：员工用户的显示名称
     */
    private String name;
}
