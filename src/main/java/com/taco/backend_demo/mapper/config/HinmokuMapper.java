package com.taco.backend_demo.mapper.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.config.HinmokuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 品目 Mapper 接口
 */
@Mapper
public interface HinmokuMapper extends BaseMapper<HinmokuEntity> {
}
