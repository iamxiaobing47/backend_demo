package com.taco.backend_demo.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.StaffUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface StaffUserMapper extends BaseMapper<StaffUserEntity> {
    
    @Select("SELECT * FROM staff_users WHERE email = #{email}")
    StaffUserEntity selectByEmail(@Param("email") String email);
    
    @Delete("DELETE FROM staff_users WHERE email = #{email}")
    void deleteByEmail(@Param("email") String email);
}
