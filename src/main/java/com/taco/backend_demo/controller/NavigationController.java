package com.taco.backend_demo.controller;

import com.taco.backend_demo.dto.NavigationDto;
import com.taco.backend_demo.service.NavigationService;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/navigations")
public class NavigationController {

    private final NavigationService navigationService;

    public NavigationController(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    /**
     * 根据用户类型获取导航菜单 (保持向后兼容)
     */
    @GetMapping("/by-type")
    public ResponseEntity<Response<List<NavigationDto>>> getNavigationsByType(
            @RequestParam String userType,
            @RequestParam(required = false) String associatedId) {
        List<NavigationDto> navigations = navigationService.getNavigationsByUserType(userType, associatedId);
        return ResponseFactory.success(navigations, "N001", "获取用户导航菜单成功");
    }
    
    /**
     * 获取当前用户的导航菜单 (基于登录信息)
     */
    @GetMapping("/user")
    public ResponseEntity<Response<List<NavigationDto>>> getUserNavigations() {
        // This method will be updated to extract user info from JWT and call the appropriate method
        // For now, we'll implement the logic in the service layer to check the authenticated user
        List<NavigationDto> navigations = navigationService.getCurrentUserNavigations();
        return ResponseFactory.success(navigations, "N001", "获取用户导航菜单成功");
    }

    /**
     * 获取所有导航菜单（仅启用的）
     */
    @GetMapping("/all")
    public ResponseEntity<Response<List<NavigationDto>>> getAllNavigations() {
        List<NavigationDto> navigations = navigationService.getAllNavigations();
        return ResponseFactory.success(navigations, "N002", "获取所有导航菜单成功");
    }
}