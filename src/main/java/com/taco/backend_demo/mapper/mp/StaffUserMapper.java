package com.taco.backend_demo.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.StaffUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 1. 员工用户Mapper：提供员工用户数据的数据库操作
 * 2. MyBatis Plus集成：继承BaseMapper提供基础CRUD操作
 * 3. 用户管理：支持员工用户的存储、查询和维护操作
 */
@Mapper
public interface StaffUserMapper extends BaseMapper<StaffUserEntity> {
}
