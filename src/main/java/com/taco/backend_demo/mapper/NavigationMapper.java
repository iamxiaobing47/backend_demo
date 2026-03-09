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
     * 根据用户类型和关联ID获取导航菜单（支持特殊权限逻辑）
     */
    @Select("<script>" +
            "SELECT * FROM navigations " +
            "WHERE enabled = true " +
            "AND (" +
            "  (#{userType} = 'business_owner' AND (user_type IS NULL OR user_type = 'business_owner' OR user_type = '')" +
            "  AND (business_owner_restrictions IS NULL OR business_owner_restrictions = '' OR FIND_IN_SET(#{associatedId}, business_owner_restrictions))) " +
            "  OR " +
            "  (#{userType} = 'employee' AND (user_type IS NULL OR user_type = 'employee' OR user_type = '')" +
            "  AND (location_restrictions IS NULL OR location_restrictions = '' OR FIND_IN_SET(#{associatedId}, location_restrictions))) " +
            ") " +
            "ORDER BY parent_id ASC, sort_order ASC" +
            "</script>")
    List<NavigationEntity> selectByUserTypeAndAssociation(
            @Param("userType") String userType, 
            @Param("associatedId") String associatedId);
    
    /**
     * 获取所有导航菜单（按父级分组）
     */
    @Select("SELECT * FROM navigations WHERE enabled = true ORDER BY parent_id ASC, sort_order ASC")
    List<NavigationEntity> selectAllEnabled();
}