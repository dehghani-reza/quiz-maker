package ir.maktab.quizmaker.dto;

import lombok.Value;

import java.util.List;

@Value
public class StudentListAssignForCourseDto {

    private String courseId;

    private List<String> studentForCourse;
}
