package ir.maktab.quizmaker.controller;


import ir.maktab.quizmaker.dto.AccountDto;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/user")
public class AuthenticationController {



    @PostMapping("/login")
    private AccountDto login(@RequestBody AccountDto command) throws Exception {
        return new AccountDto(command.getUsername(),command.getPassword(),true);
    }
}
