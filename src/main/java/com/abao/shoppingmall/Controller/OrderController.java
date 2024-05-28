package com.abao.shoppingmall.Controller;

import com.abao.shoppingmall.Dto.CreateOderRequest;
import com.abao.shoppingmall.Dto.OrderQueryParam;
import com.abao.shoppingmall.Model.OrderTotal;
import com.abao.shoppingmall.Service.OrderService;
import com.abao.shoppingmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        // 透過訂單id 取得訂單總結資訊
        OrderTotal orderTotal = OrderService.getOrderById(orderId);
        // 回傳訂單總結資訊
        return ResponseEntity.status(HttpStatus.CREATED).body(orderTotal);
    }

    // 查詢訂單列表
    @GetMapping("/users/{userId}/orders")
    // Page<OrderTotal> 代表一頁的訂單總結資訊
    public ResponseEntity<Page<OrderTotal>> getOrders(
            @PathVariable Integer userId,
            // 製作分頁
            // 一頁上限呈現 10 筆
            @RequestParam(defaultValue = "10") @Max(100) @Min(0) Integer limit,
            // 從第幾筆開始呈現
            @RequestParam(defaultValue = "0") @Min(0) Integer offset) {

        // 取得參數後，統一放進 OrderQuearyParam 物件
        OrderQueryParam orderQueryParam = new OrderQueryParam();
        orderQueryParam.setUserId(userId);
        orderQueryParam.setLimit(limit);
        orderQueryParam.setOffset(offset);
        
        // 取得 order 總數
        Integer count = OrderService.getOrdersCount(orderQueryParam);

        // 取得訂單內容，透過一個 List<OrderTotal> 物件回傳
        List<OrderTotal> orderTotalList = OrderService.getOrders(orderQueryParam);

        // 分頁
        Page<OrderTotal> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderTotalList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    // 登入後，刪除自己的一筆訂單
    // URL 有兩個參數，userId 與 orderId
    @DeleteMapping("/users/{userId}/orders/{orderId}")
    // ResponseEntity<Void> 代表成功刪除訂單，但不回傳任何資料
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer userId, @PathVariable Integer orderId) {
        // 刪除訂單
        OrderService.deleteOrder(userId, orderId);
        // 回傳成功訊息
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
