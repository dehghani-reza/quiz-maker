package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer,Long> {

    List<StudentAnswer> findAllByExam_ExamIdAndQuestion_QuestionId(Long exam_examId, Long question_questionId);
}
