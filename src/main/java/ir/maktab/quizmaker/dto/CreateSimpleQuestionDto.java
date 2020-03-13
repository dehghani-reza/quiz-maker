package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class CreateSimpleQuestionDto {

    private String username;

    private Long examId;

    private Long courseId;

    private String questionTitle;

    private String questionContext;

    private String questionAnswer;

    private float questionScore;

}
