package ir.maktab.quizmaker.dto;

import lombok.Value;

@Value
public class StudentDeleteFromCourseDto {

    private String studentUsername;

    private String courseId;
}
