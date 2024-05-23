package com.abao.shoppingmall.Dto;

import jakarta.validation.constraints.NotNull;

// 對應前端 buyItemList 裡的 json
public class BuyItem {

    @NotNull
    private Integer productId;
    @NotNull
    private Integer quantity;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
