package com.abao.shoppingmall.Dao;

import com.abao.shoppingmall.Model.Product;

public interface ProductDao {
    // 方反返回類型是 product

    Product getProductById(Integer productId);
}
