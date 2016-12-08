package ru.mail.park.model.User;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class UserCreate {
    private String login;
    private String email;
    private String password;

    @JsonCreator
    public UserCreate(@JsonProperty("login") String login,
                      @JsonProperty("email") String email,
                      @JsonProperty("password") String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString(){
        return "{\"login\": \""+login+"\", \"email\": \""+email+"\", \"password\": \""+password+"\" }";
    }
}
