package com.taco.backend_demo.controller.config;

import com.taco.backend_demo.common.message.NotificationMessageCodes;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.entity.config.ProductEntity;
import com.taco.backend_demo.service.config.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品目管理 Controller
 */
@Tag(name = "品目管理", description = "品目情報の CRUD 操作")
@RestController
@RequestMapping("/api/config/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "品目一覧取得", description = "すべての品目を取得します")
    @GetMapping
    public ResponseEntity<Response<List<ProductEntity>>> list() {
        List<ProductEntity> list = productService.listAll();
        return ResponseFactory.success(list);
    }

    @Operation(summary = "品目作成", description = "新しい品目を作成します")
    @PostMapping
    public ResponseEntity<Response<Void>> create(@RequestBody ProductEntity entity) {
        productService.create(entity);
        return ResponseFactory.success(null, NotificationMessageCodes.N001);
    }

    @Operation(summary = "品目更新", description = "品目情報を更新します")
    @PutMapping("/{id}")
    public ResponseEntity<Response<Void>> update(
            @PathVariable Integer id,
            @RequestBody ProductEntity entity) {
        entity.setProductCd(id);
        productService.update(entity);
        return ResponseFactory.success(null, NotificationMessageCodes.N002);
    }

    @Operation(summary = "品目削除", description = "品目を削除します")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseFactory.success(null, NotificationMessageCodes.N003);
    }
}
