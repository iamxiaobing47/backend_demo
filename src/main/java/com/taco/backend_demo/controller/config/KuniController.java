package com.taco.backend_demo.controller.config;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.config.KuniDTO;
import com.taco.backend_demo.entity.config.KuniEntity;
import com.taco.backend_demo.service.config.KuniService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 国家管理 Controller
 */
@Tag(name = "国家管理", description = "国情報の CRUD 操作")
@RestController
@RequestMapping("/api/config/kuni")
public class KuniController {

    @Autowired
    private KuniService kuniService;

    @Operation(summary = "国家一覧取得", description = "すべての国家を取得します")
    @GetMapping
    public ResponseEntity<Response<List<KuniEntity>>> list() {
        List<KuniEntity> list = kuniService.listAll();
        return ResponseFactory.success(list);
    }

    @Operation(summary = "国家詳細取得", description = "ID を指定して国家の詳細を取得します")
    @GetMapping("/{id}")
    public ResponseEntity<Response<KuniEntity>> get(@PathVariable Integer id) {
        KuniEntity entity = kuniService.getById(id);
        return ResponseFactory.success(entity);
    }

    @Operation(summary = "地域別国家取得", description = "地域コードを指定して国家の一覧を取得します")
    @GetMapping("/chiiki/{chiikiCd}")
    public ResponseEntity<Response<List<KuniEntity>>> listByChiiki(@PathVariable Integer chiikiCd) {
        List<KuniEntity> list = kuniService.listByChiiki(chiikiCd);
        return ResponseFactory.success(list);
    }

    @Operation(summary = "国家作成", description = "新しい国家を作成します")
    @PostMapping
    public ResponseEntity<Response<Void>> create(@Valid @RequestBody KuniDTO dto) {
        KuniEntity entity = new KuniEntity();
        BeanUtils.copyProperties(dto, entity);
        kuniService.create(entity);
        return ResponseFactory.success(null);
    }

    @Operation(summary = "国家更新", description = "国家情報を更新します")
    @PutMapping("/{id}")
    public ResponseEntity<Response<Void>> update(
            @PathVariable Integer id,
            @Valid @RequestBody KuniDTO dto) {
        KuniEntity entity = kuniService.getById(id);
        BeanUtils.copyProperties(dto, entity);
        entity.setKuniCd(id);
        kuniService.update(entity);
        return ResponseFactory.success(null);
    }

    @Operation(summary = "国家削除", description = "国家を削除します")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Integer id) {
        kuniService.delete(id);
        return ResponseFactory.success(null);
    }
}
