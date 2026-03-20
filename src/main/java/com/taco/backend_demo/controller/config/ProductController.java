package com.taco.backend_demo.controller.config;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.config.ProductDTO;
import com.taco.backend_demo.entity.config.ProductEntity;
import com.taco.backend_demo.service.config.ProductService;
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

    @Operation(summary = "品目詳細取得", description = "ID を指定して品目の詳細を取得します")
    @GetMapping("/{id}")
    public ResponseEntity<Response<ProductEntity>> get(@PathVariable Integer id) {
        ProductEntity entity = productService.getById(id);
        return ResponseFactory.success(entity);
    }

    @Operation(summary = "品目作成", description = "新しい品目を作成します")
    @PostMapping
    public ResponseEntity<Response<Void>> create(@Valid @RequestBody ProductDTO dto) {
        ProductEntity entity = new ProductEntity();
        BeanUtils.copyProperties(dto, entity);
        productService.create(entity);
        return ResponseFactory.success(null);
    }

    @Operation(summary = "品目更新", description = "品目情報を更新します")
    @PutMapping("/{id}")
    public ResponseEntity<Response<Void>> update(
            @PathVariable Integer id,
            @Valid @RequestBody ProductDTO dto) {
        ProductEntity entity = productService.getById(id);
        BeanUtils.copyProperties(dto, entity);
        entity.setProductCd(id);
        productService.update(entity);
        return ResponseFactory.success(null);
    }

    @Operation(summary = "品目削除", description = "品目を削除します")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseFactory.success(null);
    }
}
