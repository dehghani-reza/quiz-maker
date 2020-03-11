package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.model.Course;
import ir.maktab.quizmaker.model.Exam;
import ir.maktab.quizmaker.service.CourseService;
import ir.maktab.quizmaker.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/teacher")
public class TeacherController {

    CourseService courseService;
    ExamService examService;

    @Autowired
    public TeacherController(CourseService courseService, ExamService examService) {
        this.courseService = courseService;
        this.examService = examService;
    }

    @PostMapping("/load-all-course")
    private List<CourseForTeacherDto> loadAllCourseForTeacher(@RequestBody Account account) throws Exception {
        return courseService.loadAllTeacherCourse(account);
    }


    @PostMapping("/add-exam-to-course")
    private OutMessage addExamToCourse(@RequestBody CreateExamDto createExamDto) throws Exception {
        Exam exam = examService.createExam(createExamDto);
        return new OutMessage("exam with id " + exam.getExamId() + " successfully created");
    }

    @PostMapping("/load-all-course-exam")
    private List<ExamOutDto> loadAllCourseExam(@RequestBody Course course) throws Exception {
        return examService.loadAllCourseExam(course);
    }

    @PostMapping("/delete-exam-from-course")
    private OutMessage deleteExamFromCourse(@RequestBody Exam exam) throws Exception {
        examService.deleteExamFromCourse(exam);
        return new OutMessage("exam with id "+exam.getExamId()+" deleted");
    }

    @PostMapping("/edit-exam-to-db")
    private ExamOutDto changeExamToDataBase(@RequestBody ExamChangeDto exam) throws Exception {
        return examService.changeExam(exam);
    }
}
