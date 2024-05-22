package com.abao.shoppingmall.Service;

import com.abao.shoppingmall.Dto.UserLoginRequest;
import com.abao.shoppingmall.Dto.UserRegisterRequest;
import com.abao.shoppingmall.Model.User;

public interface UserService {
    // 返回註冊成功的 userId
    Integer register(UserRegisterRequest userRegisterRequest);
    // 返回該用戶的資料，透過 userId 查找
    User getUserById(Integer userId);
    // 登入
    User login(UserLoginRequest userLoginRequest);
}
