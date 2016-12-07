package ru.mail.park.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.model.User.UserProfile;
import ru.mail.park.model.other.Result;
import ru.mail.park.model.game.Room;
import ru.mail.park.model.game.UserGame;
import ru.mail.park.services.AccountService;
import ru.mail.park.services.GameUserService;
import ru.mail.park.services.RoomService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController extends MainController{

    @Autowired
    GameController (AccountService _accountService) {
        super(_accountService);
    }

    @RequestMapping(path = "/api/play" ,method = RequestMethod.POST)
    public static Result play(HttpSession httpSession){
        Result result = SessionController.checkAuth(httpSession);
        if (result.getCode()>0)
            return result;
        UserGame userGame = (UserGame) result.getResponse();
        int room_id = RoomService.getLastCreator();
        if (room_id!=-1)
            GameUserService.addUser(userGame,room_id);
        return Result.ok(room_id); //-1 вообще ошибка означает но хз как обрабатывать, по сути означает что достигнут лимит по комнатам
    }

    @RequestMapping(path = "/api/play", method = RequestMethod.DELETE)
    public static Result exit(HttpSession httpSession){
        Result result = SessionController.checkAuth(httpSession);
        if (result.getCode()>0)
            return result;
        int user_id = ((UserProfile) result.getResponse()).getId();
        Integer score = null;
        if (GameUserService.getUser(user_id) != null)
            score = GameUserService.getUser(user_id).getScore();
        if (score==null)
            return Result.notFound();
        int room_id = GameUserService.getRoom(user_id);
        if (room_id<0)
            return Result.notFound();
        Room room = RoomService.getRoom(room_id);
        /*
            Определяем игрок сдался или он уже выйграл и
            просто хочет покинуть комнату, не досматривая игру
        */
        if (!room.getPlace().contains(user_id)) {
            room.setBank(score);
            room.getLose().push(user_id);
        }else{
            room.getReward().push(score);
        }
        /*
            Хранение в памяти модели игроков которые играют
            При выходе из комнате будем убирать его
        */
        room.getUsers().remove(user_id);
        GameUserService.delUser(user_id);
        return Result.ok();
    }

    public static List<UserProfile> roomEnd(int room_id){
        Room room = RoomService.getRoom(room_id);
        List<UserProfile> top = new ArrayList<>();
        while (room.getLose().size()>0){
            room.getPlace().push(room.getLose().pop());
            room.getReward().push(0);
        }
        int place = 5;
        int user_id;
        int score = 0;
        while (room.getPlace().size()>0){
            user_id = room.getPlace().pop();
            score += room.getReward().pop();
            getAccountService().addScore(user_id,score);
            top.add(place,getAccountService().getUser(user_id));
            top.get(place).setScore(score);
            place--;
        }
        Integer [] users = (Integer[]) room.getUsers().toArray();
        for (Integer user : users) {
            GameUserService.delUser(user);
        }
        RoomService.closeRoom(room_id);
        return top;
    }


}
