package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 位置实体类：表示员工工作位置的基本信息
 * 继承基础实体：包含主键和时间戳字段
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("staff_location")
public class LocationEntity extends BaseEntity {
    /**
     * 位置名称
     */
    private String name;

    /**
     * 位置地址
     */
    private String address;
}
