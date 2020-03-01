package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.CourseCreationDto;
import ir.maktab.quizmaker.dto.CourseOutDto;
import ir.maktab.quizmaker.model.Course;

import java.text.ParseException;
import java.util.List;

public interface CourseService {

    Course createCourseByManager(CourseCreationDto courseCreationDto) throws ParseException;

    List<CourseOutDto> loadAllCourseForManager();

    void deleteCourseByManager(Long id);
}
