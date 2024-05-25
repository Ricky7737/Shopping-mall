package com.abao.shoppingmall.Service;

import com.abao.shoppingmall.Dto.CreateOderRequest;
import com.abao.shoppingmall.Model.OrderTotal;

public interface OrderService {
    // 返回訂單 id
    Integer createOrder(Integer userId, CreateOderRequest createOderRequest);
    // 透過訂單 id 返回訂單資訊
    OrderTotal getOrderById(Integer orderId);

}
