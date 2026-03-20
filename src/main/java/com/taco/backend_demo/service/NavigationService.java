package com.taco.backend_demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taco.backend_demo.dto.navigation.NavigationDTO;
import com.taco.backend_demo.entity.NavigationEntity;

import java.util.List;

/**
 * 导航服务接口
 */
public interface NavigationService extends IService<NavigationEntity> {

    /**
     * 获取用户导航菜单
     * @param orgId 组织 ID
     * @param userType 用户类型
     * @return 导航菜单树形结构
     */
    List<NavigationDTO> getUserNavigations(String orgId, String userType);
}
