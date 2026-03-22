package com.taco.backend_demo.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taco.backend_demo.entity.config.RegionEntity;
import com.taco.backend_demo.mapper.config.RegionMapper;
import com.taco.backend_demo.service.config.RegionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 地域服务实现类
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, RegionEntity> implements RegionService {

    @Override
    public List<RegionEntity> listAll() {
        return list();
    }

    @Override
    public RegionEntity getById(Integer id) {
        return super.getById(id);
    }

    @Override
    @Transactional
    public void create(RegionEntity entity) {
//        entity.setCreatedAt(LocalDateTime.now());
//        entity.setUpdatedAt(LocalDateTime.now());
        save(entity);
    }

    @Override
    @Transactional
    public void update(Integer regionCd, RegionEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());

        LambdaQueryWrapper<RegionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RegionEntity::getRegionCd, regionCd);
        update(entity, wrapper);
    }

    @Override
    @Transactional
    public void delete(Integer regionCd) {
        // regionCd を使用して削除
        LambdaQueryWrapper<RegionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RegionEntity::getRegionCd, regionCd);
        remove(wrapper);
    }
}
