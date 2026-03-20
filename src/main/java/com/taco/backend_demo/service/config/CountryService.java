package com.taco.backend_demo.service.config;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taco.backend_demo.entity.config.CountryEntity;

import java.util.List;

/**
 * 国家服务接口
 */
public interface CountryService extends IService<CountryEntity> {

    /**
     * 获取所有国家
     */
    List<CountryEntity> listAll();

    /**
     * 根据地域获取国家列表
     */
    List<CountryEntity> listByRegion(Integer regionCd);

    /**
     * 根据 ID 获取国家
     */
    CountryEntity getById(Integer id);

    /**
     * 创建国家
     */
    void create(CountryEntity entity);

    /**
     * 更新国家
     */
    void update(CountryEntity entity);

    /**
     * 删除国家
     */
    void delete(Integer id);
}
