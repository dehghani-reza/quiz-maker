package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long> {

    List<Teacher> findAllByAccountNotNull();
}
