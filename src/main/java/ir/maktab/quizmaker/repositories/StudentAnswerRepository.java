package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer,Long> {
}
