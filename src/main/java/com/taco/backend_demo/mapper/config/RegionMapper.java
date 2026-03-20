package com.taco.backend_demo.mapper.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.config.RegionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地域 Mapper 接口
 */
@Mapper
public interface RegionMapper extends BaseMapper<RegionEntity> {
}
