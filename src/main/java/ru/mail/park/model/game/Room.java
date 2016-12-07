package ru.mail.park.model.game;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Room extends Deck{
    private final Set<Integer> users = new HashSet<>();
    private final Stack<Integer> place = new Stack<>();
    private final Stack<Integer> lose = new Stack<>();
    private boolean full = false;
    private int bank = 0;
    private final Stack<Integer> reward =new Stack<>();

    public int addUser(int user_id){
        if (full) return -1;
        users.add(user_id);
        full=(users.size()==5);
        return users.size();
    }

    public boolean checkFullRoom(){
        return full;
    }

    public Stack<Integer> getPlace() {
        return place;
    }

    public Set<Integer> getUsers() {
        return users;
    }

    public Stack<Integer> getLose() {
        return lose;
    }

    public int getBank() {
        return bank;
    }

    public Stack<Integer> getReward() {
        return reward;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }
}
