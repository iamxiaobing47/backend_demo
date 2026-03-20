package com.taco.backend_demo.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taco.backend_demo.entity.config.CountryEntity;
import com.taco.backend_demo.mapper.config.CountryMapper;
import com.taco.backend_demo.service.config.CountryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 国家服务实现类
 */
@Service
public class CountryServiceImpl extends ServiceImpl<CountryMapper, CountryEntity> implements CountryService {

    @Override
    public List<CountryEntity> listAll() {
        return list();
    }

    @Override
    public List<CountryEntity> listByRegion(Integer regionCd) {
        LambdaQueryWrapper<CountryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CountryEntity::getRegionCd, regionCd);
        return list(wrapper);
    }

    @Override
    public CountryEntity getById(Integer id) {
        return getById(id);
    }

    @Override
    @Transactional
    public void create(CountryEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        save(entity);
    }

    @Override
    @Transactional
    public void update(CountryEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        updateById(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        removeById(id);
    }
}
