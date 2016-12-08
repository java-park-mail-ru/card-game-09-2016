package ru.mail.park.main;

import org.springframework.web.bind.annotation.RestController;
import ru.mail.park.services.AccountService;

@RestController
public class MainController {
    private static AccountService accountService;

    MainController(AccountService _accountService) {
        accountService=_accountService;
    }

    public static AccountService getAccountService() {
        return accountService;
    }
}
