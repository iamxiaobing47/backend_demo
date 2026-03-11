package com.taco.backend_demo.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.BusinessUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface BusinessUserMapper extends BaseMapper<BusinessUserEntity> {
    
    @Select("SELECT * FROM business_users WHERE email = #{email}")
    BusinessUserEntity selectByEmail(@Param("email") String email);
    
    @Delete("DELETE FROM business_users WHERE email = #{email}")
    void deleteByEmail(@Param("email") String email);
}
