package com.abao.shoppingmall.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// 用來接住前端傳進來的帳號密碼註冊資料
public class UserRegisterRequest {

    @NotBlank
    @Email // 驗證信箱格式
    private String mail;

    @NotBlank
    private String Password;

    public String getEmail() {
        return mail;
    }

    public void setEmail(String email) {
        mail = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
