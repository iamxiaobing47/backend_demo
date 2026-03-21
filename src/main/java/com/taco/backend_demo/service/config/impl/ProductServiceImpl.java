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
        // 如果 productCd 为空，手动生成一个值（最大 id + 1）
        if (entity.getProductCd() == null) {
            LambdaQueryWrapper<ProductEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(ProductEntity::getProductCd).last("LIMIT 1");
            ProductEntity maxProduct = this.getOne(wrapper);
            int newProductCd = (maxProduct != null && maxProduct.getProductCd() != null)
                ? maxProduct.getProductCd() + 1 : 1;
            entity.setProductCd(newProductCd);
        }
        save(entity);
    }

    @Override
    @Transactional
    public void update(ProductEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        updateById(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        removeById(id);
    }
}
