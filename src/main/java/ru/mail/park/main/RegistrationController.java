package ru.mail.park.main;

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

@RestController
public class RegistrationController {


    private final AccountService accountService;
    private final SessionService sessionService;


    @Autowired
    public RegistrationController(AccountService accountService, SessionService sessionService) {
        this.accountService = accountService;
        this.sessionService = sessionService;
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody RegistRequest body) {


        final String login = body.getLogin();
        final String password = body.getPassword();
        final  String email = body.getEmail();


        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }
        if (accountService.addUser(login, password, email) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }

        return ResponseEntity.ok(new SuccessResponse(login));
    }
    @RequestMapping(path = "/api/session", method = RequestMethod.GET)
    public ResponseEntity checkAuth(HttpSession sessionId) {
        if (sessionService.checkExists(sessionId.getId())) {
            return ResponseEntity.ok(new SuccessResponse(sessionService.returnUserId(sessionId.getId()).toString()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.DELETE)
    public ResponseEntity deleteSession(HttpSession sessionId) {
        if(sessionService.checkExists(sessionId.getId())) {
            sessionService.deleteSession(sessionId.getId());
            return ResponseEntity.ok().body("{}");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.POST)
    public ResponseEntity auth(@RequestBody AuthRequest body, HttpSession sessionId) {
        final String login = body.getLogin();
        final String password = body.getPassword();
        if(StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password) ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }

        final int userId = accountService.getId(login,password);
        if(userId!=0) {
            final UserProfile user = accountService.getUser(userId);
            sessionService.addSession(sessionId.getId(),user.getId());
            return ResponseEntity.ok(new SuccessResponse(user.getLogin()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.GET)
    public ResponseEntity getInfo(@RequestParam Integer userId, HttpSession sessionId) {
        final UserProfile user = accountService.getUser(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }
        if (sessionService.checkExists(sessionId.getId())) {
            return ResponseEntity.ok().body(user.getLogin());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }
    }


    @RequestMapping(path = "/api/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity changeInfo(@PathVariable("id") int id, HttpSession sessionId, @RequestBody RegistRequest body) {
        final String login = body.getLogin();
        final String email = body.getEmail();
        final String password = body.getPassword();

        //final String oldlogin = sessionService.returnUserId(sessionId.getId());
        if(!sessionService.checkExists(sessionId.getId())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }
        final UserProfile temp = accountService.getUser(sessionService.returnUserId(sessionId.getId()));
        if (temp==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }
        if (temp.getId() == id) {
            final String oldlogin = temp.getLogin();
            accountService.changeUser(oldlogin, login, password, email);
            //sessionService.changeSessionLogin(sessionId.getId(),oldlogin,login); -? что это
            return ResponseEntity.ok().body(body);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");
        }
    }

    @RequestMapping(path = "/api/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable("id") int id, HttpSession sessionId) {
        if (!sessionService.checkExists(sessionId.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }
        final UserProfile temp = accountService.getUser(sessionService.returnUserId(sessionId.getId()));
        if (temp==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }
        if (temp.getId() == id) {
            sessionService.deleteSession(sessionId.getId());
            accountService.removeUser(temp.getLogin());
            return ResponseEntity.ok().body("{}");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}"); }

    }


    private  static final class RegistRequest {
        private String login;
        private String email;
        private String password;

        @JsonCreator
        private RegistRequest(@JsonProperty("login") String login,
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
    }

    private static final class AuthRequest {
        private String login;
        private String password;
        @JsonCreator
        private  AuthRequest(@JsonProperty("login") String login,
                             @JsonProperty("password") String password){
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


    private static final class SuccessResponse {
        private String login;

        private SuccessResponse(String login) {
            this.login = login;
        }

        public String getLogin() {
            return login;
        }
    }

}
