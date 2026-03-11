package com.taco.backend_demo.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.BusinessEntity;
import com.taco.backend_demo.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VUserInfoMapper extends BaseMapper<UserInfoEntity> {
}
