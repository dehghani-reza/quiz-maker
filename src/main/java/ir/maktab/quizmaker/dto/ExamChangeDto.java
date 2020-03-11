package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class ExamChangeDto {

    private String examId;

    private String examTitle;

    private String examExplanation;

    private String examDuration;
}
