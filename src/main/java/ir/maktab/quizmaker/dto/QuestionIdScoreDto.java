package ir.maktab.quizmaker.dto;

import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class QuestionIdScoreDto {

    private Long examId;

    private List<Long> questionForExam;

    private List<Float> scoreQuestionForExam;
}
