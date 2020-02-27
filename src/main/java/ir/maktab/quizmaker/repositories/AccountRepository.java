package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findAccountByUsername(String username);

    List<Account> findAllByEnabledFalse();
}
