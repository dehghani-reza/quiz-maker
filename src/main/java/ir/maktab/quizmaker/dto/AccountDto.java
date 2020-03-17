package ir.maktab.quizmaker.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountDto {

    private String username;

    private String password;

    private Boolean isAuthenticated;

    private String role;

}
