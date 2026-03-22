package com.taco.backend_demo.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taco.backend_demo.entity.config.ProductEntity;
import com.taco.backend_demo.mapper.config.ProductMapper;
import com.taco.backend_demo.service.config.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 品目服务实现类
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductEntity> implements ProductService {

    @Override
    public List<ProductEntity> listAll() {
        return list();
    }

    @Override
    public ProductEntity getById(Integer id) {
        return super.getById(id);
    }

    @Override
    @Transactional
    public void create(ProductEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        save(entity);
    }

    @Override
    @Transactional
    public void update(Integer productCd, ProductEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        // 使用 pathVariable の productCd を使用して更新
        LambdaQueryWrapper<ProductEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductEntity::getProductCd, productCd);
        update(entity, wrapper);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        removeById(id);
    }
}
