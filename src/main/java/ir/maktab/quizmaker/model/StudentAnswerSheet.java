package ir.maktab.quizmaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudentAnswerSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerSheetId;

    private Date createdDate;

    private Date filledDate;

    private boolean isOnTime;

    private double finalScore;

    @ManyToOne
    @JoinColumn(name = "examiner_id")
    private Person examiner;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @OneToMany(mappedBy = "studentAnswerSheet")
    private List<StudentAnswer> studentAnswers = new ArrayList<>();
}
