package com.taco.backend_demo.mapper.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.config.YoushikiEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 申请书模板 Mapper 接口
 */
@Mapper
public interface YoushikiMapper extends BaseMapper<YoushikiEntity> {
}
