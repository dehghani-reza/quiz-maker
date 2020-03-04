package ir.maktab.quizmaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    private LocalDate startDate;

    private String courseTitle;

    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "teacherId")
    private Teacher teacher;

    @ManyToMany
    @JoinTable(name = "student_course",
            joinColumns = @JoinColumn(name = "courseId"),
            inverseJoinColumns =@JoinColumn(name = "personId"))
    private List<Student> studentList;

    @ManyToMany
    @JoinTable(name = "teachersStudent_course",
            joinColumns = @JoinColumn(name = "courseId"),
            inverseJoinColumns =@JoinColumn(name = "personId") )
    private List<Teacher> teachersStudent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return getCourseId().equals(course.getCourseId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourseId());
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", teacher=" + teacher +
                '}';
    }

    public Course(Long courseId , LocalDate startDate, LocalDate endDate, String courseTitle , Teacher teacher , List<Student> studentList) {
        this.courseId=courseId;
        this.startDate=startDate;
        this.endDate=endDate;
        this.courseTitle=courseTitle;
        this.teacher=teacher;
        this.studentList=studentList;
    }
}
