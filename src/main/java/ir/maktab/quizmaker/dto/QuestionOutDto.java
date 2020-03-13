package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class QuestionOutDto {

    private Long questionId;

    private String context;

    private String answer;

    private String title;

    private float score;

    private String type;

}
