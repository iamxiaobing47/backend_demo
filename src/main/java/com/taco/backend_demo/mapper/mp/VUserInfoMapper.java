package com.taco.backend_demo.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 1. 用户信息视图Mapper：提供用户完整信息的数据库查询操作
 * 2. MyBatis Plus集成：继承BaseMapper提供基础CRUD操作
 * 3. 视图查询：基于用户信息视图（包含业务用户和员工用户的完整信息）
 */
@Mapper
public interface VUserInfoMapper extends BaseMapper<UserInfoEntity> {
}
