package web.service;

import org.springframework.stereotype.Service;
import web.database.model.Account;
import web.database.model.Action;
import web.database.repository.AccountRepository;
import web.database.repository.ActionRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class MvcService {

    private final AccountRepository accountRepository = new AccountRepository();
    private final ActionRepository actionRepository = new ActionRepository();

    public void createAccount(Account newAccount) {
        accountRepository.addAccount(newAccount.getBalance(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).format(LocalDateTime.now()));
    }

    public void createAction(Action newAction) {
        try {
            actionRepository.addAction(newAction.getTitle(), newAction.getAmount(), newAction.getType(), newAction.getAccountId(),
                    newAction.getStatus(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).format(LocalDateTime.now()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
