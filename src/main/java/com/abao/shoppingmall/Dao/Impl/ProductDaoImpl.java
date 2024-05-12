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

        // 取得商品資訊
        String sql = "SELECT  product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date"
                + " FROM product WHERE product_id = :productId";

        // map
        Map<String, Object> map = new HashMap<String, Object>();
        // 放入參數
        map.put("productId", productId);
        // 透過 namedParameterJdbcTemplate 取得商品資訊
        List<Product> products = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        // 判斷是否有商品資訊
        if (products != null && products.size() > 0) {
            return products.get(0);
        }else {
            return null;
        }
    }
}
