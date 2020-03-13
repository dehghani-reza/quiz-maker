package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.model.Course;
import ir.maktab.quizmaker.model.Exam;

import java.util.List;

public interface ExamService {

    Exam createExam(CreateExamDto createExamDto) throws Exception;

    List<ExamOutDto> loadAllCourseExam(Course course) throws Exception;

    void deleteExamFromCourse(Exam exam);

    ExamOutDto changeExam(ExamChangeDto examChangeDto) throws Exception;
}
