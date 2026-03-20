package com.taco.backend_demo.service.config;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taco.backend_demo.entity.config.RegionEntity;

import java.util.List;

/**
 * 地域服务接口
 */
public interface RegionService extends IService<RegionEntity> {

    /**
     * 获取所有地域
     */
    List<RegionEntity> listAll();

    /**
     * 根据 ID 获取地域
     */
    RegionEntity getById(Integer id);

    /**
     * 创建地域
     */
    void create(RegionEntity entity);

    /**
     * 更新地域
     */
    void update(RegionEntity entity);

    /**
     * 删除地域
     */
    void delete(Integer id);
}
