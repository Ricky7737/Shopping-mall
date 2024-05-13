package com.abao.shoppingmall.Dao;

import com.abao.shoppingmall.Dto.ProductRequest;
import com.abao.shoppingmall.Model.Product;

public interface ProductDao {
    // 方反返回類型是 product
    // 這裡的 getProductById 是查詢 product 表格的 id 值，並回傳 product 物件
    Product getProductById(Integer productId);
    // 定義一個 creatProduct
    Integer createProduct(ProductRequest productRequest);

}
