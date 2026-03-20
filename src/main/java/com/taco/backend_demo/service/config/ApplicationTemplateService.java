package com.taco.backend_demo.service.config;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taco.backend_demo.entity.config.ApplicationTemplateEntity;

import java.util.List;

/**
 * 申请书模板服务接口
 */
public interface ApplicationTemplateService extends IService<ApplicationTemplateEntity> {

    /**
     * 获取所有模板
     */
    List<ApplicationTemplateEntity> listAll();

    /**
     * 根据地域获取模板列表
     */
    List<ApplicationTemplateEntity> listByRegion(Integer regionCd);

    /**
     * 根据国家获取模板列表
     */
    List<ApplicationTemplateEntity> listByCountry(Integer countryCd);

    /**
     * 根据品目获取模板列表
     */
    List<ApplicationTemplateEntity> listByProduct(Integer productCd);

    /**
     * 根据 ID 获取模板
     */
    ApplicationTemplateEntity getById(Integer id);

    /**
     * 创建模板
     */
    void create(ApplicationTemplateEntity entity);

    /**
     * 更新模板
     */
    void update(ApplicationTemplateEntity entity);

    /**
     * 删除模板
     */
    void delete(Integer id);
}
