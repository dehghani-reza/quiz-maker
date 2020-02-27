package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.exceptions.NotValidAccountException;
import ir.maktab.quizmaker.model.Account;

import java.util.List;

public interface AccountService {

    Account signUpAccount(Account account) throws NotValidAccountException;

    List<Account> loadPendedAccount();
}
