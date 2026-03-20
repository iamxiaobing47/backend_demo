package com.taco.backend_demo.service.config.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taco.backend_demo.entity.config.ChiikiEntity;
import com.taco.backend_demo.mapper.config.ChiikiMapper;
import com.taco.backend_demo.service.config.ChiikiService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 地域服务实现类
 */
@Service
public class ChiikiServiceImpl extends ServiceImpl<ChiikiMapper, ChiikiEntity> implements ChiikiService {

    @Override
    public List<ChiikiEntity> listAll() {
        return list();
    }

    @Override
    public ChiikiEntity getById(Integer id) {
        return getById(id);
    }

    @Override
    @Transactional
    public void create(ChiikiEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        save(entity);
    }

    @Override
    @Transactional
    public void update(ChiikiEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        updateById(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        removeById(id);
    }
}
