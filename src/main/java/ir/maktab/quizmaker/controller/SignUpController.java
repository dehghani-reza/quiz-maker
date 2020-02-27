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

    @Autowired
    public SignUpController(AccountService accountService,PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
    }

    //todo sign up jason could not have header.
    @PostMapping("/data")
    public OutMessage signUp(@RequestBody SignUpAccountDto signUpAccountDto) throws Exception {
        System.out.println(signUpAccountDto.toString());
        Account signUpAccount = accountService.signUpAccount(signUpAccountDto);
        return new OutMessage("Sign up with user name : " + signUpAccount.getUsername() + " successfully done!");
    }
}
