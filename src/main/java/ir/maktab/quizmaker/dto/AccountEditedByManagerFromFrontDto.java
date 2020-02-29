package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class AccountEditedByManagerFromFrontDto {

    String username;

    String changedFirstName;

    String changedLastName;

    String role;


}
