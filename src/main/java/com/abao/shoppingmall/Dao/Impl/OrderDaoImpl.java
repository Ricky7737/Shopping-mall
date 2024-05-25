package com.abao.shoppingmall.Dao.Impl;

import com.abao.shoppingmall.Dao.OrderDao;
import com.abao.shoppingmall.Model.OrderItem;
import com.abao.shoppingmall.Model.OrderTotal;
import com.abao.shoppingmall.mapper.OrderItemRowMapper;
import com.abao.shoppingmall.mapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
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
        List<OrderTotal> orderTotalList = namedParameterJdbcTemplate.query(sql, paramMap, new OrderRowMapper());
        
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
}
