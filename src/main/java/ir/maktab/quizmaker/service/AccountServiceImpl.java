package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.SignUpAccountDto;
import ir.maktab.quizmaker.exceptions.NotValidAccountException;
import ir.maktab.quizmaker.model.*;
import ir.maktab.quizmaker.repositories.AccountRepository;
import ir.maktab.quizmaker.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Account signUpAccount(SignUpAccountDto signUpAccountDto) throws NotValidAccountException {//todo need to refactor and break down to little methods with single task
        List<Role> roleList = new ArrayList<>();
        Person person;
        if (signUpAccountDto.getRoleName().contains("TEACHER")) {
            Optional<Role> teacher = roleRepository.findByRoleName(RoleName.ROLE_TEACHER);
            if (teacher.isEmpty()) {
                roleList.add(new Role(null, RoleName.ROLE_TEACHER, null));
                person = new Teacher(null, signUpAccountDto.getFirstName(), signUpAccountDto.getLastName(), null);
            } else {
                roleList.add(teacher.get());
                person = new Teacher(null, signUpAccountDto.getFirstName(), signUpAccountDto.getLastName(), null);
            }
        } else if (signUpAccountDto.getRoleName().contains("STUDENT")) {
            Optional<Role> student = roleRepository.findByRoleName(RoleName.ROLE_STUDENT);
            if (student.isEmpty()) {
                roleList.add(new Role(null, RoleName.ROLE_STUDENT, null));
                person = new Student(null, signUpAccountDto.getFirstName(), signUpAccountDto.getLastName(), null);
            } else {
                roleList.add(student.get());
                person = new Student(null, signUpAccountDto.getFirstName(), signUpAccountDto.getLastName(), null);
            }

        } else {
            throw new NotValidAccountException("Role Not valid");
        }
        Account account = new Account(null, signUpAccountDto.getUsername(), passwordEncoder.encode(signUpAccountDto.getPassword()), signUpAccountDto.getEmail(), roleList, null, false);
        person.setAccount(account);
        account.setPerson(person);
        if (account.getPassword().equals("") | account.getUsername().equals("") || account.getEmail().equals("")) {
            throw new NotValidAccountException("this Account cant be created");
        }
        return accountRepository.save(account);
    }

    @Override
    public List<Account> loadPendedAccount() {
        return accountRepository.findAllByEnabledFalse();
    }

    @Override
    public Account submitAccountByManger(String username, List<Role> roles) {
        Account account = accountRepository.findByUsername(username);
        account.setEnabled(true);
        if (!roles.isEmpty()) {
            for (int i = 0; i < account.getRoleList().size(); i++) {
                roleRepository.deleteById(account.getRoleList().get(i).getRoleId());
            }
            roles.forEach(role -> System.out.println(role.getRoleName()));
            account.setRoleList(roles);
        }
        return accountRepository.save(account);
    }


}
