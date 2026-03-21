package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 业务用户实体类：表示业务用户的基本信息
 * 继承基础实体：包含主键和时间戳字段
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("business_user")
public class BusinessUserEntity extends BaseEntity {
    /**
     * 业务组织主键（外键）
     */
    private Integer businessPk;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 姓名
     */
    private String name;
}
