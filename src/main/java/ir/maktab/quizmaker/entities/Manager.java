package ir.maktab.quizmaker.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@EqualsAndHashCode(callSuper = true)
@Data

@Entity
@DiscriminatorValue("manager")
public class Manager extends Person {
}
