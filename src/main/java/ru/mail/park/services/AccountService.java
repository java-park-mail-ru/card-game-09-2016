package ru.mail.park.services;


import org.springframework.stereotype.Service;
import ru.mail.park.model.UserProfile;

import java.util.concurrent.ConcurrentHashMap;


@Service
public class AccountService {
    private ConcurrentHashMap<String, UserProfile> userNameToUser = new ConcurrentHashMap<>();

    public UserProfile addUser(String login, String password, String email) {
        final UserProfile userProfile = new UserProfile(login, email, password);
        userNameToUser.put(login, userProfile);
        return userProfile;
    }

    public UserProfile getUser(String login) {
        return userNameToUser.get(login);
    }



    public void removeUser(String login) {
        if(userNameToUser.containsKey(login)) {
            userNameToUser.remove(login);
        }
    }

    public void changeUser(String oldLogin, String newLogin, String password, String email) {
        if (userNameToUser.containsKey(oldLogin)) {
            UserProfile temp = getUser(oldLogin);
            temp.setEmail(email);
            temp.setLogin(newLogin);
            temp.setPassword(password);
        }
    }


}
