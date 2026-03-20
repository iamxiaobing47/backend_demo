package com.taco.backend_demo.controller;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.navigation.NavigationDTO;
import com.taco.backend_demo.dto.user.UserInfo;
import com.taco.backend_demo.service.NavigationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 导航控制器：处理用户导航菜单的获取和管理
 */
@Tag(name = "导航管理", description = "导航菜单相关接口")
@RestController
@RequestMapping("/api/navigations")
public class NavigationController {

    @Autowired
    private NavigationService navigationService;

    /**
     * 获取用户导航菜单
     * @return 包含树形结构导航菜单的成功响应
     */
    @Operation(summary = "获取用户导航菜单", description = "根据当前登录用户返回对应的导航菜单")
    @GetMapping("/user")
    public ResponseEntity<Response<List<NavigationDTO>>> getUserNavigations() {
        // 1. 获取当前认证用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        // 2. 调用 Service 获取导航菜单
        List<NavigationDTO> rootMenus = navigationService.getUserNavigations(userInfo.getOrgId(), userInfo.getUserType());

        return ResponseFactory.success(rootMenus);
    }
}
