package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.model.Role;
import ir.maktab.quizmaker.model.RoleName;
import ir.maktab.quizmaker.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
                account -> new AccountPendedDto(account.getPerson().getFirstName(),
                        account.getPerson().getLastName(),
                        account.getUsername(),
                        account.getEmail(),
                        accountService.convertRolesListToString(account.getRoleList())))
                .collect(Collectors.toList());
        return accountPendedDtoList;
    }

    @PostMapping(value = "/load-all-account")
    private List<AccountEditableDto> loadAllAccount() {
        List<Account> accounts = accountService.loadAllAccount();
        List<AccountEditableDto> accountEditableDtos = accounts.stream().map(
                account -> new AccountEditableDto(account.getPerson().getFirstName(),
                        account.getPerson().getLastName(),
                        account.getUsername(),
                        accountService.convertBooleanStatusToString(account.isEnabled()),
                        accountService.convertRolesListToString(account.getRoleList())))
                .collect(Collectors.toList());
        return accountEditableDtos;
    }

    @PostMapping("/submit")
    private OutMessage submitAccountByManager(@RequestBody AccountSubmitDto accountSubmitDto) {
        Account account = accountService.submitAccountByManger(accountSubmitDto);
        return new OutMessage("successfully submit user:" + account.getUsername());
    }

    @PostMapping("/editAccount")
    private OutMessage editAccountByManager(@RequestBody AccountEditedByManagerFromFrontDto account) {
        Account account1 = accountService.editAccountByManager(account);
        return new OutMessage("successfully submit user:" + account1.getUsername());
    }

    @PostMapping("/unableAccount")
    private OutMessage unableAccountByManager(@RequestBody Account AccountUnableDto) {
        System.out.println(AccountUnableDto);
        Account account1 = accountService.editAccountByManager(AccountUnableDto);
        return new OutMessage("successfully unable user:" + account1.getUsername());
    }


}
