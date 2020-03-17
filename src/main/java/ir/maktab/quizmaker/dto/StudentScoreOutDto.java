package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class StudentScoreOutDto {

    private Long sheetId;

    private String examTitle;

    private String condition;

    private String examExplanation;

    private double yourScore;

    private double averageScore;

}
