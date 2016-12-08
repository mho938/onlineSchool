package com.iust.onlineschool.model.bean.authentication;

/**
 * Created by Mohsen on 12/8/2016.
 */
public class LoginAnswere {
    private     String      token;
    private     String      role;

    public LoginAnswere(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
