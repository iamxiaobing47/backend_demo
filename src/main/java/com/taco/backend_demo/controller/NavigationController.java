package com.taco.backend_demo.controller;

import com.taco.backend_demo.dto.navigation.NavigationDTO;
import com.taco.backend_demo.entity.NavigationEntity;
import com.taco.backend_demo.dto.auth.LoginUserInfo;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.mapper.mp.NavigationMapper;
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

//    /**
//     * 根据用户类型获取导航菜单 (保持向后兼容)
//     */
//    @GetMapping("/by-type")
//    public ResponseEntity<Response<List<NavigationDTO>>> getNavigationsByType(
//            @RequestParam String userType,
//            @RequestParam(required = false) String associatedId) {
//        List<NavigationDTO> navigations = getNavigationsByUserType(userType, associatedId);
//        return ResponseFactory.success(navigations, "N001", "获取用户导航菜单成功");
//    }
//
//    /**
//     * 获取当前用户的导航菜单 (基于登录信息)
//     */
//    @GetMapping("/user")
//    public ResponseEntity<Response<List<NavigationDTO>>> getUserNavigations() {
//        // This method will be updated to extract user info from JWT and call the appropriate method
//        // For now, we'll implement the logic in the service layer to check the authenticated user
//        List<NavigationDTO> navigations = getCurrentUserNavigations();
//        return ResponseFactory.success(navigations, "N001", "获取用户导航菜单成功");
//    }

//    /**
//     * 获取所有导航菜单（仅启用的）
//     */
//    @GetMapping("/all")
//    public ResponseEntity<Response<List<NavigationDTO>>> getAllNavigations() {
//        List<NavigationDTO> navigations = fetchAllNavigations();
//        return ResponseFactory.success(navigations, "N002", "获取所有导航菜单成功");
//    }
//
//    /**
//     * 根据用户类型和关联ID获取导航菜单 (支持更细粒度的权限控制)
//     */
//    private List<NavigationDTO> getNavigationsByUserType(String userType, String associatedId) {
//        List<NavigationEntity> entities = navigationMapper.selectByUserTypeAndAssociation(userType, associatedId);
//        return buildNavigationTree(entities);
//    }
//
//    /**
//     * 获取当前用户的导航菜单 (从SecurityContext获取用户信息)
//     */
//    private List<NavigationDTO> getCurrentUserNavigations() {
//        // 获取当前认证用户的信息
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            // 未认证用户返回空菜单
//            return new ArrayList<>();
//        }
//
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof LoginUserInfo loginUserInfo) {
//            String role = loginUserInfo.getRole();
//            String associationId = null;
//
//            // 根据用户角色设置关联ID
//            if ("business_owner".equals(role)) {
//                associationId = loginUserInfo.getBusinessOwnerId();
//            } else if ("employee".equals(role)) {
//                associationId = loginUserInfo.getLocationId();
//            }
//
//            // 特殊情况：如果用户是据点X的员工，则返回所有菜单
//            // In the demo, location "1" represents 据点X which has access to all menus
//            if ("employee".equals(role) && "1".equals(associationId)) {
//                return fetchAllNavigations();
//            }
//
//            // 调用带关联ID的方法
//            return getNavigationsByUserType(role, associationId);
//        }
//
//        // 默认返回空菜单
//        return new ArrayList<>();
//    }
//
//    /**
//     * 获取所有导航菜单（仅启用的）
//     */
//    private List<NavigationDTO> fetchAllNavigations() {
//        List<NavigationEntity> entities = navigationMapper.selectAllEnabled();
//        return buildNavigationTree(entities);
//    }
//
//    /**
//     * 构建导航树形结构
//     */
//    private List<NavigationDTO> buildNavigationTree(List<NavigationEntity> entities) {
//        // 创建所有节点的映射
//        List<NavigationDTO> navDtos = entities.stream().map(entity -> {
//            NavigationDTO dto = new NavigationDTO();
//            BeanUtils.copyProperties(entity, dto);
//            return dto;
//        }).collect(Collectors.toList());
//
//        // 创建父节点列表（parentId为null或0的是根节点）
//        List<NavigationDTO> rootNodes = new ArrayList<>();
//
//        // 为每个节点找到其子节点
//        for (NavigationDTO node : navDtos) {
//            if (node.getParentId() == null || node.getParentId() == 0) {
//                rootNodes.add(node);
//            } else {
//                // 查找父节点并添加到其children列表中
//                NavigationDTO parentNode = findById(navDtos, node.getParentId());
//                if (parentNode != null) {
//                    if (parentNode.getChildren() == null) {
//                        parentNode.setChildren(new ArrayList<>());
//                    }
//                    parentNode.getChildren().add(node);
//                }
//            }
//        }
//
//        return rootNodes;
//    }
//
//    /**
//     * 根据ID查找节点
//     */
//    private NavigationDTO findById(List<NavigationDTO> nodes, Long id) {
//        for (NavigationDTO node : nodes) {
//            if (node.getNavigationId().equals(id)) {
//                return node;
//            }
//        }
//        return null;
//    }
}