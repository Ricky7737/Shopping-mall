package com.abao.shoppingmall.Service.Impl;

import com.abao.shoppingmall.Dao.UserDao;
import com.abao.shoppingmall.Dto.UserRegisterRequest;
import com.abao.shoppingmall.Model.User;
import com.abao.shoppingmall.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    // 返回註冊的 id
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }


}
