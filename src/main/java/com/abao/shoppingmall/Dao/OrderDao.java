package com.abao.shoppingmall.Dao;

import com.abao.shoppingmall.Dto.OrderQueryParam;
import com.abao.shoppingmall.Model.OrderItem;
import com.abao.shoppingmall.Model.OrderTotal;

import java.util.List;

public interface OrderDao {
    Integer createOrder(Integer userId, int totalAmount);
    // 插入數據到 order_item 表中
    void creatOrderItems(Integer orderId, List<OrderItem> orederItemList);
    // 取得 Order_Total 表中某個 order 的資料
    OrderTotal getOrderById(Integer orderId);
    // 取得 Order_Item 表中某個 order 的所有商品資料
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    // 透過 OrderQueryParam 物件查詢 order 筆數
    Integer countOrders(OrderQueryParam orderQueryParam);
    // 透過 OrderQueryParam 物件查詢 order 資料
    List<OrderTotal> getOrders(OrderQueryParam orderQueryParam);

    // 刪除 order 資料
    void deleteOrder(Integer orderId);
}
