package ir.maktab.quizmaker.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@EqualsAndHashCode(callSuper = true)
@Data

@Entity
@DiscriminatorValue("manager")
public class Manager extends Person {

}
