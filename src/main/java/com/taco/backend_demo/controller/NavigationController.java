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
     * 根据用户类型获取导航菜单
     */
    @GetMapping("/user")
    public ResponseEntity<Response<List<NavigationDto>>> getUserNavigations(
            @RequestParam String userType) {
        List<NavigationDto> navigations = navigationService.getUserNavigations(userType);
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