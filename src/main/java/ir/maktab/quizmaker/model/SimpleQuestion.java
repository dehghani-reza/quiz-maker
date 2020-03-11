package ir.maktab.quizmaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor

@Entity
@DiscriminatorValue("simple_question")
public class SimpleQuestion extends Question {

}
