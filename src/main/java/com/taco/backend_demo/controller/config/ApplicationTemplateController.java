package com.taco.backend_demo.controller.config;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.config.ApplicationTemplateDTO;
import com.taco.backend_demo.entity.config.ApplicationTemplateEntity;
import com.taco.backend_demo.service.config.ApplicationTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 申请书模板管理 Controller
 */
@Tag(name = "申請書テンプレート管理", description = "テンプレート情報の CRUD 操作")
@RestController
@RequestMapping("/api/config/template")
public class ApplicationTemplateController {

    @Autowired
    private ApplicationTemplateService templateService;

    @Operation(summary = "テンプレート一覧取得", description = "条件を指定してテンプレートの一覧を取得します")
    @GetMapping
    public ResponseEntity<Response<List<ApplicationTemplateEntity>>> list(
            @RequestParam(required = false) Integer regionCd,
            @RequestParam(required = false) Integer countryCd,
            @RequestParam(required = false) Integer productCd) {
        List<ApplicationTemplateEntity> list;
        if (regionCd != null || countryCd != null || productCd != null) {
            list = templateService.listByCondition(regionCd, countryCd, productCd);
        } else {
            list = templateService.listAll();
        }
        return ResponseFactory.success(list);
    }

    @Operation(summary = "テンプレート詳細取得", description = "ID を指定してテンプレートの詳細を取得します")
    @GetMapping("/{id}")
    public ResponseEntity<Response<ApplicationTemplateEntity>> get(@PathVariable Integer id) {
        ApplicationTemplateEntity entity = templateService.getById(id);
        return ResponseFactory.success(entity);
    }

    @Operation(summary = "地域別テンプレート取得", description = "地域コードを指定してテンプレートの一覧を取得します")
    @GetMapping("/region/{regionCd}")
    public ResponseEntity<Response<List<ApplicationTemplateEntity>>> listByRegion(@PathVariable Integer regionCd) {
        List<ApplicationTemplateEntity> list = templateService.listByRegion(regionCd);
        return ResponseFactory.success(list);
    }

    @Operation(summary = "国別テンプレート取得", description = "国コードを指定してテンプレートの一覧を取得します")
    @GetMapping("/country/{countryCd}")
    public ResponseEntity<Response<List<ApplicationTemplateEntity>>> listByCountry(@PathVariable Integer countryCd) {
        List<ApplicationTemplateEntity> list = templateService.listByCountry(countryCd);
        return ResponseFactory.success(list);
    }

    @Operation(summary = "品目別テンプレート取得", description = "品目コードを指定してテンプレートの一覧を取得します")
    @GetMapping("/product/{productCd}")
    public ResponseEntity<Response<List<ApplicationTemplateEntity>>> listByProduct(@PathVariable Integer productCd) {
        List<ApplicationTemplateEntity> list = templateService.listByProduct(productCd);
        return ResponseFactory.success(list);
    }

    @Operation(summary = "テンプレート作成", description = "新しいテンプレートを作成します")
    @PostMapping
    public ResponseEntity<Response<Void>> create(@Valid @RequestBody ApplicationTemplateDTO dto) {
        ApplicationTemplateEntity entity = new ApplicationTemplateEntity();
        BeanUtils.copyProperties(dto, entity);
        templateService.create(entity);
        return ResponseFactory.success(null);
    }

    @Operation(summary = "テンプレート更新", description = "テンプレート情報を更新します")
    @PutMapping("/{id}")
    public ResponseEntity<Response<Void>> update(
            @PathVariable Integer id,
            @Valid @RequestBody ApplicationTemplateDTO dto) {
        ApplicationTemplateEntity entity = templateService.getById(id);
        if (entity == null) {
            return ResponseFactory.fail("テンプレートが見つかりません");
        }
        BeanUtils.copyProperties(dto, entity);
        entity.setTemplateId(id);
        templateService.update(entity);
        return ResponseFactory.success(null);
    }

    @Operation(summary = "テンプレート削除", description = "テンプレートを削除します")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Integer id) {
        templateService.delete(id);
        return ResponseFactory.success(null);
    }
}
