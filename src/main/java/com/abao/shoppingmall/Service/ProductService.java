package com.abao.shoppingmall.Service;

import com.abao.shoppingmall.Dto.ProductQueryParams;
import com.abao.shoppingmall.Dto.ProductRequest;
import com.abao.shoppingmall.Model.Product;

import java.util.List;

public interface ProductService {

    // 計算商品數量
    Integer countProducts(ProductQueryParams productQueryParams);
    // 定義 getProducts 方法，取得所有商品資訊
    List<Product> getProducts(ProductQueryParams productQueryParams);
    // 透過 productId 取得單個商品資訊
    Product getProductById(Integer productId);

    // 定義 crawleProduct 方法，爬取商品資訊，ProductRequest 是前端傳進來的參數
    Integer createProduct(ProductRequest productRequest);

    // 定義 updateProduct 方法，更新商品資訊，ProductRequest 是前端傳進來的參數
    void updateProduct(Integer productId, ProductRequest productRequest);

    // 定義 deleteProductById 方法，刪除商品資訊
    void deleteProductById(Integer productId);


}
