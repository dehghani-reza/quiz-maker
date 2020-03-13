package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.model.Exam;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

public interface QuestionService {

    float createSimpleQuestion(CreateSimpleQuestionDto questionDto);

    float createOptionalQuestion(CreateOptionalQuestionDto questionDto);

    List<QuestionOutDto> loadAllQuestionOfExam(Exam exam);

    float editQuestionFromExam(QuestionChangeExamDto question);

    float deleteQuestionFromExam(QuestionChangeExamDto question);

    List<QuestionOutDto> loadAllTeacherQuestion(Account account);

    float addQuestionFromBankToExam(QuestionIdScoreDto questionIdScoreDto) throws Exception;
}
