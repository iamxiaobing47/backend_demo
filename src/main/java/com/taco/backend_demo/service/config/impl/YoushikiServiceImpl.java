package com.taco.backend_demo.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taco.backend_demo.entity.config.YoushikiEntity;
import com.taco.backend_demo.mapper.config.YoushikiMapper;
import com.taco.backend_demo.service.config.YoushikiService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 申请书模板服务实现类
 */
@Service
public class YoushikiServiceImpl extends ServiceImpl<YoushikiMapper, YoushikiEntity> implements YoushikiService {

    @Override
    public List<YoushikiEntity> listAll() {
        return list();
    }

    @Override
    public List<YoushikiEntity> listByKuni(Integer kuniCd) {
        LambdaQueryWrapper<YoushikiEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YoushikiEntity::getKuniCd, kuniCd);
        return list(wrapper);
    }

    @Override
    public List<YoushikiEntity> listByHinmoku(Integer hinmokuCd) {
        LambdaQueryWrapper<YoushikiEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YoushikiEntity::getHinmokuCd, hinmokuCd);
        return list(wrapper);
    }

    @Override
    public YoushikiEntity getById(Integer id) {
        return getById(id);
    }

    @Override
    @Transactional
    public void create(YoushikiEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        save(entity);
    }

    @Override
    @Transactional
    public void update(YoushikiEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        updateById(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        removeById(id);
    }
}
