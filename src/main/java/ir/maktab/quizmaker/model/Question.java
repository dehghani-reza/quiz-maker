package ir.maktab.quizmaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type" , discriminatorType =DiscriminatorType.STRING)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Lob
    private String context;

    @Lob
    private String answer;

    private String title;

    @ManyToOne
    @JoinColumn(name = "teacherId")
    private Teacher usernameCreator;

    @ManyToOne
    @JoinColumn(name = "cpurseId")
    private Course courseCreatedId;

    @OneToMany(mappedBy = "question",cascade = CascadeType.PERSIST)
    private List<Score> scoreList = new ArrayList<>();


    @ManyToMany(mappedBy = "questionList")
    private List<Exam> exams;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question question = (Question) o;
        return getQuestionId().equals(question.getQuestionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuestionId());
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", usernameCreator=" + usernameCreator +
                ", title='" + title + '\'' +
                '}';
    }
}
