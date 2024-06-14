package com.abao.shoppingmall.Dao;

import com.abao.shoppingmall.Dto.UserRegisterRequest;
import com.abao.shoppingmall.Model.User;

public interface UserDao {

    Integer createUser(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    // 透過 email 查找使用者
    User getUsersByEmail(String email);
}
