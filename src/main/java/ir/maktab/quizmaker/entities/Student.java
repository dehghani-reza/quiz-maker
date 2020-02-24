package ir.maktab.quizmaker.entities;

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

    @ManyToMany
    @JoinTable(name = "student_course",
            joinColumns = @JoinColumn(name = "personId"),
            inverseJoinColumns = @JoinColumn(name = "courseId"))
    private List<Course> courseList;


}
