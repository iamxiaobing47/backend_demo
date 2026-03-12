package com.taco.backend_demo.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.NavigationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 1. 导航实体Mapper：提供导航菜单数据的数据库操作
 * 2. MyBatis Plus集成：继承BaseMapper提供基础CRUD操作
 * 3. 菜单管理：支持导航菜单的存储、查询和维护操作
 */
@Mapper
public interface NavigationMapper extends BaseMapper<NavigationEntity> {
}
