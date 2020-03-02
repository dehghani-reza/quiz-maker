package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class CourseEditDto {

    private String courseId;

    private String courseTitle;

    private String startDate;

    private String endDate;
}
