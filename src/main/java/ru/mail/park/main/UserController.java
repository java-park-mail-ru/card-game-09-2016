package ru.mail.park.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.model.User.UserCreate;
import ru.mail.park.model.User.UserProfile;
import ru.mail.park.model.all.Result;
import ru.mail.park.services.AccountService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final AccountService accountService;

    @Autowired
    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.POST)
    public Result userAdd(@RequestBody UserCreate body) {
        UserProfile newUser = null;
        String login = body.getLogin();
        String email = body.getEmail();
        String password = body.getPassword();
        List<String> error = new ArrayList<>();
        if (login.isEmpty())
            error.add("login");
        if (password.isEmpty())
            error.add("password");
        if (email.isEmpty())
            error.add("email");
        if (error.size() > 0)
            return Result.incorrectRequest(error);
        try {
            newUser = accountService.addUser(login, password, email);
            if (newUser == null)
                return Result.userAlreadyExists(accountService.checkUser(login, email));
        } catch (Exception e) {
            Result.unkownError();
        }
        return Result.ok(newUser);
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.GET)
    public Result userAbout(@RequestParam(name = "id") Integer id){
        UserProfile user;
        if (!(id>0))
            return Result.incorrectRequest();
        try {
            user = accountService.getUser(id);
            if (user == null)
                return Result.notFound();
        }catch (Exception e){
            return Result.unkownError();
        }
        return Result.ok(user);
    }

    @RequestMapping(path =  "/api/top", method = RequestMethod.GET)
    public Result userTop(
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "since_id", required = false) Integer since_id
    ){
        limit = (limit == null)?0:limit;
        since_id = (since_id == null)?0:since_id;
        List<UserProfile> userProfileList;
        try {
            userProfileList = accountService.getTop(limit,since_id);
        }catch (Exception e){
            return Result.unkownError();
        }
        return Result.ok(userProfileList);
    }
}
