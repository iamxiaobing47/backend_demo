package com.taco.backend_demo.service.config.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taco.backend_demo.entity.config.HinmokuEntity;
import com.taco.backend_demo.mapper.config.HinmokuMapper;
import com.taco.backend_demo.service.config.HinmokuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 品目服务实现类
 */
@Service
public class HinmokuServiceImpl extends ServiceImpl<HinmokuMapper, HinmokuEntity> implements HinmokuService {

    @Override
    public List<HinmokuEntity> listAll() {
        return list();
    }

    @Override
    public HinmokuEntity getById(Integer id) {
        return getById(id);
    }

    @Override
    @Transactional
    public void create(HinmokuEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        save(entity);
    }

    @Override
    @Transactional
    public void update(HinmokuEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        updateById(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        removeById(id);
    }
}
