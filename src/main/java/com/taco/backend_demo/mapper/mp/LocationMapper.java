package com.taco.backend_demo.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.LocationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 1. 位置实体Mapper：提供位置数据的数据库操作
 * 2. MyBatis Plus集成：继承BaseMapper提供基础CRUD操作
 * 3. 位置管理：支持位置信息的存储、查询和维护操作
 */
@Mapper
public interface LocationMapper extends BaseMapper<LocationEntity> {
}
