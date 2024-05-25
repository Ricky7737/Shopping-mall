package com.abao.shoppingmall.Controller;

import com.abao.shoppingmall.Dto.CreateOderRequest;
import com.abao.shoppingmall.Model.OrderTotal;
import com.abao.shoppingmall.Service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private OrderService OrderService;

    // 表示登入後，在用戶中才能去下單
    @PostMapping("/uesrs/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOderRequest createOderRequest) {
        // orederId 就是資料庫自動創建的 id
        Integer orderId = OrderService.createOrder(userId, createOderRequest);

        // 回傳訂單
        OrderTotal orderTotal = OrderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderTotal);
    }
}
