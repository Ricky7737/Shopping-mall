package com.abao.shoppingmall.Dao;

import com.abao.shoppingmall.Dto.ProductQueryParams;
import com.abao.shoppingmall.Dto.ProductRequest;
import com.abao.shoppingmall.Model.Product;

import java.util.List;

public interface ProductDao {

    // 定義一個 countProducts，用來計算 product 表格的資料量
    Integer countProducts(ProductQueryParams productQueryParams);
    // 返回類型是 List<Product>，表示全部的 product 物件
    List<Product> getProducts(ProductQueryParams productQueryParams);
    // 方反返回類型是 product
    // 這裡的 getProductById 是查詢 product 表格的 id 值，並回傳 product 物件
    Product getProductById(Integer productId);

    // 定義一個 creatProduct
    Integer createProduct(ProductRequest productRequest);

    // 定義一個 updateProduct，用來更新 product 表格的資料
    void updateProduct(Integer productId, ProductRequest productRequest);

    // 定義一個 deleteProductById，用來刪除 product 表格的資料
    void deleteProductById(Integer productId);

}
