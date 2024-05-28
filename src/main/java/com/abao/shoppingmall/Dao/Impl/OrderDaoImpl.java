package com.abao.shoppingmall.Dao.Impl;

import com.abao.shoppingmall.Dao.OrderDao;
import com.abao.shoppingmall.Dto.OrderQueryParam;
import com.abao.shoppingmall.Model.OrderItem;
import com.abao.shoppingmall.Model.OrderTotal;
import com.abao.shoppingmall.mapper.OrderItemRowMapper;
import com.abao.shoppingmall.mapper.OrderTotalRowMapper;
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
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public Integer createOrder(Integer userId, int totalAmount) {

        String sql = "INSERT INTO order_Total (user_id, total_amount, created_date, last_modified_date)" +
                " VALUES (:user_id, :total_amount, :created_date, :last_modified_date)";

        //Map 填入參數
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id", userId);
        paramMap.put("total_amount", totalAmount);

        //取得建立訂單時間
        Date now = new Date();
        paramMap.put("created_date", now);
        paramMap.put("last_modified_date", now);

        //執行插入使用者訂單這裡時，自動生成 id 並回傳
        KeyHolder keyHolder = new GeneratedKeyHolder();

        //將資料填入SQL
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(paramMap), keyHolder);

        //取得 id
        int orderId = keyHolder.getKey().intValue();

        return orderId;
    }

    @Override
    public void creatOrderItems(Integer orderId, List<OrderItem> orederItemList) {

        String sql = "INSERT INTO order_item (order_id, product_id, quantity, amount)" +
                " VALUES (:order_id, :product_id, :quantity, :amount)";

        // MapSqlParameterSource 一次性置入多筆資料
        MapSqlParameterSource[] batchArgs = new MapSqlParameterSource[orederItemList.size()];

        // 一次性置入多筆資料
        for (int i = 0; i < orederItemList.size(); i++) {
            OrderItem orderItem = orederItemList.get(i);

            batchArgs[i] = new MapSqlParameterSource();
            batchArgs[i].addValue("order_id", orderId);
            batchArgs[i].addValue("product_id", orderItem.getProductId());
            batchArgs[i].addValue("quantity", orderItem.getQuantity());
            batchArgs[i].addValue("amount", orderItem.getAmount());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchArgs);
    }
    
    
    // 取得 Order_total 表中指定 id 的訂單資訊
    @Override
    public OrderTotal getOrderById(Integer orderId) {
        
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date" +
                " FROM order_Total WHERE order_id = :order_id";
        
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("order_id", orderId);

        // 把取得的數據轉成一個 List
        List<OrderTotal> orderTotalList = namedParameterJdbcTemplate.query(sql, paramMap, new OrderTotalRowMapper());
        
        // 判斷是否有查詢到資料
        if(orderTotalList.size()>0){
            return orderTotalList.get(0);
        } else {
            return null;
        }
    }

    // 取得 Order_item 表中指定訂單 id 的商品資訊
    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {

        // 下訂單後，取得除了每一筆訂單的資訊外，還要加入訂單對應的商品名稱、圖片資訊
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, p.product_name, p.image_url" +
                " FROM order_item as oi" +
                " LEFT JOIN product as p ON oi.product_id = p.product_id" +
                " WHERE oi.order_id = :order_id";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("order_id", orderId);

        // 把取得的數據轉成一個 List
        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, paramMap, new OrderItemRowMapper());

        // 回傳查詢到的商品資訊
        return orderItemList;
    }

    // 透過 OrderQueryParam 物件查詢 order 筆數
    @Override
    public Integer countOrders(OrderQueryParam orderQueryParam) {
        // 這個語法表示要用 SQL 語法來寫 count 函式
        String sql = "SELECT COUNT(*) FROM order_Total WHERE 1=1";

        Map<String, Object> paramMap = new HashMap<>();

        // 加入查詢條件
        sql = addFliteringSql(sql, paramMap, orderQueryParam);

        // 執行查詢
        Integer total = namedParameterJdbcTemplate.queryForObject(sql, paramMap, Integer.class);

        return total;
    }

    // 透過 OrderQueryParam 物件查詢 order 資料
    @Override
    public List<OrderTotal> getOrders(OrderQueryParam orderQueryParam) {
        // 查詢語法
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date" +
                " FROM order_Total WHERE 1=1";

        Map<String, Object> paramMap = new HashMap<>();

        // 查詢條件
        sql = addFliteringSql(sql, paramMap, orderQueryParam);

        // 排序
        sql = sql + " ORDER BY created_date";

        // 分頁
        sql = sql + " LIMIT :limit OFFSET :offset";

        // 透過 map 將 limit 與 offset 加入 sql 語法中
        paramMap.put("limit", orderQueryParam.getLimit());
        paramMap.put("offset", orderQueryParam.getOffset());

        // 執行查詢
        List<OrderTotal> orderTotalList = namedParameterJdbcTemplate.query(sql, paramMap, new OrderTotalRowMapper());

        // 回傳查詢到的訂單資訊
        return orderTotalList;
    }

    @Override
    public void deleteOrder(Integer orderId) {

        // 刪除訂單的 SQL 語法
        String sql = "DELETE order_total, order_item " +
                "FROM  order_item " +
                "JOIN order_total ON order_total.order_id = order_item.order_id " +
                "WHERE order_item.order_id = :order_id";

        // 執行刪除
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource("order_id", orderId));

    }


    // 從上面 getOders() 與 countOrders() 函式中，把共同的程式提煉出來
    private String addFliteringSql(String sql, Map<String, Object> paramMap, OrderQueryParam orderQueryParam) {
        // 先判斷 userId 是否為 null
        if (orderQueryParam.getUserId()!= null) {
            sql += " AND user_id = :user_id";
            paramMap.put("user_id", orderQueryParam.getUserId());
        }
        return sql;
    }
}
