package com.taco.backend_demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taco.backend_demo.dto.navigation.NavigationDTO;
import com.taco.backend_demo.entity.BusinessStaffNavigationEntity;
import com.taco.backend_demo.entity.NavigationEntity;
import com.taco.backend_demo.mapper.mp.BusinessStaffNavigationMapper;
import com.taco.backend_demo.mapper.mp.NavigationMapper;
import com.taco.backend_demo.service.NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 导航服务实现类
 */
@Service
public class NavigationServiceImpl extends ServiceImpl<NavigationMapper, NavigationEntity> implements NavigationService {

    @Autowired
    private NavigationMapper navigationMapper;

    @Autowired
    private BusinessStaffNavigationMapper businessStaffNavigationMapper;

    @Override
    public List<NavigationDTO> getUserNavigations(String orgId, String userType) {
        // 1. 根据用户类型和组织 ID 查询关联的导航菜单
        QueryWrapper<BusinessStaffNavigationEntity> queryWrapper = new QueryWrapper<>();
        if ("BUSINESS_USER".equals(userType)) {
            // business_pk 是 INTEGER 类型，需要转换
            queryWrapper.eq("business_pk", Integer.parseInt(orgId));
        } else if ("STAFF_USER".equals(userType)) {
            // location_pk 是 BIGINT 类型，需要转换
            queryWrapper.eq("location_pk", Long.parseLong(orgId));
        }
        List<BusinessStaffNavigationEntity> businessStaffNavigations = businessStaffNavigationMapper.selectList(queryWrapper);

        // 2. 提取所有导航菜单 ID
        List<Long> navigationPks = businessStaffNavigations.stream()
                .map(BusinessStaffNavigationEntity::getNavigationPk)
                .collect(Collectors.toList());

        // 3. 查询对应的导航菜单实体
        QueryWrapper<NavigationEntity> navigationQueryWrapper = new QueryWrapper<>();
        navigationQueryWrapper.in("pk", navigationPks);
        navigationQueryWrapper.orderByAsc("sort_order");

        List<NavigationEntity> navigationEntities = navigationMapper.selectList(navigationQueryWrapper);

        // 4. 转换为 DTO 并构建树形结构
        List<NavigationDTO> navigationDTOs = navigationEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // 5. 构建层级化的导航菜单树
        return buildTree(navigationDTOs);
    }

    /**
     * 将 NavigationEntity 转换为 NavigationDTO
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
     * 构建树形结构
     */
    private List<NavigationDTO> buildTree(List<NavigationDTO> menus) {
        Map<Long, NavigationDTO> menuMap = new HashMap<>();
        List<NavigationDTO> rootMenus = new ArrayList<>();

        // 1. 将所有菜单放入 Map 中，便于快速查找
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
     * 递归排序子菜单
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
