package com.abao.shoppingmall.Service;

import com.abao.shoppingmall.Dto.ProductRequest;
import com.abao.shoppingmall.Model.Product;

public interface ProductService {
    // 透過 productId 取得商品資訊
    Product getProductById(Integer productId);
    // 定義 crawleProduct 方法，爬取商品資訊，ProductRequest 是前端傳進來的參數
    Integer createProduct(ProductRequest productRequest);
}
