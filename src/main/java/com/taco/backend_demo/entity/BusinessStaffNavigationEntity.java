package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 业务 - 员工 - 导航关联实体：管理用户与导航菜单的权限关联
 * 继承基础实体：包含主键和时间戳字段
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("business_staff_navigation")
public class BusinessStaffNavigationEntity extends BaseEntity {
    /**
     * 业务组织主键（外键，关联 business 表）
     */
    private Integer businessPk;

    /**
     * 位置主键（外键，关联 staff_location 表）
     */
    private Long locationPk;

    /**
     * 导航主键（外键，关联 navigation 表）
     */
    private Long navigationPk;
}
