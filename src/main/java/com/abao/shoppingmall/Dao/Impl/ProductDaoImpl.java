package com.abao.shoppingmall.Dao.Impl;

import com.abao.shoppingmall.Dao.ProductDao;
import com.abao.shoppingmall.Dto.ProductQueryParams;
import com.abao.shoppingmall.Dto.ProductRequest;
import com.abao.shoppingmall.Model.Product;
import com.abao.shoppingmall.mapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Override
    // 取得所有商品
    // WHERE 1=1 表示不篩選任何條件，可以拼接多個條件
    public List<Product> getProducts(ProductQueryParams productQueryParams) {


        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date " +
                "FROM product WHERE 1=1";

        /*---執行查詢，取得商品列表---*/
        Map<String, Object> map = new HashMap<>();  // 建立空的Map物件
        // 如果 category 參數不是空，就加入查詢條件
        if (productQueryParams.getCategory() != null) {
            sql = sql + " AND category = :category"; // 這邊主要讓查詢條件拼接在 WHERE 後面
            // category 是 Enum 型態，透過 .name() 取得字串值
            map.put("category", productQueryParams.getCategory().name());
        }

        // 如果 search 參數不是空，就加入查詢條件
        if (productQueryParams.getSearch() != null) {
            sql = sql + " AND product_name LIKE :search";
            // 將 search 加上 % 作為模糊查詢
            map.put("search", "%" + productQueryParams.getSearch() + "%");
        }

        /*---排序條件---*/
        // 這邊透過字串拼接的方式，將 order_by 和 sort 參數拼接到 SQL 語法中
        // 這邊有在 ProductController 加入預設條件，所以不用在這邊判斷是否有排序條件
        sql = sql + " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

        /* ---分頁查詢---*/
        // 這邊透過 offset 和 limit 參數，將查詢結果限制在一個區間內
        sql = sql + " LIMIT :limit OFFSET :offset";
        map.put("limit", productQueryParams.getLimit());
        map.put("offset", productQueryParams.getOffset());

        // 根據 RowMapper 將查詢結果轉換成 Product 物件
        List<Product> products = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        return products;
    }

    // 取得單個商品
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

    // 更新商品
    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {

        // 參數化 SQL 語法
        String sql = "UPDATE product SET product_name = :productName, category = :category, " +
                "image_url = :imageUrl, price = :price, stock = :stock, description = :description, " +
                "last_modified_date = :lastModifiedDate WHERE product_id = :productId";

        // 建立 Map 物件，用來存放參數
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        // 更新所有欄位
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        // 更新時間，而且是最後時間
        Date now = new Date();
        map.put("lastModifiedDate", now);

        // 執行更新
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map));
    }

    @Override
    public void deleteProductById(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map));
    }
}
