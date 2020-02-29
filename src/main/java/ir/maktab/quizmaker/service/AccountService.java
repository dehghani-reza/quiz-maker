package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.exceptions.NotValidAccountException;
import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.model.Role;

import java.util.List;

public interface AccountService {

    Account signUpAccount(SignUpAccountDto signUpAccountDto) throws NotValidAccountException;

    List<Account> loadPendedAccount();

    List<Account> loadAllAccount();

    Account submitAccountByManger(AccountSubmitDto accountSubmitDto);

    String convertRolesListToString(List<Role> roles);

    String convertBooleanStatusToString(boolean status);

    Account editAccountByManager(AccountEditedByManagerFromFrontDto account);

    Account editAccountByManager(Account account);
}
