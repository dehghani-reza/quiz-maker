package ir.maktab.quizmaker.dto;

import lombok.Value;

import java.util.List;

@Value
public class SubmitAnswersDto {

    private String username;

    private Long examId;

    private String[][] answers;

    private Long endTime;
}
