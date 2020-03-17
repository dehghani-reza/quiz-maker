package ir.maktab.quizmaker.controller;
import ir.maktab.quizmaker.dto.OutMessage;
import ir.maktab.quizmaker.dto.WelcomeMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message/first-login")
public class FirstLoginMessageController {


    @PostMapping
    private OutMessage firstLoginMessage(@RequestBody WelcomeMessage command) throws Exception {
        return new OutMessage("خوش آمدید ..."+command.getWhoIAm());
    }
}
