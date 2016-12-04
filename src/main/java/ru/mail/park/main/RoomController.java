package ru.mail.park.main;

import org.springframework.stereotype.Controller;
import ru.mail.park.model.game.Room;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Controller
public class RoomController {
    private final HashMap<Integer,Room> roomHashMap = new HashMap<>();
    private final Set<Integer> roomIdSet = new HashSet<>();

    public int newRoom(){
        int room_id=0;
        final int countRoom = roomIdSet.size();
        final Random random = new Random();
        while (roomIdSet.size()==countRoom){
            room_id=random.nextInt();
            roomIdSet.add(room_id);
        }
        roomHashMap.put(room_id,new Room(room_id));
        return room_id;
    }

    public boolean closeRoom(Integer room_id){
        if (!roomIdSet.remove(room_id))
            return false;
        roomHashMap.remove(room_id);
        return true;
    }
}
