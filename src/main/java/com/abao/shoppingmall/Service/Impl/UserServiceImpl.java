package com.abao.shoppingmall.Service.Impl;

import com.abao.shoppingmall.Dao.UserDao;
import com.abao.shoppingmall.Dto.UserLoginRequest;
import com.abao.shoppingmall.Dto.UserRegisterRequest;
import com.abao.shoppingmall.Model.User;
import com.abao.shoppingmall.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    // 建立 Logger 物件，用來輸出 log 訊息
    //
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;


    @Override
    // 返回註冊的 id
    public Integer register(UserRegisterRequest userRegisterRequest) {

        // 檢查 email 是否已經存在，若存在則回傳錯誤訊息
        User user = userDao.getUsersByEmail(userRegisterRequest.getEmail());

        if(user != null){
            log.warn("該 email {} 已經被註冊", userRegisterRequest.getEmail());
            // 若 email 已經存在，則回傳錯誤訊息
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用 MD 5 加密 password
        // .getBytes() 轉換為 byte 陣列，再使用 DigestUtils.md5DigestAsHex() 加密
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());

        // 將 password 加密後，再設定到 UserRegisterRequest 物件中
        userRegisterRequest.setPassword(hashedPassword);

        // 創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        // 檢查 email 與 password 是否正確
        User user = userDao.getUsersByEmail(userLoginRequest.getMail());


        // 透過 Email 檢查 User 是否存在
        if(user == null){
            log.warn("該 Email {} 不存在", userLoginRequest.getMail());
            // 如果沒有找到 email，回傳錯誤訊息，強制停止該次請求
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 若 email 存在，則檢查 password 是否正確
        // 將輸入的密碼加密後再比較
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
        // 將 password 加密後，再設定到 UserLoginRequest 物件中
        userLoginRequest.setPassword(hashedPassword);

        // 接著檢查 password 是否正確，如果密碼正確回傳 user，如果錯誤返回該密碼錯誤
        // 比對邏輯就是，取得密碼後，筆對加密過後的密碼
        if(user.getPassword().equals(hashedPassword)){
            return user;
        }else {
            log.warn("email {} 密碼錯誤", userLoginRequest.getPassword());
            // 密碼錯誤，回傳錯誤訊息，強制停止該次請求
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
