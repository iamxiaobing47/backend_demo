package com.taco.backend_demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taco.backend_demo.entity.NavigationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NavigationMapper extends BaseMapper<NavigationEntity> {
    
    /**
     * 根据用户类型获取启用的导航菜单
     */
    @Select("<script>" +
            "SELECT * FROM navigations " +
            "WHERE enabled = true " +
            "<if test='userType != null and userType != \"\"'>" +
            "AND (user_type IS NULL OR user_type = '' OR user_type = #{userType}) " +
            "</if>" +
            "ORDER BY parent_id ASC, sort_order ASC" +
            "</script>")
    List<NavigationEntity> selectByUserType(@Param("userType") String userType);
    
    /**
     * 获取所有导航菜单（按父级分组）
     */
    @Select("SELECT * FROM navigations WHERE enabled = true ORDER BY parent_id ASC, sort_order ASC")
    List<NavigationEntity> selectAllEnabled();
}