package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.AccountDto;
import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.model.RoleName;
import ir.maktab.quizmaker.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    AccountRepository accountRepository;

    @Autowired
    public AuthenticationServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto login(AccountDto command) {
        Account username = accountRepository.findByUsername(command.getUsername());
        return new AccountDto(command.getUsername(),command.getPassword(),username.isEnabled(),recognizeRole(username));
    }

    private String recognizeRole(Account account){
        if(account.getRoleList().stream().anyMatch(role -> role.getRoleName() == RoleName.ROLE_MANAGER))return "manager";
        if(account.getRoleList().stream().anyMatch(role -> role.getRoleName() == RoleName.ROLE_TEACHER))return "teacher";
        if(account.getRoleList().stream().allMatch(role -> role.getRoleName() == RoleName.ROLE_STUDENT))return "student";
        return null;
    }
}
