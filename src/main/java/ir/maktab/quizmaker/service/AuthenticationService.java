package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.AccountDto;

public interface AuthenticationService {

    AccountDto login(AccountDto command);
}
