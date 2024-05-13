package com.abao.shoppingmall.Dao.Impl;

import com.abao.shoppingmall.Dao.ProductDao;
import com.abao.shoppingmall.Model.Product;
import com.abao.shoppingmall.mapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {

    // JDBC
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // 取得商品
    @Override
    public Product getProductById(Integer productId) {
        // 參數化查詢 SQL，防止 SQL 注入攻擊
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date " +
                "FROM product WHERE product_id = :productId";

        // 建立 Map 物件，用來存放參數
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        // 執行查詢，取得商品列表
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        // 如果商品列表不為空，取第一筆商品回傳
        if (productList.size() > 0) {
            return productList.get(0);
            // 如果商品列表為空，回傳 null
        } else {
            return null;
        }
    }
}
