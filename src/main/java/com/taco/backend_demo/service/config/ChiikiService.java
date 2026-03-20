package com.taco.backend_demo.service.config;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taco.backend_demo.entity.config.ChiikiEntity;

import java.util.List;

/**
 * 地域服务接口
 */
public interface ChiikiService extends IService<ChiikiEntity> {

    /**
     * 查询所有地域列表
     */
    List<ChiikiEntity> listAll();

    /**
     * 根据 ID 查询地域
     */
    ChiikiEntity getById(Integer id);

    /**
     * 创建地域
     */
    void create(ChiikiEntity entity);

    /**
     * 更新地域
     */
    void update(ChiikiEntity entity);

    /**
     * 删除地域
     */
    void delete(Integer id);
}
