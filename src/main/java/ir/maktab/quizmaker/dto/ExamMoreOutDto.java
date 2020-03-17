package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class ExamMoreOutDto {

    private Long examId;

    private String courseName;

    private String examTitle;

    private String status;

    private int studentNumber;

    private int studentParticipationNumber;

    private double examScore;

    private double averageScore;

}
