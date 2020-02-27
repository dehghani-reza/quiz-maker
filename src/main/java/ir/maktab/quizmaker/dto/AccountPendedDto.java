package ir.maktab.quizmaker.dto;
import ir.maktab.quizmaker.model.Role;

import lombok.Value;

import java.util.List;
@Value
public class AccountPendedDto {

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String role;
}
