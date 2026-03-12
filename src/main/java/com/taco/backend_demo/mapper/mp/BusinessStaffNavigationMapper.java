package com.taco.backend_demo.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.BusinessStaffNavigationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 1. 业务-员工-导航关联Mapper：管理用户与导航菜单的关联关系
 * 2. MyBatis Plus集成：继承BaseMapper提供基础CRUD操作
 * 3. 权限控制：通过关联表实现用户菜单权限的动态分配
 */
@Mapper
public interface BusinessStaffNavigationMapper extends BaseMapper<BusinessStaffNavigationEntity> {
}
