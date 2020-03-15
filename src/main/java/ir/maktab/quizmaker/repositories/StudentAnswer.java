package ir.maktab.quizmaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAnswer extends JpaRepository<ir.maktab.quizmaker.model.StudentAnswer,Long> {
}
