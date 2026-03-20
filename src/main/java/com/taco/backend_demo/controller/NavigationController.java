package com.taco.backend_demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taco.backend_demo.dto.navigation.NavigationDTO;
import com.taco.backend_demo.dto.user.UserInfo;
import com.taco.backend_demo.entity.BusinessStaffNavigationEntity;
import com.taco.backend_demo.entity.NavigationEntity;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.mapper.mp.BusinessStaffNavigationMapper;
import com.taco.backend_demo.mapper.mp.NavigationMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 1. 导航控制器：处理用户导航菜单的获取和管理
 * 2. 权限控制：根据用户类型和组织ID返回对应的导航菜单
 * 3. 树形结构：构建层级化的导航菜单结构，支持多级菜单
 */
@RestController
@RequestMapping("/api/navigations")
public class NavigationController {

    private final NavigationMapper navigationMapper;
    private final BusinessStaffNavigationMapper businessStaffNavigationMapper;

    /**
     * 1. 构造函数注入：初始化导航相关的Mapper依赖
     * @param navigationMapper 导航实体Mapper
     * @param businessStaffNavigationMapper 业务-员工-导航关联Mapper
     */
    public NavigationController(NavigationMapper navigationMapper, BusinessStaffNavigationMapper businessStaffNavigationMapper) {
        this.navigationMapper = navigationMapper;
        this.businessStaffNavigationMapper = businessStaffNavigationMapper;
    }

    /**
     * 1. 获取用户导航菜单：根据当前登录用户返回对应的导航菜单
     * @return 包含树形结构导航菜单的成功响应
     */
    @GetMapping("/user")
    public ResponseEntity<Response<List<NavigationDTO>>> getUserNavigations() {
        // 1. 获取当前认证用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        // 2. 根据用户类型和组织ID查询关联的导航菜单
        String orgId = userInfo.getOrgId();
        String userType = userInfo.getUserType();
        
        QueryWrapper<BusinessStaffNavigationEntity> queryWrapper = new QueryWrapper<>();
        if ("BUSINESS_USER".equals(userType)) {
            queryWrapper.eq("business_id", orgId);
        } else if ("STAFF_USER".equals(userType)) {
            queryWrapper.eq("location_id", orgId);
        }
        List<BusinessStaffNavigationEntity> businessStaffNavigations = businessStaffNavigationMapper.selectList(queryWrapper);
        
        // 3. 提取所有导航菜单ID
        List<Long> navigationPks = businessStaffNavigations.stream()
                .map(BusinessStaffNavigationEntity::getNavigationPk)
                .collect(Collectors.toList());
        
        // 4. 查询对应的导航菜单实体
        QueryWrapper<NavigationEntity> navigationQueryWrapper = new QueryWrapper<>();
        navigationQueryWrapper.in("pk", navigationPks);
        navigationQueryWrapper.orderByAsc("sort_order");
        
        List<NavigationEntity> navigationEntities = navigationMapper.selectList(navigationQueryWrapper);
        
        // 5. 转换为DTO并构建树形结构
        List<NavigationDTO> navigationDTOs = navigationEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        // 6. 构建层级化的导航菜单树
        List<NavigationDTO> rootMenus = buildTree(navigationDTOs);

        return ResponseFactory.success(rootMenus);
    }
    
    /**
     * 2. 实体转换：将NavigationEntity转换为NavigationDTO
     * @param entity 导航实体对象
     * @return 导航DTO对象
     */
    private NavigationDTO convertToDTO(NavigationEntity entity) {
        NavigationDTO dto = new NavigationDTO();
        dto.setPk(entity.getPk());
        dto.setName(entity.getName());
        dto.setPath(entity.getPath());
        dto.setIcon(entity.getIcon());
        dto.setSortOrder(entity.getSortOrder());
        dto.setParentId(entity.getParentId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
    
    /**
     * 3. 构建树形结构：将扁平的菜单列表转换为层级化的树形结构
     * @param menus 菜单DTO列表
     * @return 根节点菜单列表
     */
    private List<NavigationDTO> buildTree(List<NavigationDTO> menus) {
        Map<Long, NavigationDTO> menuMap = new HashMap<>();
        List<NavigationDTO> rootMenus = new ArrayList<>();
        
        // 1. 将所有菜单放入Map中，便于快速查找
        for (NavigationDTO menu : menus) {
            menuMap.put(menu.getPk(), menu);
        }
        
        // 2. 构建父子关系
        for (NavigationDTO menu : menus) {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                // 根节点菜单
                rootMenus.add(menu);
            } else {
                // 找到父节点并添加子菜单
                NavigationDTO parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menu);
                }
            }
        }
        
        // 3. 按排序字段对菜单进行排序
        rootMenus.sort(Comparator.comparing(NavigationDTO::getSortOrder, Comparator.nullsLast(Integer::compareTo)));
        for (NavigationDTO root : rootMenus) {
            sortChildren(root);
        }
        
        return rootMenus;
    }
    
    /**
     * 4. 递归排序子菜单：对菜单及其所有子菜单按排序字段进行排序
     * @param menu 菜单DTO对象
     */
    private void sortChildren(NavigationDTO menu) {
        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            menu.getChildren().sort(Comparator.comparing(NavigationDTO::getSortOrder, Comparator.nullsLast(Integer::compareTo)));
            for (NavigationDTO child : menu.getChildren()) {
                sortChildren(child);
            }
        }
    }
}
