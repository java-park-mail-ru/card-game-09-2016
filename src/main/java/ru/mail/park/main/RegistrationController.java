package ru.mail.park.main;

//импорты появятся автоматически, если вы выбираете класс из выпадающего списка или же после alt+enter
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;
import ru.mail.park.services.SessionService;

import javax.servlet.http.HttpSession;
import java.util.Objects;

//Метка по которой спринг находит контроллер
@RestController
public class RegistrationController {
    private final AccountService accountService;
    private final SessionService sessionService;

    @Autowired
    public RegistrationController(AccountService accountService, SessionService sessionService) {
        this.accountService = accountService;
        this.sessionService = sessionService;
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody RegistrationRequest body, HttpSession httpSession) {
        final String sessionId = httpSession.getId();
        final String login = body.getLogin();
        final String password = body.getPassword();
        final String email = body.getEmail();
        boolean bodyNull = false;

        bodyNull = (StringUtils.isEmpty(login));
        bodyNull |= (StringUtils.isEmpty(email));
        bodyNull |= (StringUtils.isEmpty(password));

        if (bodyNull) {
            String messegeError = "Field filled not:";
            boolean flag = false;
            if (StringUtils.isEmpty(login)) {
                messegeError += "login";
                flag = true;
            }
            if ((StringUtils.isEmpty(email))) {
                if (flag) messegeError +=", ";
                messegeError += "email";
                flag = true;
            }
            if (StringUtils.isEmpty(password)) {
                if (flag) messegeError +=", ";
                messegeError += "password";
            }
            messegeError +='!';
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messegeError);
        }
        final UserProfile existingUser = accountService.getUser(login);
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Такой пользователь уже существует");
        }
        final String existingLogin = sessionService.getLogin(sessionId);
        if(existingLogin!=null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Вы уже авторизированы");
        }
        accountService.addUser(login, password, email);
        return ResponseEntity.ok(new SuccessResponse("User created successfully"));
    }

    @RequestMapping(path = "/hello",method = RequestMethod.GET)
    public ResponseEntity hello(HttpSession httpSession) {
        final String sessionId = httpSession.getId();
        final String login = sessionService.getLogin(sessionId);

        if (login!=null){
            return ResponseEntity.ok(new SuccessResponse("You are logged"));
        }
        return ResponseEntity.ok(new SuccessResponse("you are not authorized"));
    }

    @RequestMapping(path = "/exit", method = RequestMethod.GET)
    public ResponseEntity exit(HttpSession httpSession){
        final String sessionId = httpSession.getId();
        final String login = sessionService.removeLogin(sessionId);
        if (login != null){
            return ResponseEntity.ok(new SuccessResponse("You are no longer authorized"));
        }
        return ResponseEntity.ok(new SuccessResponse("You are not logged in"));
    }

    @RequestMapping(path = "/session", method = RequestMethod.POST)
    public ResponseEntity auth(@RequestBody AuthorisationRequest body, HttpSession httpSession){
        final String sessionId = httpSession.getId();

        final String login = body.getLogin();
        final String password = body.getPassword();

        if (StringUtils.isEmpty(login)||StringUtils.isEmpty(password)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
        }
        final UserProfile user = accountService.getUser(login);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
        }
        if (user.getPassword().equals(password)) {
            final String existingLogin = sessionService.getLogin(sessionId);
            if(Objects.equals(existingLogin, login)) { sessionService.removeLogin(sessionId); }
            sessionService.addSession(sessionId, login);
            return ResponseEntity.ok(new SuccessResponse("You have successfully signed"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
    }

    private static final class RegistrationRequest {
        private String login;
        private String password;
        private String email;

        private RegistrationRequest(String login, String password, String email) {
            this.login = login;
            this.password = password;
            this.email = email;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }
    }

    private static final class AuthorisationRequest{
        private String login;
        private String password;

        private AuthorisationRequest(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }

    private static final class SuccessResponse{
        private String response;

        private SuccessResponse(String responce){
            this.response = responce;
        }

        @SuppressWarnings("unused")
        public String getResponce(){
            return response;
        }
    }
}
