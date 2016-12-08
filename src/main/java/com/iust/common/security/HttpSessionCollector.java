package com.iust.common.security;

import com.iust.onlineschool.model.bean.authentication.Authentication;
import com.iust.onlineschool.model.bean.authentication.AuthenticationDAO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohsen on 12/7/2016.
 */
public class HttpSessionCollector implements HttpSessionListener {
    private static final Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();

    private AuthenticationDAO authentications;
    @Autowired

    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        System.out.println(session.getId());
        sessions.put(session.getId(), session);
    }


    public void sessionDestroyed(HttpSessionEvent event) {
        if (event.getSession().getId()!="" && event.getSession().getId()!=null) {
            Authentication authentication = authentications.findBySession(event.getSession().getId());
            authentications.delete(authentication);
        }
        sessions.remove(event.getSession().getId());
    }

    public static HttpSession find(String sessionId) {
        return sessions.get(sessionId);
    }

}