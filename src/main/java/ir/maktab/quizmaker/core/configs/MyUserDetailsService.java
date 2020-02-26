package ir.maktab.quizmaker.core.configs;

import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {


    AccountRepository accountRepository;

    @Autowired
    public MyUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Account> account = accountRepository.findAccountByUsername(username);
        account.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
        return account.map(MyUserDetails::new).get();
    }
}