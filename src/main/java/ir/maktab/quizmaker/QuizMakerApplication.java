package ir.maktab.quizmaker;

import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.model.Role;
import ir.maktab.quizmaker.model.RoleName;
import ir.maktab.quizmaker.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.Arrays;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = AccountRepository.class)
public class QuizMakerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizMakerApplication.class, args);
    }

}
