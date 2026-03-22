package com.taco.backend_demo.service.config;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taco.backend_demo.entity.config.ProductEntity;

import java.util.List;

/**
 * 品目服务接口
 */
public interface ProductService extends IService<ProductEntity> {

    /**
     * 获取所有品目
     */
    List<ProductEntity> listAll();

    /**
     * 根据 ID 获取品目
     */
    ProductEntity getById(Integer id);

    /**
     * 创建品目
     */
    void create(ProductEntity entity);

    /**
     * 更新品目
     */
    void update(Integer productCd, ProductEntity entity);

    /**
     * 删除品目
     */
    void delete(Integer id);
}
