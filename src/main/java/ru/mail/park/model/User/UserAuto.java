package ru.mail.park.model.User;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAuto {
    private String login;
    private String password;

    @JsonCreator
    public UserAuto(@JsonProperty("login") String login,
                      @JsonProperty("password") String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString(){
        return "{\"login\": \""+login+"\", \"password\": \""+password+"\" }";
    }
}
