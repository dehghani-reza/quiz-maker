package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class CourseOutDto {

    private String id;

    private String startDate;

    private String endDate;

    private String courseName;

    private String teacherName;
}
