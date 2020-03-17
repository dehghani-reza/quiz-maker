package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.model.Course;

import java.text.ParseException;
import java.util.List;

public interface CourseService {

    Course createCourseByManager(CourseCreationDto courseCreationDto) throws Exception;

    List<CourseOutDto> loadAllCourseForManager();

    void deleteCourseByManager(Long id);

    List<TeacherIdAndNameDto> loadAllTeacher();

    CourseOutDto editCourse(CourseEditDto courseCreationDto) throws Exception;

    List<StudentInCourseDto> loadAllCourseStudent(Course course);

    List<StudentInCourseDto> loadAllStudentForAddingToCourse(CourseEditDto courseEditDto);

    Course addStudentToCourse(StudentListAssignForCourseDto students) throws Exception;

    void deleteStudentFromCourse(StudentDeleteFromCourseDto studentCourse) throws Exception;

    List<CourseForTeacherDto> loadAllTeacherCourse(Account account) throws Exception;

    List<CourseOutDto> loadAllCourseForStudent(Account account);

    List<AccountStatusDto> loadAccountStatus();
}
