package com.taco.backend_demo.mapper.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.PasswordEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PasswordMybatisMapper extends BaseMapper<PasswordEntity> {
}
