package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class ChangeOptionalQuestionDto {

    private String questionTitle;

    private String questionContext;

    private String questionAnswer;

    private String questionOption;

    private Long questionId;
}
