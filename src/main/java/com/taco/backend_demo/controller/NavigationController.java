package com.taco.backend_demo.controller;

import com.taco.backend_demo.dto.NavigationDto;
import com.taco.backend_demo.entity.NavigationEntity;
import com.taco.backend_demo.mapper.NavigationMapper;
import com.taco.backend_demo.security.LoginUser;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/navigations")
public class NavigationController {

    private final NavigationMapper navigationMapper;

    public NavigationController(NavigationMapper navigationMapper) {
        this.navigationMapper = navigationMapper;
    }

    /**
     * 根据用户类型获取导航菜单 (保持向后兼容)
     */
    @GetMapping("/by-type")
    public ResponseEntity<Response<List<NavigationDto>>> getNavigationsByType(
            @RequestParam String userType,
            @RequestParam(required = false) String associatedId) {
        List<NavigationDto> navigations = getNavigationsByUserType(userType, associatedId);
        return ResponseFactory.success(navigations, "N001", "获取用户导航菜单成功");
    }
    
    /**
     * 获取当前用户的导航菜单 (基于登录信息)
     */
    @GetMapping("/user")
    public ResponseEntity<Response<List<NavigationDto>>> getUserNavigations() {
        // This method will be updated to extract user info from JWT and call the appropriate method
        // For now, we'll implement the logic in the service layer to check the authenticated user
        List<NavigationDto> navigations = getCurrentUserNavigations();
        return ResponseFactory.success(navigations, "N001", "获取用户导航菜单成功");
    }

    /**
     * 获取所有导航菜单（仅启用的）
     */
    @GetMapping("/all")
    public ResponseEntity<Response<List<NavigationDto>>> getAllNavigations() {
        List<NavigationDto> navigations = fetchAllNavigations();
        return ResponseFactory.success(navigations, "N002", "获取所有导航菜单成功");
    }

    /**
     * 根据用户类型和关联ID获取导航菜单 (支持更细粒度的权限控制)
     */
    private List<NavigationDto> getNavigationsByUserType(String userType, String associatedId) {
        List<NavigationEntity> entities = navigationMapper.selectByUserTypeAndAssociation(userType, associatedId);
        return buildNavigationTree(entities);
    }
    
    /**
     * 获取当前用户的导航菜单 (从SecurityContext获取用户信息)
     */
    private List<NavigationDto> getCurrentUserNavigations() {
        // 获取当前认证用户的信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // 未认证用户返回空菜单
            return new ArrayList<>();
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof LoginUser loginUser) {
            String role = loginUser.getRole();
            String associationId = null;
            
            // 根据用户角色设置关联ID
            if ("business_owner".equals(role)) {
                associationId = loginUser.getBusinessOwnerId();
            } else if ("employee".equals(role)) {
                associationId = loginUser.getLocationId();
            }
            
            // 特殊情况：如果用户是据点X的员工，则返回所有菜单
            // In the demo, location "1" represents 据点X which has access to all menus
            if ("employee".equals(role) && "1".equals(associationId)) {
                return fetchAllNavigations();
            }
            
            // 调用带关联ID的方法
            return getNavigationsByUserType(role, associationId);
        }
        
        // 默认返回空菜单
        return new ArrayList<>();
    }

    /**
     * 获取所有导航菜单（仅启用的）
     */
    private List<NavigationDto> fetchAllNavigations() {
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