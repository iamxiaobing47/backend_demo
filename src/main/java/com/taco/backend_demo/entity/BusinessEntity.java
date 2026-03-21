package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 业务实体类：表示业务组织的基本信息
 * 继承基础实体：包含主键和时间戳字段
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("business")
public class BusinessEntity extends BaseEntity {
    /**
     * 业务名称
     */
    private String name;

    /**
     * 业务地址
     */
    private String address;
}
