package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.model.Course;
import ir.maktab.quizmaker.model.Exam;
import ir.maktab.quizmaker.model.Question;

import java.util.List;
import java.util.Set;

public interface ExamService {

    Exam createExam(CreateExamDto createExamDto) throws Exception;

    List<ExamOutDto> loadAllCourseExam(Course course) throws Exception;

    void deleteExamFromCourse(Exam exam);

    ExamOutDto changeExam(ExamChangeDto examChangeDto) throws Exception;

    List<ExamOutDto> loadAllCourseExamForStudent(CourseExamDto courseExamDto);

    Set<QuestionOutExamDto> startExamForStudent(StartExamDto startExamDto);

    int submitAnswersToAnswerSheet(SubmitAnswersDto submitAnswersDto);
}
