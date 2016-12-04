package ru.mail.park.model.game;

import ru.mail.park.model.User.UserProfile;

import java.util.HashSet;
import java.util.Set;

public class UserGame extends UserProfile {
    final Set cardSet = new HashSet();
    public UserGame(Integer id, String login, Integer score) {
        super(id, login, score);
    }
}
