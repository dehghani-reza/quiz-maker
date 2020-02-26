package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.dto.OutMessage;
import ir.maktab.quizmaker.dto.SignUpAccountDto;
import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.model.Person;
import ir.maktab.quizmaker.model.Role;
import ir.maktab.quizmaker.model.RoleName;
import ir.maktab.quizmaker.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/signUp")
public class SignUpController {

    AccountService accountService;

    @Autowired
    public SignUpController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/data")
    public OutMessage signUp(@RequestBody SignUpAccountDto signUpAccountDto) throws Exception {
        List<Role> roleList = new ArrayList<>();
        if (signUpAccountDto.getRoleName().contains("TEACHER")) {
            roleList.add(new Role(null, RoleName.ROLE_TEACHER,null));
        }else if(signUpAccountDto.getRoleName().contains("STUDENT")){
            roleList.add(new Role(null, RoleName.ROLE_STUDENT,null));
        }else {
            throw new Exception("Role Not valid");
        }
        Person person = new Person(null,signUpAccountDto.getFirstName(),signUpAccountDto.getLastName(),null);
        Account account = new Account(null, signUpAccountDto.getUsername(),signUpAccountDto.getPassword(),signUpAccountDto.getEmail(),roleList,null,false);
        person.setAccount(account);
        account.setPerson(person);
        Account signUpAccount = accountService.signUpAccount(account);
        return new OutMessage("Sign up with user name : "+signUpAccount.getUsername()+" successfully done!");
    }
}
