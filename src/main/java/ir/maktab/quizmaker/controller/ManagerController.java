package ir.maktab.quizmaker.controller;
import ir.maktab.quizmaker.dto.AccountPendedDto;
import ir.maktab.quizmaker.dto.AccountSubmitDto;
import ir.maktab.quizmaker.dto.OutMessage;
import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.model.Role;
import ir.maktab.quizmaker.model.RoleName;
import ir.maktab.quizmaker.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/manager")
public class ManagerController {

    AccountService accountService;

    @Autowired
    public ManagerController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/load-pended-account")
    private List<AccountPendedDto> loadPendedAccount() {
        List<Account> accounts = accountService.loadPendedAccount();
        List<AccountPendedDto> accountPendedDtoList = accounts.stream().map(
                account -> new AccountPendedDto(account.getPerson().getFirstName(), account.getPerson().getLastName(),
                        account.getUsername(), account.getEmail(), account.getRoleList().toString())).collect(Collectors.toList());
        return accountPendedDtoList;
    }

    @PostMapping("/submit")
    private OutMessage submitAccountByManager(@RequestBody AccountSubmitDto accountSubmitDto) {
        System.out.println(accountSubmitDto.toString());
        List<Role> roleList = new ArrayList<>();
        if(!(accountSubmitDto.getRole()==null||accountSubmitDto.getRole().equals("")||accountSubmitDto.getRole().isEmpty())){
            if(accountSubmitDto.getRole().contains("TEACHER")){
                roleList.add(new Role(null, RoleName.ROLE_TEACHER,null));
            }
            if(accountSubmitDto.getRole().contains("STUDENT")){
                roleList.add(new Role(null, RoleName.ROLE_STUDENT,null));
            }
        }
        Account account = accountService.submitAccountByManger(accountSubmitDto.getUsername(), roleList);
        return new OutMessage("successfully submit user:" + account.getUsername());
    }
}
