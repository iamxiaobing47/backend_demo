package com.taco.backend_demo.service.config;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taco.backend_demo.entity.config.YoushikiEntity;

import java.util.List;

/**
 * 申请书模板服务接口
 */
public interface YoushikiService extends IService<YoushikiEntity> {

    /**
     * 查询所有模板列表
     */
    List<YoushikiEntity> listAll();

    /**
     * 根据国代码查询模板列表
     */
    List<YoushikiEntity> listByKuni(Integer kuniCd);

    /**
     * 根据品目代码查询模板列表
     */
    List<YoushikiEntity> listByHinmoku(Integer hinmokuCd);

    /**
     * 根据 ID 查询模板
     */
    YoushikiEntity getById(Integer id);

    /**
     * 创建模板
     */
    void create(YoushikiEntity entity);

    /**
     * 更新模板
     */
    void update(YoushikiEntity entity);

    /**
     * 删除模板
     */
    void delete(Integer id);
}
