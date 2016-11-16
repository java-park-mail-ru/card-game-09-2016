package ru.mail.park.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionService {
    private Map<String, Integer> sessionToLogin = new HashMap<>();

    public void addSession(String sessionId, Integer userId) {
        sessionToLogin.put(sessionId, userId);
    }

    public void deleteSession(String sessionId) {
        if (checkExists(sessionId)) sessionToLogin.remove(sessionId);
    }

    public Integer returnUserId(String sessionId) {
        return sessionToLogin.get(sessionId);
    }

    public boolean checkExists(String sessionId) {
        return sessionToLogin.containsKey(sessionId);
    }


    public void changeSessionLogin(String sessionID, Integer oldUserId, Integer userId){
        sessionToLogin.replace(sessionID,oldUserId,userId); //-? не понял этого метода
    }

}
