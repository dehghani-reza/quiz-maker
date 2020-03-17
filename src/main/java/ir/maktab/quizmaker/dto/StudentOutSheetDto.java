package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class StudentOutSheetDto {

    private String questionContext;
    private String questionAnswer;
    private String studentAnswer;
    private String isCorrected;
    private float score;
    private float studentScore;
    private float averageScore;
}
