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
@DiscriminatorValue(value = "teacher")
public class Teacher extends Person {

    public Teacher(Long personId, String firstName, String lastName, Account account) {
        super(personId, firstName, lastName, account);
    }

    @OneToMany(mappedBy = "teacher")
    private List<Course> courseList;

    @ManyToMany(mappedBy = "teachersStudent")
    private List<Course> studentInCourseList;
}
