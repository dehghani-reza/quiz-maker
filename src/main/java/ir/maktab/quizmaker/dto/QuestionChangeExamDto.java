package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class QuestionChangeExamDto {

    private Long examId;

    private Long questionId;

    private float questionScore;
}
