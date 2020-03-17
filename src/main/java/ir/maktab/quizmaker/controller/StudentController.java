package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.model.Question;
import ir.maktab.quizmaker.model.StudentAnswerSheet;
import ir.maktab.quizmaker.service.CourseService;
import ir.maktab.quizmaker.service.ExamService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StudentController {

    private final CourseService courseService;
    private final ExamService examService;

    public StudentController(CourseService courseService, ExamService examService) {
        this.courseService = courseService;
        this.examService = examService;
    }

    @PostMapping("/load-all-course")
    private List<CourseOutDto> loadStudentCourses(@RequestBody Account account) {
        return courseService.loadAllCourseForStudent(account);
    }

    @PostMapping("/load-all-course-exam")
    private List<ExamOutDto> loadStudentCourseExam(@RequestBody CourseExamDto courseExamDto) {
        return examService.loadAllCourseExamForStudent(courseExamDto);
    }

    @PostMapping("/start-exam")
    private Set<QuestionOutExamDto> startExamForStudent(@RequestBody StartExamDto startExamDto) {
        return examService.startExamForStudent(startExamDto);
    }

    @PostMapping("/submit-exam-answers")
    private OutMessage submitAnswersToExamByStudent(@RequestBody SubmitAnswersDto submitAnswersDto) {
        int i =examService.submitAnswersToAnswerSheet(submitAnswersDto);
        return new OutMessage(i+"answers submitted");
    }

    @PostMapping("/load-all-scores")
    private List<StudentScoreOutDto> loadAllStudentScoreSheet(@RequestBody Account account) {
       return  examService.loadAllScoreOfStudent(account);
    }

    @PostMapping("/load-student-exam-sheet")
    private List<StudentOutSheetDto> loadAllStudentScores(@RequestBody StudentAnswerSheet sheet) {
        return  examService.loadStudentAnswersForSheet(sheet);
    }
}
