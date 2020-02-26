package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.exceptions.NotValidAccountException;
import ir.maktab.quizmaker.model.Account;

public interface AccountService {

    Account signUpAccount(Account account) throws NotValidAccountException;
}
