package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.StudentAnswerSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAnswerSheetRepository extends JpaRepository<StudentAnswerSheet,Long> {
}
