package ru.mail.park.model.User;

public class UserProfile {
    private final String login;
    private Integer score;
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

    public void setScore(Integer score) {
        this.score = score;
    }
}

