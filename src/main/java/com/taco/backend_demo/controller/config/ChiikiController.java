package com.taco.backend_demo.controller.config;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.config.ChiikiDTO;
import com.taco.backend_demo.entity.config.ChiikiEntity;
import com.taco.backend_demo.service.config.ChiikiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地域管理 Controller
 */
@Tag(name = "地域管理", description = "地域情報の CRUD 操作")
@RestController
@RequestMapping("/api/config/chiiki")
public class ChiikiController {

    @Autowired
    private ChiikiService chiikiService;

    @Operation(summary = "地域一覧取得", description = "すべての地域を取得します")
    @GetMapping
    public ResponseEntity<Response<List<ChiikiEntity>>> list() {
        List<ChiikiEntity> list = chiikiService.listAll();
        return ResponseFactory.success(list);
    }

    @Operation(summary = "地域詳細取得", description = "ID を指定して地域の詳細を取得します")
    @GetMapping("/{id}")
    public ResponseEntity<Response<ChiikiEntity>> get(@PathVariable Integer id) {
        ChiikiEntity entity = chiikiService.getById(id);
        return ResponseFactory.success(entity);
    }

    @Operation(summary = "地域作成", description = "新しい地域を作成します")
    @PostMapping
    public ResponseEntity<Response<Void>> create(@Valid @RequestBody ChiikiDTO dto) {
        ChiikiEntity entity = new ChiikiEntity();
        BeanUtils.copyProperties(dto, entity);
        chiikiService.create(entity);
        return ResponseFactory.success(null);
    }

    @Operation(summary = "地域更新", description = "地域情報を更新します")
    @PutMapping("/{id}")
    public ResponseEntity<Response<Void>> update(
            @PathVariable Integer id,
            @Valid @RequestBody ChiikiDTO dto) {
        ChiikiEntity entity = chiikiService.getById(id);
        BeanUtils.copyProperties(dto, entity);
        entity.setChiikiCd(id);
        chiikiService.update(entity);
        return ResponseFactory.success(null);
    }

    @Operation(summary = "地域削除", description = "地域を削除します")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Integer id) {
        chiikiService.delete(id);
        return ResponseFactory.success(null);
    }
}
