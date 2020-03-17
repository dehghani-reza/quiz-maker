package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class ChangeSimpleQuestionDto {

    private String questionTitle;

    private String questionContext;

    private String questionAnswer;

    private Long questionId;

}
