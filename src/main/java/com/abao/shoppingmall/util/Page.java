package com.abao.shoppingmall.util;

import java.util.List;

// 用來設計分頁，會設計成泛型
public class Page<T> {

    private Integer limit;    // 每頁顯示的筆數
    private Integer offset;
    private Integer total;    // 資料總筆數
    private List<T> results;  // result 為泛型，存放泛型資料

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
