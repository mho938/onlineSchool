package com.iust.onlineschool.model.bean.authentication;

import com.iust.onlineschool.model.bean.membership.Membership;

/**
 * Created by Mohsen on 12/8/2016.
 */
public class AuthenticationModel extends Membership {
    private         String          sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
