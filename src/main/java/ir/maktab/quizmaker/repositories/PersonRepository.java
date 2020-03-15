package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Period;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {

    Person findByAccount_Username(String username);

}
