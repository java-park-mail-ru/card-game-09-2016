package ru.mail.park.services;

import org.springframework.stereotype.Service;
import ru.mail.park.model.game.Room;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class RoomService {
    private static final HashMap<Integer,Room> roomHashMap = new HashMap<>();
    private static final Set<Integer> roomIdSet = new HashSet<>();
    final private static int maxRoom = 25;
    private static int lastID;
    
    private static int RoomCreator(){
        int room_id=0;
        if (roomIdSet.size()==maxRoom)
            return -1;
        final int countRoom = roomIdSet.size();
        final Random random = new Random();
        while (roomIdSet.size()==countRoom){
            room_id=random.nextInt();
            roomIdSet.add(room_id);
        }
        roomHashMap.put(room_id,new Room());
        lastID = room_id;
        return room_id;
    }

    public static boolean closeRoom(Integer room_id){
        if (!roomIdSet.remove(room_id))
            return false;
        roomHashMap.remove(room_id);
        return true;
    }

    public static int getLastCreator(){
        if (roomHashMap.get(lastID).checkFullRoom()&& RoomCreator()==-1){
                return -1;
        }
        return lastID;
    }
    
    public static Room getRoom(int room_id){
        return roomHashMap.get(room_id);
    }

}
