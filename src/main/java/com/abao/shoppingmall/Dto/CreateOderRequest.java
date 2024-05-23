package com.abao.shoppingmall.Dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

// 去對應前端的訂單，資料格式是JSON Obj
public class CreateOderRequest {

    // 前端的裡面還有另一個 JSON
    @NotEmpty // 裡面不可以是空的
    private List<BuyItem> buyItemList;

    public List<BuyItem> getBuyItemList() {
        return buyItemList;
    }

    public void setBuyItemList(List<BuyItem> buyItemList) {
        this.buyItemList = buyItemList;
    }
}
