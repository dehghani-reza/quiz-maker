package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class CreateExamDto {

    private String courseId;

    private String examTitle;

    private String examExplanation;

    private String examDuration;

}
