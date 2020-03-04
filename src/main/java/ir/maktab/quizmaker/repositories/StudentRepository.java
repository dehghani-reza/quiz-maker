package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.Course;
import ir.maktab.quizmaker.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

    List<Student> findAllByAccountNotNull();

    Optional<List<Student>> findAllByCourseListIn(Collection<List<Course>> courseList);
}
