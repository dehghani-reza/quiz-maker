package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class AccountEditableDto {

    private String firstName;

    private String lastName;

    private String username;

    private String isEnable;

    private String role;
}
