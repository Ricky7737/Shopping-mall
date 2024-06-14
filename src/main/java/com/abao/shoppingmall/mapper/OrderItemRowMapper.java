package com.abao.shoppingmall.mapper;

import com.abao.shoppingmall.Model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

// 讀取 order_item 表的資料，並將其轉換成 OrderItem 物件
// 另外 因為有 JOIN product 表的 product_name 和 image_url 變數，所以在 OrderItem 物件擴充了這兩個變數
public class OrderItemRowMapper implements RowMapper<OrderItem> {


    @Override
    public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(rs.getInt("order_item_id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setProductId(rs.getInt("product_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setAmount(rs.getInt("amount"));

        // 在 orderItem 擴充變數
        orderItem.setProductName(rs.getString("product_name"));
        orderItem.setImageUrl(rs.getString("image_url"));

        return orderItem;
    }
}
