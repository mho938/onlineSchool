package com.iust.common.security;

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


    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        System.out.println(session.getId());
        sessions.put(session.getId(), session);
    }


    public void sessionDestroyed(HttpSessionEvent event) {
        sessions.remove(event.getSession().getId());
    }

    public static HttpSession find(String sessionId) {
        return sessions.get(sessionId);
    }

}