package ir.maktab.quizmaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @ManyToMany(mappedBy = "roleList")
    private List<Account> accountList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return getRoleId().equals(role.getRoleId()) &&
                getRoleName() == role.getRoleName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(), getRoleName());
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleName=" + roleName +
                '}';
    }
}
