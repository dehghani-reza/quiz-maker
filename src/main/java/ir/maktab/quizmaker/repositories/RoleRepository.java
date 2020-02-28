package ir.maktab.quizmaker.repositories;

import ir.maktab.quizmaker.model.Role;
import ir.maktab.quizmaker.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByRoleName(RoleName roleName);

}
