package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.StudentAnswerSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAnswerSheetRepository extends JpaRepository<StudentAnswerSheet,Long> {

    StudentAnswerSheet findByExam_ExamIdAndExaminer_PersonId(Long exam_examId, Long examiner_personId);

    List<StudentAnswerSheet> findAllByExaminer_Account_Username(String examiner_account_username);

    Optional<StudentAnswerSheet> findByExaminer_Account_UsernameAndExam_ExamId(String examiner_account_username, Long exam_examId);
}
