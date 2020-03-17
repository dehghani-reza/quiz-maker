package ir.maktab.quizmaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.*;

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
    @Column(nullable = false)
    private LocalTime examDuration;

    private boolean isEnded;

    private boolean isStarted;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "exam_question",
            joinColumns = @JoinColumn(name = "examId"),
            inverseJoinColumns = @JoinColumn(name = "questionId"))
    private Set<Question> questionList = new HashSet<>();

    @OneToMany(mappedBy = "exam",orphanRemoval = true)
    private List<Score> scores;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

    @OneToMany(mappedBy = "exam",orphanRemoval = true)
    private List<StudentAnswer> studentAnswerList;

    @OneToMany(mappedBy = "exam",orphanRemoval = true)
    private List<StudentAnswerSheet> studentAnswerSheetList;

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
