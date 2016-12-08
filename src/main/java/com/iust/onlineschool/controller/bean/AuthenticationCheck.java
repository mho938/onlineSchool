package com.iust.onlineschool.controller.bean;

import com.iust.onlineschool.enumaration.RoleType;
import com.iust.onlineschool.model.bean.authentication.Authentication;
import com.iust.onlineschool.model.bean.authentication.AuthenticationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;

/**
 * Created by Mohsen on 12/8/2016.
 */
public class AuthenticationCheck {
    @Autowired
    AuthenticationDAO authentications;
    public RoleType isAuthenticated (String sessionId){
        Authentication authentication =null;
        if (sessionId!=null) {
            try {
                authentication = authentications.findBySession(sessionId);
            } catch (Exception e) {
                System.out.println(e);
            }

            if (authentication != null)
                return authentication.getMembership().getRole();
            else return null;
        }
        else return null;

    }
}
