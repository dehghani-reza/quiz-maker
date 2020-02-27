package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.exceptions.NotValidAccountException;
import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account signUpAccount(Account account) throws NotValidAccountException {
        if(account.getPassword().equals("") | account.getUsername().equals("") ||account.getEmail().equals("")){
            throw new NotValidAccountException("this Account cant be created");
        }
        return accountRepository.save(account);
    }

    @Override
    public List<Account> loadPendedAccount() {
       return accountRepository.findAllByEnabledFalse();
    }


}
