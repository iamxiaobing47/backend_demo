package com.taco.backend_demo.service.config;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taco.backend_demo.entity.config.HinmokuEntity;

import java.util.List;

/**
 * 品目服务接口
 */
public interface HinmokuService extends IService<HinmokuEntity> {

    /**
     * 查询所有品目列表
     */
    List<HinmokuEntity> listAll();

    /**
     * 根据 ID 查询品目
     */
    HinmokuEntity getById(Integer id);

    /**
     * 创建品目
     */
    void create(HinmokuEntity entity);

    /**
     * 更新品目
     */
    void update(HinmokuEntity entity);

    /**
     * 删除品目
     */
    void delete(Integer id);
}
