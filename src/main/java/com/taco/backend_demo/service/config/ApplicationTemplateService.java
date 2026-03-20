package com.taco.backend_demo.service.config;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taco.backend_demo.entity.config.ApplicationTemplateEntity;

import java.util.List;

/**
 * 申請書テンプレートサービスインターフェース
 */
public interface ApplicationTemplateService extends IService<ApplicationTemplateEntity> {

    /**
     * 条件を指定してテンプレートの一覧を取得します
     */
    List<ApplicationTemplateEntity> listByCondition(Integer regionCd, Integer countryCd, Integer productCd);

    /**
     * テンプレートを作成します
     */
    void create(ApplicationTemplateEntity entity);

    /**
     * テンプレートを更新します
     */
    void update(ApplicationTemplateEntity entity);

    /**
     * テンプレートを削除します
     */
    void delete(Integer id);
}
