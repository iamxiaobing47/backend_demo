package com.taco.backend_demo.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.PasswordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 1. 密码实体Mapper：提供密码相关数据的数据库操作
 * 2. MyBatis Plus集成：继承BaseMapper提供基础CRUD操作
 * 3. 自定义查询：支持按邮箱查询密码记录
 */
@Mapper
public interface PasswordMapper extends BaseMapper<PasswordEntity> {

    /**
     * 1. 根据邮箱查询密码实体：用于用户登录和密码验证
     * @param email 用户邮箱地址
     * @return 密码实体对象，如果不存在则返回null
     */
    @Select("SELECT * FROM password WHERE email = #{email}")
    PasswordEntity selectByEmail(String email);
}
