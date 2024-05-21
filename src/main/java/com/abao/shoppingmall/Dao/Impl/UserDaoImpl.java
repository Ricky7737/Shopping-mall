package com.abao.shoppingmall.Dao.Impl;

import com.abao.shoppingmall.Dao.UserDao;
import com.abao.shoppingmall.Dto.UserRegisterRequest;
import com.abao.shoppingmall.Model.User;
import com.abao.shoppingmall.mapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String sql = "INSERT INTO user(email, password, created_date, last_modified_date) " +
                "VALUES (:email, :password, :createdDate, :lastModifiedDate)";

        // 建立一個 map 來存放使用者資料
        Map<String, Object> map = new HashMap<>();
        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());

        // 建立一個 now 來存放現在時間
        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        // 建立一個 keyHolder 來存放產生的 id
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // 執行 SQL 並取得產生的 id
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        // 取得產生的 id
        int userId = keyHolder.getKey().intValue();

        return userId;
    }

    @Override
    public User getUserById(Integer userId) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date " +
                "FROM user WHERE user_id = :userId";

        // 建立一個 map 來存放 userId
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        // 透過 UserRowMapper 將資料轉換成 User 物件，存放到 List 中
        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        // 取出第一筆資料
        if (userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }
}
