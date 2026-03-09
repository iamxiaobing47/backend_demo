package com.taco.backend_demo.service;

import com.taco.backend_demo.dto.NavigationDto;
import com.taco.backend_demo.entity.NavigationEntity;
import com.taco.backend_demo.mapper.NavigationMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NavigationService {

    private final NavigationMapper navigationMapper;

    public NavigationService(NavigationMapper navigationMapper) {
        this.navigationMapper = navigationMapper;
    }

    /**
     * 根据用户类型获取导航菜单
     */
    public List<NavigationDto> getUserNavigations(String userType) {
        List<NavigationEntity> entities = navigationMapper.selectByUserType(userType);
        return buildNavigationTree(entities);
    }

    /**
     * 获取所有导航菜单（仅启用的）
     */
    public List<NavigationDto> getAllNavigations() {
        List<NavigationEntity> entities = navigationMapper.selectAllEnabled();
        return buildNavigationTree(entities);
    }

    /**
     * 构建导航树形结构
     */
    private List<NavigationDto> buildNavigationTree(List<NavigationEntity> entities) {
        // 创建所有节点的映射
        List<NavigationDto> navDtos = entities.stream().map(entity -> {
            NavigationDto dto = new NavigationDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        // 创建父节点列表（parentId为null或0的是根节点）
        List<NavigationDto> rootNodes = new ArrayList<>();
        
        // 为每个节点找到其子节点
        for (NavigationDto node : navDtos) {
            if (node.getParentId() == null || node.getParentId() == 0) {
                rootNodes.add(node);
            } else {
                // 查找父节点并添加到其children列表中
                NavigationDto parentNode = findById(navDtos, node.getParentId());
                if (parentNode != null) {
                    if (parentNode.getChildren() == null) {
                        parentNode.setChildren(new ArrayList<>());
                    }
                    parentNode.getChildren().add(node);
                }
            }
        }

        return rootNodes;
    }

    /**
     * 根据ID查找节点
     */
    private NavigationDto findById(List<NavigationDto> nodes, Long id) {
        for (NavigationDto node : nodes) {
            if (node.getNavigationId().equals(id)) {
                return node;
            }
        }
        return null;
    }
}