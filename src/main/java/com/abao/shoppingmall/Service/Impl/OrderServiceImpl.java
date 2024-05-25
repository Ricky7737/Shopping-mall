package com.abao.shoppingmall.Service.Impl;

import com.abao.shoppingmall.Dao.OrderDao;
import com.abao.shoppingmall.Dao.ProductDao;
import com.abao.shoppingmall.Dto.BuyItem;
import com.abao.shoppingmall.Dto.CreateOderRequest;
import com.abao.shoppingmall.Model.OrderItem;
import com.abao.shoppingmall.Model.OrderTotal;
import com.abao.shoppingmall.Model.Product;
import com.abao.shoppingmall.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Transactional // 訂單的功能，需要 transactional 來保證資料一致性，避免出現狀況無法復原數據
    @Override
    public Integer createOrder(Integer userId, CreateOderRequest createOderRequest) {

        int totalAmount = 0;
        // 取得買了哪些商品
        List<OrderItem> orederItemList = new ArrayList<>();


        for(BuyItem buyItem : createOderRequest.getBuyItemList()){
            // 透過商品id，去查詢商品資訊
            Product product = productDao.getProductById(buyItem.getProductId());
            // 計算總價錢，透過取得使用者購買數量 * 商品價格
            int amount = buyItem.getQuantity() * product.getPrice();
            // 將總價錢加到總計中
            totalAmount = totalAmount + amount;

            // 轉換 BuyItem 物件為 OrderItem 物件
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            // 將 OrderItem 物件加入 orederItemList，當成參數傳入到 orderDao.createOrderItem()
            orederItemList.add(orderItem);
        }


        // 創建訂單，因為訂單功能是兩個 Table 組成
        // @Transactional 可以讓兩張 Table 同時更新，但要注意，若其中一張 Table 失敗，則整個 transaction 失敗，所以要小心使用
        Integer orderId = orderDao.createOrder(userId, totalAmount); // 在 order_Total 去創建這筆訂單，然後傳入訂單總金額
        orderDao.creatOrderItems(orderId, orederItemList); // 在 order_item 去建立訂單下了那些商品

        return orderId;
    }

    @Override
    public OrderTotal getOrderById(Integer orderId) {
        // 取得 order_total 表的訂單資訊
        OrderTotal orderTotal = orderDao.getOrderById(orderId);
        // 取得 order_item 表的訂單商品資訊
        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        // 合併上面兩筆數據
        orderTotal.setOrderItemList(orderItemList);

        return orderTotal;
    }
}
