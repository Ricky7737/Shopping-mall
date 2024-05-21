package com.abao.shoppingmall.Dto;

import jakarta.validation.constraints.NotBlank;

// 用來接住前端傳進來的帳號密碼註冊資料
public class UserRegisterRequest {

    @NotBlank
    private String Email;

    @NotBlank
    private String Password;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
