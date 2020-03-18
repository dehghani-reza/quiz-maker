package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam,Long> {

    List<Exam> findAllByCourse_CourseId(Long course_courseId);

    Optional<List<Exam>> findAllByCourse_Teacher_Account_Username(String course_teacher_account_username);

    @Query("SELECT e FROM Exam e WHERE e.isEnded = false and e.isStarted =true and e.course.courseId= :courseId")
    List<Exam> findAllByCourse_CourseIdAndIsEndedFalse(@Param("courseId") Long courseId);


}
