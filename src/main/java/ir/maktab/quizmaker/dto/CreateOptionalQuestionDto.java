package ir.maktab.quizmaker.dto;

import lombok.Value;
@Value
public class CreateOptionalQuestionDto {

        private String username;

        private Long examId;

        private Long courseId;

        private String questionTitle;

        private String questionContext;

        private String questionAnswer;

        private String questionOption2;

        private String questionOption3;

        private String questionOption4;

        private float questionScore;

    }
