package ir.maktab.quizmaker.controller;


import ir.maktab.quizmaker.dto.AccountDto;
import ir.maktab.quizmaker.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/user")
public class AuthenticationController {

    AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    private AccountDto login(@RequestBody AccountDto command) throws Exception {
        return authenticationService.login(command);
    }
}
