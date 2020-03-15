package ir.maktab.quizmaker.dto;

import lombok.Value;

import java.util.List;

@Value
public class QuestionOutExamDto {

    private Long questionId;

    private String context;

    private float score;

    private String type;

    private int duration;

    private List<String> options;
}
