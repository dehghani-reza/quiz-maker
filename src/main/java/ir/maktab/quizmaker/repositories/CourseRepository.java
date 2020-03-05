package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.Course;
import ir.maktab.quizmaker.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    Optional<List<Course>> findAllByTeacher(Teacher teacher);
}
