package com.abao.shoppingmall.Dao.Impl;

import com.abao.shoppingmall.Dao.OrderDao;
import com.abao.shoppingmall.Model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
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
}
