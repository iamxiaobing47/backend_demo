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
 * 申请书模板服务实现类
 */
@Service
public class ApplicationTemplateServiceImpl extends ServiceImpl<ApplicationTemplateMapper, ApplicationTemplateEntity> implements ApplicationTemplateService {

    @Override
    public List<ApplicationTemplateEntity> listAll() {
        return list();
    }

    @Override
    public List<ApplicationTemplateEntity> listByRegion(Integer regionCd) {
        LambdaQueryWrapper<ApplicationTemplateEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApplicationTemplateEntity::getRegionCd, regionCd);
        return list(wrapper);
    }

    @Override
    public List<ApplicationTemplateEntity> listByCountry(Integer countryCd) {
        LambdaQueryWrapper<ApplicationTemplateEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApplicationTemplateEntity::getCountryCd, countryCd);
        return list(wrapper);
    }

    @Override
    public List<ApplicationTemplateEntity> listByProduct(Integer productCd) {
        LambdaQueryWrapper<ApplicationTemplateEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApplicationTemplateEntity::getProductCd, productCd);
        return list(wrapper);
    }

    @Override
    public ApplicationTemplateEntity getById(Integer id) {
        return getById(id);
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
