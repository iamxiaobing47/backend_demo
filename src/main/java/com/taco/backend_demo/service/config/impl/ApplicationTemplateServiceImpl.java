package com.taco.backend_demo.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taco.backend_demo.entity.config.ApplicationTemplateEntity;
import com.taco.backend_demo.mapper.config.ApplicationTemplateMapper;
import com.taco.backend_demo.service.config.ApplicationTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 申請書テンプレートサービス実装クラス
 */
@Service
public class ApplicationTemplateServiceImpl extends ServiceImpl<ApplicationTemplateMapper, ApplicationTemplateEntity> implements ApplicationTemplateService {

    @Override
    public List<ApplicationTemplateEntity> listByCondition(Integer regionCd, Integer countryCd, Integer productCd) {
        LambdaQueryWrapper<ApplicationTemplateEntity> wrapper = new LambdaQueryWrapper<>();
        if (regionCd != null) {
            wrapper.eq(ApplicationTemplateEntity::getRegionCd, regionCd);
        }
        if (countryCd != null) {
            wrapper.eq(ApplicationTemplateEntity::getCountryCd, countryCd);
        }
        if (productCd != null) {
            wrapper.eq(ApplicationTemplateEntity::getProductCd, productCd);
        }
        return list(wrapper);
    }

    @Override
    @Transactional
    public void create(ApplicationTemplateEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        save(entity);
    }

    @Override
    @Transactional
    public void update(ApplicationTemplateEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        updateById(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        removeById(id);
    }
}
