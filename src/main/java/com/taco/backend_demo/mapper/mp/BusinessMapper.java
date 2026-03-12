package com.taco.backend_demo.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.BusinessEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 1. 业务实体Mapper：提供业务数据的数据库操作
 * 2. MyBatis Plus集成：继承BaseMapper提供基础CRUD操作
 * 3. 业务管理：支持业务信息的存储、查询和维护操作
 */
@Mapper
public interface BusinessMapper extends BaseMapper<BusinessEntity> {
}
