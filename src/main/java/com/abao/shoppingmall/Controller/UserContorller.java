package com.abao.shoppingmall.Controller;

import com.abao.shoppingmall.Dto.UserRegisterRequest;
import com.abao.shoppingmall.Model.User;
import com.abao.shoppingmall.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserContorller {

    @Autowired
    private UserService userService;

    // 實作註冊帳號API
    @PostMapping("/users/register")
    // 透過 UserRegisterRequest 接住前端資料並進行驗證
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        // 呼叫userService的register方法進行註冊，返回 UserId
        Integer userId = userService.register(userRegisterRequest);
        // 透過 getUserById 查詢 User 資訊
        User user = userService.getUserById(userId);
        // 回傳 User 資訊
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
