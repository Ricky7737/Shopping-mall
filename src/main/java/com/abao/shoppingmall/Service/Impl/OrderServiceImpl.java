package com.abao.shoppingmall.Service.Impl;

import com.abao.shoppingmall.Dao.OrderDao;
import com.abao.shoppingmall.Dao.ProductDao;
import com.abao.shoppingmall.Dao.UserDao;
import com.abao.shoppingmall.Dto.BuyItem;
import com.abao.shoppingmall.Dto.CreateOderRequest;
import com.abao.shoppingmall.Dto.OrderQueryParam;
import com.abao.shoppingmall.Model.OrderItem;
import com.abao.shoppingmall.Model.OrderTotal;
import com.abao.shoppingmall.Model.Product;
import com.abao.shoppingmall.Model.User;
import com.abao.shoppingmall.Service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    // 定義 log 物件，用來記錄警告訊息
    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    // 透過 userId 與 createOderRequest 來創建訂單
    @Transactional // 訂單的功能，需要 transactional 來保證資料一致性，避免出現狀況無法復原數據
    @Override
    public Integer createOrder(Integer userId, CreateOderRequest createOderRequest) {

        // 要先檢查使用者是否存在
        User user = userDao.getUserById(userId);
        // 如果使用者不存在，就回傳 BAD_REQUEST 狀態
        if(user==null){
            // 使用 log 來記錄警告訊息
            log.warn("使用者{}不存在，無法下訂單", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        // 取得買了哪些商品
        List<OrderItem> orederItemList = new ArrayList<>();


        for(BuyItem buyItem : createOderRequest.getBuyItemList()){

            // 在開始訂單前，先檢查商品是否存在
            Product product = productDao.getProductById(buyItem.getProductId());
            // 使用 if-else if 來檢查商品是否存在，先檢查商品是否存在，如果存在就檢查庫存數量是否小於購買數量
            if(product == null){
                // 使用 log 來記錄警告訊息
                log.warn("商品{}不存在，無法下訂單", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if(product.getStock() < buyItem.getQuantity()){
                // product.Stock() 取得商品庫存數量，buyItem.getQuantity() 取得使用者購買數量
                // 使用 log 來記錄警告訊息
                log.warn("商品{}庫存數量不足，無法下訂單", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 如果下單成功，要從庫存中去扣除商品數量，然後更新資料庫料
            // 參數表示(取得商品 id, 商品庫存扣除下單數量)
            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());


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

    // 透過訂單 id 取得訂單資訊
    @Override
    public OrderTotal getOrderById(Integer orderId) {
        // 取得 order_total 表的訂單資訊
        OrderTotal orderTotal = orderDao.getOrderById(orderId);
        // 取得 order_item 表的訂單商品資訊
        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);
        // 合併上面兩筆數據，所以要擴充一個 orderItemList 到 OrderTotal 物件中
        orderTotal.setOrderItemList(orderItemList);
        // 回傳 OrderTotal 物件，包含訂單資訊和訂單商品資訊
        return orderTotal;
    }

    // 透過 OrderQueryParam 裡面得參數取得使用者訂單總數量
    @Override
    public Integer getOrdersCount(OrderQueryParam orderQueryParam) {
        return orderDao.countOrders(orderQueryParam);
    }

    // 透過 OrderQueryParam 裡面得參數取得使用者訂單資訊
    @Override
    public List<OrderTotal> getOrders(OrderQueryParam orderQueryParam) {

        List<OrderTotal> orderTotalList = orderDao.getOrders(orderQueryParam);
        // 透過 for 迴圈，取得每一筆訂單的訂單商品資訊，並合併到 OrderTotal 物件中
        for(OrderTotal orderTotal : orderTotalList){
            List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderTotal.getOrderId());
            orderTotal.setOrderItemList(orderItemList);
        }

        return orderTotalList;
    }

    // 刪除訂單的部分，因為一次異動到 orderItem、orderTotal 與 product 三個 Table，所以要小心使用
    @Transactional
    @Override
    public void deleteOrder(Integer userId, Integer orderId) {
        // 先檢查使用者 與 訂單是否存在
        User user = userDao.getUserById(userId);
        if(user==null){
            // 使用 log 來記錄警告訊息
            log.warn("使用者{}不存在，無法刪除訂單", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 檢查訂單是否在這個使用者名下
        // 取得 order_total 表的訂單資訊
        // 不過正常來說，要登陸後才會有 userId，也只會看到自己的訂單，所以不會有誤刪別人資料的問題
        OrderTotal orderTotal = orderDao.getOrderById(orderId);
        // 如果訂單不存在，或者訂單不屬於這個使用者，就回傳 BAD_REQUEST 狀態
        if(orderTotal==null || orderTotal.getUserId() != userId){
            // 使用 log 來記錄警告訊息
            log.warn("使用者{}無權刪除訂單{}", userId, orderId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


        // 另外 product 下單時被扣除庫存，先在也要恢復庫存數量
        // 先透過 List 取得 order_item 對應商品的資訊
        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);
        // 再透過 for 迴圈，將商品庫存數量加回來
        for(OrderItem orderItem : orderItemList){
            // 取得商品 id
            Integer productId = orderItem.getProductId();
            // 取得商品庫存數量
            Product product = productDao.getProductById(productId);
            // 將庫存數量加回來， product.getStock() 取得商品庫存數量，orderItem.getQuantity() 取得使用者購買數量
            productDao.updateStock(productId, product.getStock() + orderItem.getQuantity());
        }

        // 把訂單數量添加回庫存後，刪除訂單
        orderDao.deleteOrder(orderId);
    }
}
