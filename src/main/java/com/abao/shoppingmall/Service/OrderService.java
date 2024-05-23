package com.abao.shoppingmall.Service;

import com.abao.shoppingmall.Dto.CreateOderRequest;

public interface OrderService {
    Integer createOrder(Integer userId, CreateOderRequest createOderRequest);
    // 返回訂單 id
}
