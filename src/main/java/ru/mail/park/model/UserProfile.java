package ru.mail.park.model;

public class UserProfile {
    private final String login;
    private final Integer score;
    private final Integer id;

    public UserProfile(Integer id, String login, Integer score) {
        this.login = login;
        this.id = id;
        this.score = score;
    }

    public String getLogin() {
        return login;
    }

    public Integer getId() {
        return id;
    }

    public Integer getScore() {
        return score;
    }
}

