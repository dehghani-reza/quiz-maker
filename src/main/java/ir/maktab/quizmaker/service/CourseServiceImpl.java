package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.CourseCreationDto;
import ir.maktab.quizmaker.dto.CourseOutDto;
import ir.maktab.quizmaker.model.Course;
import ir.maktab.quizmaker.model.Teacher;
import ir.maktab.quizmaker.repositories.CourseRepository;
import ir.maktab.quizmaker.repositories.PersonRepository;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    CourseRepository courseRepository;
    PersonRepository personRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, PersonRepository personRepository) {
        this.courseRepository = courseRepository;
        this.personRepository = personRepository;
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

    private LocalDate convertStringToDate(String date , String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(date, formatter);
    }

}
