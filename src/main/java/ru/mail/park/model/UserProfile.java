package ru.mail.park.model;

import java.util.concurrent.atomic.AtomicLong;

public class UserProfile {
    private String login;
    private String email;
    private String password;
    private Integer id;

    public UserProfile(Integer id, String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.id = id
    }
    
    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

