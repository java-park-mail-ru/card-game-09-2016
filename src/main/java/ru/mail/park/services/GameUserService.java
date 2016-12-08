package ru.mail.park.services;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.mail.park.model.game.UserGame;

import java.util.HashMap;
/*Честно я бы такое писал в бд чтобы если что можно было восстановить*/
@Service
public class GameUserService {
    private static final HashMap<Integer,UserGame> userMap = new HashMap<>();
    private static final HashMap<Integer,Integer> userFomRoom = new HashMap<>();
    
    public static boolean addUser(UserGame user, Integer room_id){
        userFomRoom.put(user.getId(),room_id);
        user.setScore(0);
        userMap.put(user.getId(),user);
        return true;
    }
    
    @Nullable
    public static UserGame getUser(int user_id){
        if (!userMap.containsKey(user_id))
            return null;
        return userMap.get(user_id);
    }
    
    public static Integer getRoom(int user_id){
        if (!userFomRoom.containsKey(user_id))
            return -1;
        return userFomRoom.get(user_id);
    }
    
    public static boolean delUser(int user_id) {
        return userFomRoom.remove(user_id) != null && userMap.remove(user_id) != null;
    }
}
