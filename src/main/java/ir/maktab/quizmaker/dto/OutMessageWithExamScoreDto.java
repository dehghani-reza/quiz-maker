package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class OutMessageWithExamScoreDto {

    private String message;

    private float examScore;
}
