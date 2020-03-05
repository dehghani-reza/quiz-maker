package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.dto.CourseForTeacherDto;
import ir.maktab.quizmaker.model.Account;
import ir.maktab.quizmaker.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/teacher")
public class TeacherController {

    CourseService courseService;

    @Autowired
    public TeacherController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/load-all-course")
    private List<CourseForTeacherDto> loadAllCourseForTeacher(@RequestBody Account account) throws Exception {
        return courseService.loadAllTeacherCourse(account);
    }
}
