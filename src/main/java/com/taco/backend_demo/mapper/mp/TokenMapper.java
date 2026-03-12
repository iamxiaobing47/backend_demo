package com.taco.backend_demo.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.TokenEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 1. 令牌实体Mapper：提供刷新令牌的数据库存储和管理操作
 * 2. MyBatis Plus集成：继承BaseMapper提供基础CRUD操作
 * 3. 令牌管理：支持刷新令牌的存储、查询和删除操作
 */
@Mapper
public interface TokenMapper extends BaseMapper<TokenEntity> {
}
