package com.taco.backend_demo.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.StaffUserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StaffUserMapper extends BaseMapper<StaffUserEntity> {
}
