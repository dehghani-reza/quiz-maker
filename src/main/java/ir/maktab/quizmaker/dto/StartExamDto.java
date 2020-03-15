package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class StartExamDto {

    private String username;

    private Long examId;

    private String startTime;
}
