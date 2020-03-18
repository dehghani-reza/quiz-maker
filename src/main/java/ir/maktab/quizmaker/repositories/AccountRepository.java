package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.model.Role;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> , JpaSpecificationExecutor<Account> {

    Optional<Account> findAccountByUsername(String username);

    List<Account> findAllByEnabledFalse();

    Account findByUsername(String username);

    Long countAllByEnabledIsTrue();

    Long countAccountsByEnabledFalse();

}
