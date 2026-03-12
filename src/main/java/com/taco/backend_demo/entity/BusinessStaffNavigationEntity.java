package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 1. 业务-员工-导航关联实体：管理用户与导航菜单的权限关联
 * 2. 继承基础实体：包含主键和时间戳字段
 * 3. 权限控制：通过关联表实现动态的菜单权限分配
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("business_staff_navigation")
public class BusinessStaffNavigationEntity extends BaseEntity {
    /**
     * 1. 业务ID：关联的业务组织标识符（适用于业务用户）
     */
    private String businessId;
    
    /**
     * 2. 位置ID：关联的位置标识符（适用于员工用户）
     */
    private String locationId;
    
    /**
     * 3. 导航主键：关联的导航菜单主键
     */
    private Long navigationPk;
}
