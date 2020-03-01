package ir.maktab.quizmaker.dto;

import ir.maktab.quizmaker.model.Teacher;
import lombok.Value;

import java.util.Date;

@Value
public class CourseCreationDto {

    private String startDate;

    private String courseTitle;

    private String endDate;

}
