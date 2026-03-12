package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 1. 导航实体类：表示系统导航菜单的基本信息
 * 2. 继承基础实体：包含主键和时间戳字段
 * 3. 树形结构：支持多级菜单的父子关系和排序
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("navigation")
public class NavigationEntity extends BaseEntity {
    /**
     * 1. 中文名称：导航菜单的中文显示名称
     */
    private String chineseName;
    
    /**
     * 2. 英文名称：导航菜单的英文显示名称
     */
    private String englishName;
    
    /**
     * 3. 路径：导航菜单对应的路由路径
     */
    private String path;
    
    /**
     * 4. 图标：导航菜单的图标标识
     */
    private String icon;
    
    /**
     * 5. 排序序号：控制菜单在同级中的显示顺序
     */
    private Integer sortOrder;
    
    /**
     * 6. 父级ID：指向父级菜单的主键，0或null表示根节点
     */
    private Long parentId;
}
