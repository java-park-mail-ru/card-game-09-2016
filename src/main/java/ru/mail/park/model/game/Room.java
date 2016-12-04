package ru.mail.park.model.game;

import java.util.HashMap;

public class Room {
    final int id;
    final HashMap<Integer,UserGame> users = new HashMap<>();
    final Deck deck = new Deck();

    public Room(int id) {
        this.id = id;
    }

    public int addUser(UserGame user){
        users.put(user.getId(),user);
        return users.size();
    }
}
