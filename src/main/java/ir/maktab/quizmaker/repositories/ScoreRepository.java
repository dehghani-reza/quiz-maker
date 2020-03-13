package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score,Long> {

    Score findByQuestion_QuestionIdAndExam_ExamId(Long question_questionId, Long exam_examId);
}
