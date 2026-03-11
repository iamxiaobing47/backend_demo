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

@RestController
@RequestMapping("/api/navigations")
public class NavigationController {

    private final NavigationMapper navigationMapper;
    private final BusinessStaffNavigationMapper businessStaffNavigationMapper;

    public NavigationController(NavigationMapper navigationMapper, BusinessStaffNavigationMapper businessStaffNavigationMapper) {
        this.navigationMapper = navigationMapper;
        this.businessStaffNavigationMapper = businessStaffNavigationMapper;
    }

    /**
     * 获取当前用户的导航菜单 (基于登录信息)
     */
    @GetMapping("/user")
    public ResponseEntity<Response<List<NavigationDTO>>> getUserNavigations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        // 根据用户类型和组织ID查询关联的导航菜单
        String orgId = userInfo.getOrgId();
        String userType = userInfo.getUserType();
        
        QueryWrapper<BusinessStaffNavigationEntity> queryWrapper = new QueryWrapper<>();
        if ("BUSINESS_USER".equals(userType)) {
            queryWrapper.eq("business_id", orgId);
        } else if ("STAFF_USER".equals(userType)) {
            queryWrapper.eq("location_id", orgId);
        }
        List<BusinessStaffNavigationEntity> businessStaffNavigations = businessStaffNavigationMapper.selectList(queryWrapper);
        
        // 提取所有 navigationPk
        List<Long> navigationPks = businessStaffNavigations.stream()
                .map(BusinessStaffNavigationEntity::getNavigationPk)
                .collect(Collectors.toList());
        
        // 查询对应的导航菜单
        QueryWrapper<NavigationEntity> navigationQueryWrapper = new QueryWrapper<>();
        navigationQueryWrapper.in("pk", navigationPks);
        navigationQueryWrapper.orderByAsc("sort_order");
        
        List<NavigationEntity> navigationEntities = navigationMapper.selectList(navigationQueryWrapper);
        
        // 转换为 DTO 并构建树形结构
        List<NavigationDTO> navigationDTOs = navigationEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        // 构建树形结构
        List<NavigationDTO> rootMenus = buildTree(navigationDTOs);

        return ResponseFactory.success(rootMenus);
    }
    
    private NavigationDTO convertToDTO(NavigationEntity entity) {
        NavigationDTO dto = new NavigationDTO();
        dto.setPk(entity.getPk());
        dto.setChineseName(entity.getChineseName());
        dto.setEnglishName(entity.getEnglishName());
        dto.setPath(entity.getPath());
        dto.setIcon(entity.getIcon());
        dto.setSortOrder(entity.getSortOrder());
        dto.setParentId(entity.getParentId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
    
    private List<NavigationDTO> buildTree(List<NavigationDTO> menus) {
        Map<Long, NavigationDTO> menuMap = new HashMap<>();
        List<NavigationDTO> rootMenus = new ArrayList<>();
        
        // 将所有菜单放入 map 中
        for (NavigationDTO menu : menus) {
            menuMap.put(menu.getPk(), menu);
        }
        
        // 构建父子关系
        for (NavigationDTO menu : menus) {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                // 根节点
                rootMenus.add(menu);
            } else {
                // 找到父节点
                NavigationDTO parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menu);
                }
            }
        }
        
        // 按排序字段排序
        rootMenus.sort(Comparator.comparing(NavigationDTO::getSortOrder, Comparator.nullsLast(Integer::compareTo)));
        for (NavigationDTO root : rootMenus) {
            sortChildren(root);
        }
        
        return rootMenus;
    }
    
    private void sortChildren(NavigationDTO menu) {
        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            menu.getChildren().sort(Comparator.comparing(NavigationDTO::getSortOrder, Comparator.nullsLast(Integer::compareTo)));
            for (NavigationDTO child : menu.getChildren()) {
                sortChildren(child);
            }
        }
    }

}