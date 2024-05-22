package com.abao.shoppingmall.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// 接住前端登入時傳進來的 Email 與 Password 資料
public class UserLoginRequest {

    @NotBlank
    @Email
    private String mail;
    @NotBlank
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
