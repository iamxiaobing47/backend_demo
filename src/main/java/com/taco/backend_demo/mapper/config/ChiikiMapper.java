package com.taco.backend_demo.mapper.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.config.ChiikiEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地域 Mapper 接口
 */
@Mapper
public interface ChiikiMapper extends BaseMapper<ChiikiEntity> {
}
