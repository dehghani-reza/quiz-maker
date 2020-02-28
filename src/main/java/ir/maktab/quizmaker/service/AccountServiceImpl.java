package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.AccountSubmitDto;
import ir.maktab.quizmaker.dto.SignUpAccountDto;
import ir.maktab.quizmaker.exceptions.NotValidAccountException;
import ir.maktab.quizmaker.model.*;
import ir.maktab.quizmaker.repositories.AccountRepository;
import ir.maktab.quizmaker.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

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
    public Account signUpAccount(SignUpAccountDto signUpAccountDto) throws NotValidAccountException {
        // validation
        if (signUpAccountDto.getPassword().equals("") | signUpAccountDto.getUsername().equals("") || signUpAccountDto.getEmail().equals("")) {
            throw new NotValidAccountException("this Account cant be created");
        }
        //creating account progress
        Role role = convertStringToValidRole(signUpAccountDto.getRoleName());
        List<Role> roleList = Collections.singletonList(role);
        Person person;
        if (role.getRoleName().equals(RoleName.ROLE_TEACHER)) {
            person = new Teacher(null, signUpAccountDto.getFirstName(), signUpAccountDto.getLastName(), null);
        } else {
            person = new Student(null, signUpAccountDto.getFirstName(), signUpAccountDto.getLastName(), null);
        }
        Account account = new Account(null, signUpAccountDto.getUsername(),
                passwordEncoder.encode(signUpAccountDto.getPassword()),
                signUpAccountDto.getEmail(),
                roleList,
                person, false);
        return accountRepository.save(account);
    }

    @Override
    public List<Account> loadPendedAccount() {
        return accountRepository.findAllByEnabledFalse();
    }

    @Override
    public Account submitAccountByManger(AccountSubmitDto accountSubmitDto) {
        Account account = accountRepository.findByUsername(accountSubmitDto.getUsername());
        account.setEnabled(true);
        if (accountSubmitDto.getRole() == null || accountSubmitDto.getRole().equals("") || accountSubmitDto.getRole().isEmpty()) {
            return accountRepository.save(account);
        }
        List<RoleName> managerConfirmRoleString = convertMultipleStringToRoleNameList(accountSubmitDto.getRole());
        List<Role> managerConfirmRole = managerConfirmRoleString.stream().map(roleName -> {
            try {
                return convertStringToValidRole(roleName.toString());
            } catch (NotValidAccountException e) {
                System.out.println(e.getMessage() + "when want to submit user by username: " + accountSubmitDto.getUsername());
            }
            return null;
        }).collect(Collectors.toList());
        if (!compareAccountRoleWithWhatManagerSubmit(managerConfirmRole, account.getRoleList())) {
            account.setRoleList(null);
            if (account.getPerson() instanceof Student && managerConfirmRole.size() == 2) {
                Person person = new Teacher(null, account.getPerson().getFirstName(), account.getPerson().getLastName(), null);
                account.setPerson(person);
            } else if (managerConfirmRole.size() == 1) {
                Person person = createPersonByRole(managerConfirmRole.get(0), account.getPerson().getFirstName(), account.getPerson().getLastName());
                account.setPerson(person);
            }
            accountRepository.save(account);
            account.setRoleList(managerConfirmRole);
        }
        return accountRepository.save(account);
    }

    private boolean compareAccountRoleWithWhatManagerSubmit(List<Role> managerConfirm, List<Role> accountSignedUp) {
        int counter = managerConfirm.size();
        Collection<String> strings;
        Collection<String> strings1;
        strings = managerConfirm.stream().map(role -> role.getRoleName().toString()).collect(Collectors.toList());
        strings1 = accountSignedUp.stream().map(role -> role.getRoleName().toString()).collect(Collectors.toList());
        if(strings.size()!=strings1.size()) return false;
        strings.retainAll(strings1);
        return strings.size() == counter;
    }

    private Role convertStringToValidRole(String role) throws NotValidAccountException {
        RoleName roleName;
        if (role.contains("TEACHER")) {
            roleName = RoleName.ROLE_TEACHER;
        } else if (role.contains("STUDENT")) {
            roleName = RoleName.ROLE_STUDENT;
        } else {
            throw new NotValidAccountException("role not valid");
        }
        Optional<Role> optionalRole = roleRepository.findByRoleName(roleName);
        return optionalRole.orElseGet(() -> switch (roleName.toString()) {
            case "ROLE_TEACHER" -> new Role(null, RoleName.ROLE_TEACHER, null);
            case "ROLE_STUDENT" -> new Role(null, RoleName.ROLE_STUDENT, null);
            default -> throw new IllegalStateException("Unexpected value: " + roleName.toString());
        });
    }

    private List<RoleName> convertMultipleStringToRoleNameList(String role) {
        List<RoleName> roleList = new ArrayList<>();
        if (role.contains("TEACHER")) {
            roleList.add(RoleName.ROLE_TEACHER);
        }
        if (role.contains("STUDENT")) {
            roleList.add(RoleName.ROLE_STUDENT);
        }
        return roleList;
    }

    private Person createPersonByRole(Role role, String firstName, String lastName) {
        if (role.getRoleName().equals(RoleName.ROLE_TEACHER)) {
            return new Teacher(null, firstName, lastName, null);
        } else {
            return new Student(null, firstName, lastName, null);
        }
    }
}
