package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class CourseForTeacherDto {

    private String courseId;
    private String courseName;
    private String startDate;
    private String endDate;
    private String studentNumber;

}
