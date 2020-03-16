package ir.maktab.quizmaker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

//    @JsonIgnore//todo really amazing tip its like lombok and going in to loop but remember we dont sent this object we sent dto of this class
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "account_role",
            joinColumns = @JoinColumn(name = "accountId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<Role> roleList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST,orphanRemoval = true)
    @JoinColumn(name = "personId")
    private Person person;

    private boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getAccountId().equals(account.getAccountId()) &&
                getUsername().equals(account.getUsername()) &&
                Objects.equals(getPassword(), account.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountId(), getUsername(), getPassword());
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
