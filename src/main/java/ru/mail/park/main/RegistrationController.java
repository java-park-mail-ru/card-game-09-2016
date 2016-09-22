package ru.mail.park.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;

@RestController
public class RegistrationController {

    private final AccountService accountService;
    @Autowired
    public RegistrationController(AccountService accountService){
        this.accountService=accountService;
    }

    @RequestMapping(path = "/ali/user", method = RequestMethod.POST)
    private ResponseEntity login(@RequestParam(name = "login") String login,
                                 @RequestParam(name = "password") String password,
                                 @RequestParam(name = "email") String email){
        if (StringUtils.isEmpty(login)||StringUtils.isEmpty(password)||StringUtils.isEmpty(email)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }
        UserProfile user = accountService.getUser(login);

        if (user!=null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }

        return ResponseEntity.ok(new SuccessResponse(login));
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.POST)
    public ResponseEntity auth(@RequestParam(name = "login") String login,
                               @RequestParam(name = "password") String password) {
        if (StringUtils.isEmpty(login)||StringUtils.isEmpty(password)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }
        final UserProfile user = accountService.getUser(login);
        if (user.getPassword().equals(password)){
            return ResponseEntity.ok(new SuccessResponse(login));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
    }

    private static final class SuccessResponse {
        private String login;

        private SuccessResponse(String login) {
            this.login = login;
        }

        //Функция необходима для преобразования см  https://en.wikipedia.org/wiki/Plain_Old_Java_Object
        @SuppressWarnings("unused")
        public String getLogin() {
            return login;
        }
    }

    private static final class BadResponse {
        private String lastLogin;

        private BadResponse(String login) {
            this.lastLogin = login;
        }

        //Справа, на полосе прокрутки, можно увидеть желтые метки.
        //Это потенциальные ошибки и места, которые можно улучшить или инспекции
        //Если вы уверены в том как написан ваш код (только в этом случае), то можете убрать их. Это назвыется подавление инспекций
        @SuppressWarnings("unused")
        public String getLastLogin() {
            return lastLogin;
        }
    }

}
