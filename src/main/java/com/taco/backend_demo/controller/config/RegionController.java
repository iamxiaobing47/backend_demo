package com.taco.backend_demo.controller.config;

import com.taco.backend_demo.common.message.NotificationMessageCodes;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.entity.config.RegionEntity;
import com.taco.backend_demo.service.config.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地域管理 Controller
 */
@Tag(name = "地域管理", description = "地域情報の CRUD 操作")
@RestController
@RequestMapping("/api/config/region")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Operation(summary = "地域一覧取得", description = "すべての地域を取得します")
    @GetMapping
    public ResponseEntity<Response<List<RegionEntity>>> list() {
        List<RegionEntity> list = regionService.listAll();
        return ResponseFactory.success(list);
    }

    @Operation(summary = "地域作成", description = "新しい地域を作成します")
    @PostMapping
    public ResponseEntity<Response<Void>> create(@RequestBody RegionEntity entity) {
        regionService.create(entity);
        return ResponseFactory.success(null, NotificationMessageCodes.N001);
    }

    @Operation(summary = "地域更新", description = "地域情報を更新します")
    @PutMapping("/{id}")
    public ResponseEntity<Response<Void>> update(
            @PathVariable Integer id,
            @RequestBody RegionEntity entity) {
        entity.setRegionCd(id);
        regionService.update(entity);
        return ResponseFactory.success(null, NotificationMessageCodes.N002);
    }

    @Operation(summary = "地域削除", description = "地域を削除します")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Integer id) {
        regionService.delete(id);
        return ResponseFactory.success(null, NotificationMessageCodes.N003);
    }
}
