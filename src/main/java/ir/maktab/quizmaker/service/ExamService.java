package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.model.*;

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

    List<ExamMoreOutDto> loadAllExamForTeacher(Account account);

    List<AnswerSheetOutDto> loadAllExamStyleSheetForTeacher(Exam exam);

    List<StudentAnswersOutDto> loadStudentAnswerForCorrection(StudentAnswerSheet sheet);

    List<StudentAnswersOutDto> correctOneAnswerByTeacher(StudentAnswerDto answer) throws Exception;

    List<StudentAnswersOutDto> correctAllAnswerByTeacher(AllAnswerScoreDto answer) throws Exception;

    void startExamByTeacher(Exam exam);

    void endExamByTeacher(Exam exam);

    List<StudentScoreOutDto> loadAllScoreOfStudent(Account account);

    List<StudentOutSheetDto> loadStudentAnswersForSheet(StudentAnswerSheet sheet);
}
