package ir.maktab.quizmaker.model;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@DiscriminatorValue(value = "teacher")
public class Teacher extends Person {

    @OneToMany(mappedBy = "teacher")
    private List<Course> courseList;
}
