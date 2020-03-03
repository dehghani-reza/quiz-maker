package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.model.Course;
import ir.maktab.quizmaker.model.Student;
import ir.maktab.quizmaker.model.Teacher;
import ir.maktab.quizmaker.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    CourseRepository courseRepository;
    PersonRepository personRepository;
    TeacherRepository teacherRepository;
    StudentRepository studentRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, PersonRepository personRepository,TeacherRepository teacherRepository ,  StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.personRepository = personRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Course createCourseByManager(CourseCreationDto courseCreationDto) throws ParseException {
        LocalDate startDate = convertStringToDate(courseCreationDto.getStartDate(),"yyyy-MM-dd");
        LocalDate endDate = convertStringToDate(courseCreationDto.getEndDate(),"yyyy-MM-dd");

        Course course = new Course(null, startDate,
                courseCreationDto.getCourseTitle(),
                endDate,
                null,
                null);
        return courseRepository.save(course);
    }

    @Override
    public List<CourseOutDto> loadAllCourseForManager() {
        List<Course> courseList = courseRepository.findAll();

        Function<Course, CourseOutDto> function = course -> {
            String teacherLastName;
            if(course.getTeacher()==null){
                teacherLastName = "Not assigned!";
            }else {
                teacherLastName = course.getTeacher().getLastName();
            }
            return new CourseOutDto(course.getCourseId().toString(),
                    course.getStartDate().toString(),
                    course.getEndDate().toString(),
                    course.getCourseTitle(),
                    teacherLastName);
        };

        return courseList.stream().map(function).collect(Collectors.toList());
    }

    @Override
    public void deleteCourseByManager(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public List<TeacherIdAndNameDto> loadAllTeacher() {
        List<Teacher> teacherList = teacherRepository.findAllByAccountNotNull().stream().filter(teacher ->
                teacher.getAccount().isEnabled()).collect(Collectors.toList());
        return teacherList.stream().map(teacher -> new TeacherIdAndNameDto(teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getAccount().getUsername())).collect(Collectors.toList());
    }

    @Override
    public CourseOutDto editCourse(CourseEditDto courseEditDto) throws Exception {
        Optional<Course> course = courseRepository.findById(Long.valueOf(courseEditDto.getCourseId()));
        if(course.isEmpty()) throw new Exception("course with this id does not exist");
        if (!(courseEditDto.getTeacherUsername()==null||courseEditDto.getTeacherUsername().equals(""))){
            Optional<Teacher> byAccount_username = teacherRepository.findByAccount_Username(courseEditDto.getTeacherUsername());
            if(byAccount_username.isEmpty()) throw new Exception("teacher with this id does not exist");
            course.get().setTeacher(byAccount_username.get());
        }
        course.get().setCourseTitle(courseEditDto.getCourseTitle());
        course.get().setStartDate(convertStringToDate(courseEditDto.getStartDate(),"yyyy-MM-dd"));
        course.get().setEndDate(convertStringToDate(courseEditDto.getEndDate(),"yyyy-MM-dd"));
        Course save = courseRepository.save(course.get());
        String teacherName;
        if(save.getTeacher()==null){
            teacherName = "Not assigned!";
        }else {
            teacherName = save.getTeacher().getLastName();
        }
        return new CourseOutDto(save.getCourseId().toString(),
                save.getStartDate().toString(),
                save.getEndDate().toString(),
                save.getCourseTitle(),
                teacherName);
    }

    @Override
    public List<StudentInCourseDto> loadAllCourseStudent(Course course) {
        Optional<Course> byId = courseRepository.findById(course.getCourseId());
        List<Student> studentList = byId.get().getStudentList();
        return studentList.stream().map(student -> new StudentInCourseDto(student.getFirstName(),
                student.getLastName(),
                student.getAccount().getUsername(),
                student.getAccount().getEmail())).collect(Collectors.toList());
    }

    @Override
    public List<StudentInCourseDto> loadAllStudent() {

        List<Student> allByAccountNotNull = studentRepository.findAllByAccountNotNull();
        allByAccountNotNull = allByAccountNotNull.stream().filter(student -> student.getAccount().isEnabled()).collect(Collectors.toList());
        return allByAccountNotNull.stream().map(student -> new StudentInCourseDto(student.getFirstName(),
                student.getLastName(),
                student.getAccount().getUsername(),
                student.getAccount().getEmail())).collect(Collectors.toList());
    }

    private LocalDate convertStringToDate(String date , String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(date, formatter);
    }

}
