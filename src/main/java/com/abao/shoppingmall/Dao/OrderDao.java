package com.abao.shoppingmall.Dao;

import com.abao.shoppingmall.Model.OrderItem;

import java.util.List;

public interface OrderDao {
    Integer createOrder(Integer userId, int totalAmount);
    // 插入數據到 order_item 表中
    void creatOrderItems(Integer orderId, List<OrderItem> orederItemList);
}
