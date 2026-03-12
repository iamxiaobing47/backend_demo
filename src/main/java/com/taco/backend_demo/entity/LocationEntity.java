package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 1. 位置实体类：表示员工工作位置的基本信息
 * 2. 继承基础实体：包含主键和时间戳字段
 * 3. 位置管理：支持位置信息的存储和维护
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("staff_location")
public class LocationEntity extends BaseEntity {
    /**
     * 1. 位置ID：位置的唯一标识符
     */
    private String locationId;
    
    /**
     * 2. 位置名称：位置的显示名称
     */
    private String name;
    
    /**
     * 3. 位置地址：位置的物理地址
     */
    private String address;
}
