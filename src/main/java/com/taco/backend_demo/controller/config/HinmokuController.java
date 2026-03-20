package com.taco.backend_demo.controller.config;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.config.HinmokuDTO;
import com.taco.backend_demo.entity.config.HinmokuEntity;
import com.taco.backend_demo.service.config.HinmokuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品目管理 Controller
 */
@Tag(name = "品目管理", description = "品目情報の CRUD 操作")
@RestController
@RequestMapping("/api/config/hinmoku")
public class HinmokuController {

    @Autowired
    private HinmokuService hinmokuService;

    @Operation(summary = "品目一覧取得", description = "すべての品目を取得します")
    @GetMapping
    public ResponseEntity<Response<List<HinmokuEntity>>> list() {
        List<HinmokuEntity> list = hinmokuService.listAll();
        return ResponseFactory.success(list);
    }

    @Operation(summary = "品目詳細取得", description = "ID を指定して品目の詳細を取得します")
    @GetMapping("/{id}")
    public ResponseEntity<Response<HinmokuEntity>> get(@PathVariable Integer id) {
        HinmokuEntity entity = hinmokuService.getById(id);
        return ResponseFactory.success(entity);
    }

    @Operation(summary = "品目作成", description = "新しい品目を作成します")
    @PostMapping
    public ResponseEntity<Response<Void>> create(@Valid @RequestBody HinmokuDTO dto) {
        HinmokuEntity entity = new HinmokuEntity();
        BeanUtils.copyProperties(dto, entity);
        hinmokuService.create(entity);
        return ResponseFactory.success(null);
    }

    @Operation(summary = "品目更新", description = "品目情報を更新します")
    @PutMapping("/{id}")
    public ResponseEntity<Response<Void>> update(
            @PathVariable Integer id,
            @Valid @RequestBody HinmokuDTO dto) {
        HinmokuEntity entity = hinmokuService.getById(id);
        BeanUtils.copyProperties(dto, entity);
        entity.setHinmokuCd(id);
        hinmokuService.update(entity);
        return ResponseFactory.success(null);
    }

    @Operation(summary = "品目削除", description = "品目を削除します")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Integer id) {
        hinmokuService.delete(id);
        return ResponseFactory.success(null);
    }
}
