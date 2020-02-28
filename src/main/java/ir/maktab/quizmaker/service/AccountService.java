package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.AccountDto;
import ir.maktab.quizmaker.dto.AccountSubmitDto;
import ir.maktab.quizmaker.dto.SignUpAccountDto;
import ir.maktab.quizmaker.exceptions.NotValidAccountException;
import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.model.Role;

import java.util.List;

public interface AccountService {

    Account signUpAccount(SignUpAccountDto signUpAccountDto) throws NotValidAccountException;

    List<Account> loadPendedAccount();

    Account submitAccountByManger(AccountSubmitDto accountSubmitDto);
}
