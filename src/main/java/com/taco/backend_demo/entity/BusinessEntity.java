package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 1. 业务实体类：表示业务组织的基本信息
 * 2. 继承基础实体：包含主键和时间戳字段
 * 3. 业务标识：使用businessId作为业务唯一标识符
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("business")
public class BusinessEntity extends BaseEntity {
    /**
     * 1. 业务ID：业务组织的唯一标识符
     */
    private String businessId;
    
    /**
     * 2. 业务名称：业务组织的显示名称
     */
    private String name;
    
    /**
     * 3. 业务地址：业务组织的物理地址
     */
    private String address;
}
