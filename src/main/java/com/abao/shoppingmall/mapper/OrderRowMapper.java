package com.abao.shoppingmall.mapper;

import com.abao.shoppingmall.Model.OrderTotal;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper implements RowMapper<OrderTotal> {


    @Override
    public OrderTotal mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderTotal orderTotal = new OrderTotal();

        orderTotal.setOrderId(rs.getInt("order_id"));
        orderTotal.setUserId(rs.getInt("user_id"));
        orderTotal.setTotalAmount(rs.getInt("total_amount"));
        orderTotal.setCreatedDate(rs.getTimestamp("created_date"));
        orderTotal.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

        return orderTotal;
    }
}
