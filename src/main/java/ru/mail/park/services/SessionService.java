package ru.mail.park.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionService {
    private Map<String, String> sessionIdTologin = new HashMap<>();

    public String addSession(String sessionId, String login) {
        return sessionIdTologin.put(sessionId, login);
    }

    public String getLogin(String sessionId) {
        return sessionIdTologin.get(sessionId);
    }

    public String removeLogin(String sessionId) { return sessionIdTologin.remove(sessionId); }
}
