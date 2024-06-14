package com.abao.shoppingmall.Controller;

import com.abao.shoppingmall.Dto.ProductQueryParams;
import com.abao.shoppingmall.Dto.ProductRequest;
import com.abao.shoppingmall.Model.Product;
import com.abao.shoppingmall.Service.ProductService;
import com.abao.shoppingmall.constant.ProductCategory;
import com.abao.shoppingmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated //這樣才能啟用 Max 與 Min 這兩個驗證參數功能
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    // 透過 List<Product> 取得查詢的多個商品
    // 這邊還要加入查詢商品分類功能，透過 @RequestParam ProductCategory category 來取得分類名稱
    @GetMapping("/products") // 取的資料對應的是 Get 方法
    public ResponseEntity<Page<Product>> getProducts(
            // 查詢條件
            // category 透過商品分類查詢商品
            @RequestParam(required = false) ProductCategory category,
            // search 透過商品名稱查詢商品
            @RequestParam(required = false) String search,

            // 排序功能
            // orderBy 根據甚麼欄位來排序，預設為 created_date 最新的
            @RequestParam(defaultValue = "created_date") String orderBy,
            // sort 排序方式，升續或降序，這邊預設 desc 降序
            @RequestParam(defaultValue = "desc") String sort,

            // 分頁功能
            // limit 表示一次取得幾筆資料，對應SQL 的 LIMIT 參數，最大取得不可以超過 1000 筆，最小為 0不可以是負數
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
            // offset 表示跳過多少筆數據，對應SQL 的 OFFSET 參數，這邊預設從第一筆開始，OFFSET 最小為 0
            @RequestParam(defaultValue = "0") @Min(0) Integer offset) {

        /*組合查詢條件來搜尋資料，然後回傳資料存入 productList*/
        // 透過 productQuertParams 物件來存放查詢的參數，並且傳入 productService 的 getProducts 方法。
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        // List<Product> 所有商品的 List，參數為 category，表示要取得哪個分類的商品。
        List<Product> productsList = productService.getProducts(productQueryParams);

        /*以下用來存放查詢到的商品，並組成 Page 物件*/
        // 取得 product 筆數
        Integer total = productService.countProducts(productQueryParams);  // 取得總共有多少筆資料，後面參數為查詢時不同商品有不同筆數

        // 以下用來建立 Page 物件，並把查詢到的商品放進去。
        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productsList);  // 查詢到的資料放回去 Resutls 裡面

        // 不管有沒有商品，都回傳 HTTP 200 狀態碼與商品資料。
        return ResponseEntity.status(HttpStatus.OK).body(page);
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




























