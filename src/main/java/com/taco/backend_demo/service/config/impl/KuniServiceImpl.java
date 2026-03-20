package com.taco.backend_demo.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taco.backend_demo.entity.config.KuniEntity;
import com.taco.backend_demo.mapper.config.KuniMapper;
import com.taco.backend_demo.service.config.KuniService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 国家服务实现类
 */
@Service
public class KuniServiceImpl extends ServiceImpl<KuniMapper, KuniEntity> implements KuniService {

    @Override
    public List<KuniEntity> listAll() {
        return list();
    }

    @Override
    public List<KuniEntity> listByChiiki(Integer chiikiCd) {
        LambdaQueryWrapper<KuniEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KuniEntity::getChiikiCd, chiikiCd);
        return list(wrapper);
    }

    @Override
    public KuniEntity getById(Integer id) {
        return getById(id);
    }

    @Override
    @Transactional
    public void create(KuniEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        save(entity);
    }

    @Override
    @Transactional
    public void update(KuniEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        updateById(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        removeById(id);
    }
}
