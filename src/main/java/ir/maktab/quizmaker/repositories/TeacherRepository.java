package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.Course;
import ir.maktab.quizmaker.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long> {

    List<Teacher> findAllByAccountNotNull();

    Optional<Teacher> findByAccount_Username(String username);

    Optional<List<Teacher>> findAllByStudentInCourseListIn(Collection<List<Course>> studentInCourseList);
}
