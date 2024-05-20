package com.abao.shoppingmall.Service.Impl;

import com.abao.shoppingmall.Dao.ProductDao;
import com.abao.shoppingmall.Dto.ProductRequest;
import com.abao.shoppingmall.Model.Product;
import com.abao.shoppingmall.Service.ProductService;
import com.abao.shoppingmall.constant.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    // 注入ProductDao
    private ProductDao productDao;

    @Override
    public List<Product> getProducts(ProductCategory category) {
        return productDao.getProducts(category);
    }

    @Override
    public Product getProductById(Integer productId) {
        // 回傳 ProductDao 物件的 getProductById 方法
        // 取得商品資訊
        return productDao.getProductById(productId);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        return productDao.createProduct(productRequest);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId, productRequest);
    }

    @Override
    public void deleteProductById(Integer productId) {
        productDao.deleteProductById(productId);
    }
}
