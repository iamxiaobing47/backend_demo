package com.taco.backend_demo.mapper.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.TokenEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenMybatisMapper extends BaseMapper<TokenEntity> {
}
