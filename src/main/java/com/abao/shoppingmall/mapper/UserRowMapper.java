package com.abao.shoppingmall.mapper;

import com.abao.shoppingmall.Model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

// 透過將資料庫的 user 表格的欄位對應到 Java 的 User 類別，來實現資料庫查詢的結果轉換成 Java 類別的功能。
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        // 建立一個 User 類別的實體
        User user = new User();
        // 將資料庫查詢的結果對應到 User 類別的屬性上
        user.setUserId(resultSet.getInt("user_id"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setCreatedDate(resultSet.getTimestamp("created_date"));
        user.setLastModifiedDate(resultSet.getTimestamp("last_modified_date"));

        return user;
    }
}
