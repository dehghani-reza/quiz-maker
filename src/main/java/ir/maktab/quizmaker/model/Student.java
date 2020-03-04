package ir.maktab.quizmaker.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@DiscriminatorValue(value = "student")
public class Student extends Person {

    public Student(Long personId, String firstName, String lastName, Account account) {
        super(personId, firstName, lastName, account);
    }

    @ManyToMany(mappedBy = "studentList")
    private List<Course> courseList;


}
