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

    // 這邊是查詢所有商品，並且回傳 List<Product>
    // 而且他返回是要一個 List
    // getProducts 表示回傳多個商品
    @GetMapping("/products") // 取的資料對應的是 Get 方法
    public ResponseEntity<List<Product>> getProducts() {
        // List<Product> 所有商品的 List
        List<Product> products = productService.getProducts();
        // 不管有沒有商品，都回傳 HTTP 200 狀態碼與商品資料。
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

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

    // 修改商品功能
    @PutMapping("/products/{productId}") // 修改資料對應的是 PUT 方法
    // 參數(接住URL 的 productId 數值, 商品修改後的資料 ProductRequest)
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest) {
        // 檢查商品是否存在
        Product product = productService.getProductById(productId);
        // 如果是空的，表示商品不存在，回傳 404 Not Found 狀態
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 修改商品功能，參數為 productId 與 productRequest，表示指定商品然後修改商品資料。
        productService.updateProduct(productId, productRequest);
        // 查詢更新後的商品資料
        Product updatedProduct = productService.getProductById(productId);
        // 回傳 200 OK，在 body 回傳更新後的商品資料。
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    // 刪除商品功能，指定 id
    @DeleteMapping("/products/{productId}") // 刪除資料對應的是 DELETE 方法
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productId) {
        // 對於前端來說只在意有沒有把商品刪除，所以不用在加入判斷商品是否存在的程式碼。
        // 刪除商品功能，參數為 productId，表示指定商品然後刪除商品資料。
        productService.deleteProductById(productId);
        // 回傳 204 No Content 表示刪除成功，但不回傳任何資料。
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}




























