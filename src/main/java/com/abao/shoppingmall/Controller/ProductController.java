package com.abao.shoppingmall.Controller;

import com.abao.shoppingmall.Dto.ProductRequest;
import com.abao.shoppingmall.Model.Product;
import com.abao.shoppingmall.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    // 查詢商品列表，會返回一個 List，存放商品數據，會還傳所有商品的資料，對應的是 Get 方法
    //@GetMapping("/products")
    //public ResponseEntity<List<Product>> getProducts() {}


    // 這邊是查詢單個商品
    @GetMapping("/products/{productId}") // 取的資料對應的是 Get 方法，並且使用PathVariable來取得商品的 id
    // 當前端請求後，就會回傳的對應商品 .json 格式的資料
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);

        // 這邊的 ResponseEntity 就是回傳 HTTP 狀態碼的工具類別，可以用來回傳 HTTP 狀態碼與回傳的資料。
        // 如果商品存在，就回傳 HTTP 200 狀態碼與商品資料，否則回傳 HTTP 404 狀態碼。
        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 新增上品的功能
    @PostMapping("/products") // 新增資料對應的是 POST 方法
    // @RequestBody 代表前端請求的資料是 JSON 格式，並且會把 JSON 格式的資料轉成 ProductRequest 類別的物件。
    // @Valid 要寫才會讓 @NotNull 生效
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        // productService 提供一個 createProduct 方法，可以用來新增商品資料。
        // craeteProduct 方法會回傳新增的商品的 id，並且把新增的商品資料存到資料庫。
        Integer productId = productService.createProduct(productRequest);
        // 取得商品數據
        Product product = productService.getProductById(productId);
        // 回傳 HTTP 201 狀態碼與新增的商品資料，把資料傳回去給前端
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
}