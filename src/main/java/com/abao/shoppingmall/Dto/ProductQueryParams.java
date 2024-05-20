package com.abao.shoppingmall.Dto;

import com.abao.shoppingmall.constant.ProductCategory;

/*
    用來統一管理查詢商品的參數
    如果查詢條件有很多需要一一從 Controller 傳遞到 Service 再到 Dao，就可以方便管理變數
    可以省略修改 Service ，Dao 只要對應的條件微調就好
    這樣就可以提升程式的可讀性，提升維護性
*/

public class ProductQueryParams {

    private ProductCategory category;
    private String search;
    // 排序用
    private String orderBy;
    private String sort;
    // 分頁用
    private Integer limit;
    private Integer offset;

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
