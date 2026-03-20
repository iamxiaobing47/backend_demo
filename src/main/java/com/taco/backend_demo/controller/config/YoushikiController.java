package com.taco.backend_demo.controller.config;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.config.YoushikiDTO;
import com.taco.backend_demo.entity.config.YoushikiEntity;
import com.taco.backend_demo.service.config.YoushikiService;
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
@Tag(name = "申请书模板管理", description = "様式情報の CRUD 操作")
@RestController
@RequestMapping("/api/config/youshiki")
public class YoushikiController {

    @Autowired
    private YoushikiService youshikiService;

    @Operation(summary = "模板一覧取得", description = "すべてのテンプレートを取得します")
    @GetMapping
    public ResponseEntity<Response<List<YoushikiEntity>>> list() {
        List<YoushikiEntity> list = youshikiService.listAll();
        return ResponseFactory.success(list);
    }

    @Operation(summary = "模板詳細取得", description = "ID を指定してテンプレートの詳細を取得します")
    @GetMapping("/{id}")
    public ResponseEntity<Response<YoushikiEntity>> get(@PathVariable Integer id) {
        YoushikiEntity entity = youshikiService.getById(id);
        return ResponseFactory.success(entity);
    }

    @Operation(summary = "国別模板取得", description = "国コードを指定してテンプレートの一覧を取得します")
    @GetMapping("/kuni/{kuniCd}")
    public ResponseEntity<Response<List<YoushikiEntity>>> listByKuni(@PathVariable Integer kuniCd) {
        List<YoushikiEntity> list = youshikiService.listByKuni(kuniCd);
        return ResponseFactory.success(list);
    }

    @Operation(summary = "品目別模板取得", description = "品目コードを指定してテンプレートの一覧を取得します")
    @GetMapping("/hinmoku/{hinmokuCd}")
    public ResponseEntity<Response<List<YoushikiEntity>>> listByHinmoku(@PathVariable Integer hinmokuCd) {
        List<YoushikiEntity> list = youshikiService.listByHinmoku(hinmokuCd);
        return ResponseFactory.success(list);
    }

    @Operation(summary = "模板作成", description = "新しいテンプレートを作成します")
    @PostMapping
    public ResponseEntity<Response<Void>> create(@Valid @RequestBody YoushikiDTO dto) {
        YoushikiEntity entity = new YoushikiEntity();
        BeanUtils.copyProperties(dto, entity);
        youshikiService.create(entity);
        return ResponseFactory.success(null);
    }

    @Operation(summary = "模板更新", description = "テンプレート情報を更新します")
    @PutMapping("/{id}")
    public ResponseEntity<Response<Void>> update(
            @PathVariable Integer id,
            @Valid @RequestBody YoushikiDTO dto) {
        YoushikiEntity entity = youshikiService.getById(id);
        BeanUtils.copyProperties(dto, entity);
        entity.setYoushikiId(id);
        youshikiService.update(entity);
        return ResponseFactory.success(null);
    }

    @Operation(summary = "模板削除", description = "テンプレートを削除します")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Integer id) {
        youshikiService.delete(id);
        return ResponseFactory.success(null);
    }
}
