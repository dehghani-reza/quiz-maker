package ir.maktab.quizmaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;

    private String title;

    private String explanation;

    //todo for operate exam we can create timer and receive time from browser if have 30second difference we can ignore else throw exception
    private LocalTime examDuration;

    private boolean isEnded;

    @ManyToMany
    @JoinTable(name = "exam_question",
            joinColumns = @JoinColumn(name = "examId"),
            inverseJoinColumns = @JoinColumn(name = "questionId"))
    private List<Question> questionList;

    @OneToMany(mappedBy = "exam")
    private List<Score> scores;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exam)) return false;
        Exam exam = (Exam) o;
        return getExamId().equals(exam.getExamId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExamId());
    }

    @Override
    public String toString() {
        return "Exam{" +
                "examId=" + examId +
                ", title='" + title + '\'' +
                ", explanation='" + explanation + '\'' +
                ", isEnded=" + isEnded +
                ", course=" + course +
                '}';
    }
}
