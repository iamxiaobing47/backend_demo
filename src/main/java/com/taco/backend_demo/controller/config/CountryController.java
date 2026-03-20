package com.taco.backend_demo.controller.config;

import com.taco.backend_demo.common.message.NotificationMessageCodes;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.entity.config.CountryEntity;
import com.taco.backend_demo.service.config.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 国家管理 Controller
 */
@Tag(name = "国家管理", description = "国情報の CRUD 操作")
@RestController
@RequestMapping("/api/config/country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @Operation(summary = "国家一覧取得", description = "すべての国家を取得します")
    @GetMapping
    public ResponseEntity<Response<List<CountryEntity>>> list() {
        List<CountryEntity> list = countryService.listAll();
        return ResponseFactory.success(list);
    }

    @Operation(summary = "国家詳細取得", description = "ID を指定して国家の詳細を取得します")
    @GetMapping("/{id}")
    public ResponseEntity<Response<CountryEntity>> get(@PathVariable Integer id) {
        CountryEntity entity = countryService.getById(id);
        return ResponseFactory.success(entity);
    }

    @Operation(summary = "地域別国家取得", description = "地域コードを指定して国家の一覧を取得します")
    @GetMapping("/region/{regionCd}")
    public ResponseEntity<Response<List<CountryEntity>>> listByRegion(@PathVariable Integer regionCd) {
        List<CountryEntity> list = countryService.listByRegion(regionCd);
        return ResponseFactory.success(list);
    }

    @Operation(summary = "国家作成", description = "新しい国家を作成します")
    @PostMapping
    public ResponseEntity<Response<Void>> create(@RequestBody CountryEntity entity) {
        countryService.create(entity);
        return ResponseFactory.success(null, NotificationMessageCodes.N001);
    }

    @Operation(summary = "国家更新", description = "国家情報を更新します")
    @PutMapping("/{id}")
    public ResponseEntity<Response<Void>> update(
            @PathVariable Integer id,
            @RequestBody CountryEntity entity) {
        entity.setCountryCd(id);
        countryService.update(entity);
        return ResponseFactory.success(null, NotificationMessageCodes.N002);
    }

    @Operation(summary = "国家削除", description = "国家を削除します")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Integer id) {
        countryService.delete(id);
        return ResponseFactory.success(null, NotificationMessageCodes.N003);
    }
}
