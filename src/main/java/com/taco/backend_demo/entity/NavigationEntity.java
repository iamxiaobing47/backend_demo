package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 导航实体类：表示系统导航菜单的基本信息
 * 继承基础实体：包含主键和时间戳字段
 * 树形结构：支持多级菜单的父子关系和排序
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("navigation")
public class NavigationEntity extends BaseEntity {
    /**
     * 导航菜单名称
     */
    private String name;

    /**
     * 路径：导航菜单对应的路由路径
     */
    private String path;

    /**
     * 图标：导航菜单的图标标识
     */
    private String icon;

    /**
     * 排序序号：控制菜单在同级中的显示顺序
     */
    private Integer sortOrder;

    /**
     * 父级 ID: 指向父级菜单的主键，0 或 null 表示根节点
     */
    private Long parentId;
}
