package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class ExamOutDto {

    private Long examId;

    private String examTitle;

    private String explanation;

    private String duration;

    private float totalScore;

}

