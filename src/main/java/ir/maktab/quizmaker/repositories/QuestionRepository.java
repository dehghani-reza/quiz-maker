package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

    Optional<List<Question>> findAllByQuestionIdIn(Collection<Long> questionId);
}
