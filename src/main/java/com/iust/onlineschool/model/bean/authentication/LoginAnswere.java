package com.iust.onlineschool.model.bean.authentication;

/**
 * Created by Mohsen on 12/8/2016.
 */
public class LoginAnswere {
    private     String      sessionId;
    private     String      role;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LoginAnswere(String role, String sessionId) {
        this.role = role;
        this.sessionId = sessionId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
