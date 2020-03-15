package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam,Long> {

    List<Exam> findAllByCourse_CourseId(Long courseId);
}
