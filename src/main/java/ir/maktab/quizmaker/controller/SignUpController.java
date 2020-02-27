package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.dto.OutMessage;
import ir.maktab.quizmaker.dto.SignUpAccountDto;
import ir.maktab.quizmaker.model.*;
import ir.maktab.quizmaker.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/signUp")
public class SignUpController {

    private AccountService accountService;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public SignUpController(AccountService accountService,PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder= passwordEncoder;
    }

    //todo sign up jason could not have header.
    @PostMapping("/data")
    public OutMessage signUp(@RequestBody SignUpAccountDto signUpAccountDto) throws Exception {
        List<Role> roleList = new ArrayList<>();
        Person person;
        if (signUpAccountDto.getRoleName().contains("TEACHER")) {
            roleList.add(new Role(null, RoleName.ROLE_TEACHER, null));
            person = new Teacher(null, signUpAccountDto.getFirstName(), signUpAccountDto.getLastName(), null);
        } else if (signUpAccountDto.getRoleName().contains("STUDENT")) {
            roleList.add(new Role(null, RoleName.ROLE_STUDENT, null));
            person = new Student(null, signUpAccountDto.getFirstName(), signUpAccountDto.getLastName(), null);
        } else {
            throw new Exception("Role Not valid");
        }
        Account account = new Account(null, signUpAccountDto.getUsername(),passwordEncoder.encode(signUpAccountDto.getPassword()), signUpAccountDto.getEmail(), roleList, null, false);
        person.setAccount(account);
        account.setPerson(person);
        Account signUpAccount = accountService.signUpAccount(account);
        return new OutMessage("Sign up with user name : " + signUpAccount.getUsername() + " successfully done!");
    }
}
