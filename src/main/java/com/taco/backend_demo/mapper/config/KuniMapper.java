package com.taco.backend_demo.mapper.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.config.KuniEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 国家 Mapper 接口
 */
@Mapper
public interface KuniMapper extends BaseMapper<KuniEntity> {
}
