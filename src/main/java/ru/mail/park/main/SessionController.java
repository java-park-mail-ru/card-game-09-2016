package ru.mail.park.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.park.model.User.UserAuto;
import ru.mail.park.model.User.UserProfile;
import ru.mail.park.model.all.Result;
import ru.mail.park.services.AccountService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SessionController {
    private final AccountService accountService;

    @Autowired
    public SessionController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.GET)
    public Result checkAuth(HttpSession httpSession) {
        UserProfile userProfile = null;

        if (httpSession.getAttribute("userId") == null){
            return Result.notFound();
        }

        try {
            userProfile = accountService.getUser((Integer) httpSession.getAttribute("userId"));
        }catch (Exception e) {
            Result.unkownError();
        }
        return Result.ok(userProfile);
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.POST)
    public Result auth(@RequestBody UserAuto body, HttpSession httpSession) {
        final String login = body.getLogin();
        final String password = body.getPassword();
        UserProfile userProfile = null;
        List<String> error = new ArrayList<>();
        if (login.isEmpty())
            error.add("login");
        if (password.isEmpty())
            error.add("password");
        if (error.size() > 0)
            return Result.incorrectRequest(error);

        try {
            final int userId = accountService.getId(login, password);
            if (userId == 0) {
                error = accountService.checkUser(login,null);
                if (error.size()==0) {
                    return Result.invalidReques();
                }
                return Result.notFound();
            }
            userProfile = accountService.getUser(userId);
            assert userProfile != null;
            httpSession.setAttribute("userId",userProfile.getId());
        } catch (Exception e) {
            Result.unkownError();
        }

        return Result.ok(userProfile);
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.DELETE)
    public Result exit(HttpSession httpSession){
        httpSession.setAttribute("userId",null);
        return Result.ok();
    }
}
