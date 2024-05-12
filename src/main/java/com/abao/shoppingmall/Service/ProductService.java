package com.abao.shoppingmall.Service;

import com.abao.shoppingmall.Model.Product;

public interface ProductService {
    // 透過 productId 取得商品資訊
    Product getProductById(Integer productId);
}
