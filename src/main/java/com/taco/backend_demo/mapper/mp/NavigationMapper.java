package com.taco.backend_demo.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.NavigationEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NavigationMapper extends BaseMapper<NavigationEntity> {
}