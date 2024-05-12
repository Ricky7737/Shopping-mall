package com.abao.shoppingmall.Model;

import java.util.Date;

public class Product {
    private  Integer productId;
    private  String productName;
    private  String category;
    private  String imageUrl;
    private Integer price;
    private Integer stock;
    private String description;
    private Date craetedDate;
    private Date lastModifiedDate;

    public Product() {}
    // 帶參數構造

    public Product(Integer productId, String productName, String category, String imageUrl, Integer price, Integer stock, String description, Date craetedDate, Date lastModifiedDate) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.craetedDate = craetedDate;
        this.lastModifiedDate = lastModifiedDate;
    }
    // getter and setter


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCraetedDate() {
        return craetedDate;
    }

    public void setCraetedDate(Date craetedDate) {
        this.craetedDate = craetedDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
