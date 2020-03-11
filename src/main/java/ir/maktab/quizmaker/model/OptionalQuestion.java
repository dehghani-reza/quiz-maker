package ir.maktab.quizmaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@DiscriminatorValue("optional_question")
public class OptionalQuestion extends Question {

    private String firstOption;

    private String secondOption;

    private String thirdOption;

    private String fourthOption;

}
