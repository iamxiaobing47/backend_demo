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
        return super.getById(id);
    }

    @Override
    @Transactional
    public void create(CountryEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        // 如果 countryCd 为空，手动生成一个值（最大 id + 1）
        if (entity.getCountryCd() == null) {
            LambdaQueryWrapper<CountryEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(CountryEntity::getCountryCd).last("LIMIT 1");
            CountryEntity maxCountry = this.getOne(wrapper);
            int newCountryCd = (maxCountry != null && maxCountry.getCountryCd() != null)
                ? maxCountry.getCountryCd() + 1 : 1;
            entity.setCountryCd(newCountryCd);
        }
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
