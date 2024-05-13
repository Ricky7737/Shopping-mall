package com.abao.shoppingmall.mapper;

import com.abao.shoppingmall.Model.Product;
import com.abao.shoppingmall.constant.ProductCategory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

// RowMapper 要用 JDBCTemplate
// 這個 RowMapper 就是用來把查詢到的資料轉成 Product 物件
public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {
        // 時做裡面要做的事情
        // ResultSet 就是查詢結果集
        // rowNum 是第幾筆資料
        Product product = new Product();

        product.setProductId(resultSet.getInt("product_id"));
        product.setProductName(resultSet.getString("product_name"));

        // 這邊因為 Product 物件裡面有 category 屬性，所以要把查詢到的 category 資料存進去
        // String 轉換成 Enum 類型，透過 Enum 的 valueOf() 方法
        // 然後接住資料庫的資料，存進 Product 物件裡面
        product.setCategory(ProductCategory.valueOf(resultSet.getString("category")));

        product.setImageUrl(resultSet.getString("image_url"));
        product.setPrice(resultSet.getInt("price"));
        product.setStock(resultSet.getInt("stock"));
        product.setDescription(resultSet.getString("description"));
        product.setCreatedDate(resultSet.getTimestamp("created_date"));
        product.setLastModifiedDate(resultSet.getTimestamp("last_modified_date"));

        return product;
    }
}
