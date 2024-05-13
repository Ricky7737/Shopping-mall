package com.abao.shoppingmall.Dao.Impl;

import com.abao.shoppingmall.Dao.ProductDao;
import com.abao.shoppingmall.Dto.ProductRequest;
import com.abao.shoppingmall.Model.Product;
import com.abao.shoppingmall.mapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
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

    // 新增商品到資料庫
    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product(product_name, category, image_url, price, stock, " +
                "description, created_date, last_modified_date) " +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, :description, " +
                ":createdDate, :lastModifiedDate)";

        // 建立 Map 物件，用來存放參數
        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        // 添加時間
        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        // 使用 KeyHolder 自動生成產生的產品 ID，存入資料庫
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // JDBC 存入資料庫
        // MapSqlParameterSource 用來將 Map 物件轉換成 SQL 參數
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int productId = keyHolder.getKey().intValue();
        return productId;
    }
}
