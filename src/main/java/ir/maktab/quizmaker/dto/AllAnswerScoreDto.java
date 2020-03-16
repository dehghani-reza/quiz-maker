package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class AllAnswerScoreDto {

    private String username;

    private String[][] scoreQuestionList;
}
