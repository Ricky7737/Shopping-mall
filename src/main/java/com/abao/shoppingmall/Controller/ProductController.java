package com.abao.shoppingmall.Controller;

import com.abao.shoppingmall.Model.Product;
import com.abao.shoppingmall.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    // 查詢商品列表，會返回一個 List，存放商品數據，會還傳所有商品的資料，對應的是 Get 方法
    //@GetMapping("/products")
    //public ResponseEntity<List<Product>> getProducts() {}


    // 這邊是查詢單個商品
    @GetMapping("/products/{productId}")
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
}
