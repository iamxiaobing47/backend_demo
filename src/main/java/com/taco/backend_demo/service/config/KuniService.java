package com.taco.backend_demo.service.config;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taco.backend_demo.entity.config.KuniEntity;

import java.util.List;

/**
 * 国家服务接口
 */
public interface KuniService extends IService<KuniEntity> {

    /**
     * 查询所有国家列表
     */
    List<KuniEntity> listAll();

    /**
     * 根据地域代码查询国家列表
     */
    List<KuniEntity> listByChiiki(Integer chiikiCd);

    /**
     * 根据 ID 查询国家
     */
    KuniEntity getById(Integer id);

    /**
     * 创建国家
     */
    void create(KuniEntity entity);

    /**
     * 更新国家
     */
    void update(KuniEntity entity);

    /**
     * 删除国家
     */
    void delete(Integer id);
}
