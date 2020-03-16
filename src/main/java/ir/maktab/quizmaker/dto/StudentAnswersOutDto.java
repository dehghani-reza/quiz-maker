package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class StudentAnswersOutDto {

    private Long answerId;

    private String context;

    private float studentScore;

    private float questionScore;

    private String isCorrected;

    private String isTrue;

    private String questionContent;

    private String questionAnswer;
}
