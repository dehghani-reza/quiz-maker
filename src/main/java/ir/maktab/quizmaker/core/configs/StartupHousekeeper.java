package ir.maktab.quizmaker.core.configs;

import ir.maktab.quizmaker.model.*;
import ir.maktab.quizmaker.repositories.AccountRepository;
import ir.maktab.quizmaker.repositories.ManagerRepository;
import ir.maktab.quizmaker.repositories.PersonRepository;
import ir.maktab.quizmaker.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StartupHousekeeper {

    RoleRepository roleRepository;
    AccountRepository accountRepository;
    ManagerRepository managerRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public StartupHousekeeper(RoleRepository roleRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder,ManagerRepository managerRepository) {
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.managerRepository = managerRepository;
    }


    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        Optional<Role> byRoleName = roleRepository.findByRoleName(RoleName.ROLE_MANAGER);
        if (byRoleName.isEmpty()) {
            Manager p = new Manager();
            p.setFirstName("mohammadreza");
            p.setLastName("dehghani");
            Account account = new Account(null,
                    "mohammadreza",
                    passwordEncoder.encode("09104539431"),
                    "manager@app.ir",
                    Collections.singletonList(new Role(null, RoleName.ROLE_MANAGER, null)),
                    p,
                    true);
            accountRepository.save(account);
        }
    }


}
