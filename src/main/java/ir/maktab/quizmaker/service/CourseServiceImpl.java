package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.model.*;
import ir.maktab.quizmaker.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    CourseRepository courseRepository;
    PersonRepository personRepository;
    TeacherRepository teacherRepository;
    StudentRepository studentRepository;
    AccountRepository accountRepository;
    RoleRepository roleRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             PersonRepository personRepository,
                             TeacherRepository teacherRepository,
                             StudentRepository studentRepository,
                             AccountRepository accountRepository,
                             RoleRepository roleRepository) {
        this.courseRepository = courseRepository;
        this.personRepository = personRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Course createCourseByManager(CourseCreationDto courseCreationDto) throws Exception {
        LocalDate startDate = convertStringToDate(courseCreationDto.getStartDate(), "yyyy-MM-dd");
        LocalDate endDate = convertStringToDate(courseCreationDto.getEndDate(), "yyyy-MM-dd");

        if(startDate.isAfter(endDate)) throw new Exception("start date cant be after end date");

        Course course = new Course(null, startDate,
                endDate,
                courseCreationDto.getCourseTitle(),
                null,
                null);
        course.setEnable(true);
        return courseRepository.save(course);
    }

    @Override
    public List<CourseOutDto> loadAllCourseForManager() {
        List<Course> courseList = courseRepository.findAll();

        Function<Course, CourseOutDto> function = course -> {
            String teacherLastName;
            if (course.getTeacher() == null) {
                teacherLastName = "Not assigned!";
            } else {
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
        if (course.isEmpty()) throw new Exception("course with this id does not exist");
        if (!(courseEditDto.getTeacherUsername() == null || courseEditDto.getTeacherUsername().equals(""))) {
            Optional<Teacher> byAccount_username = teacherRepository.findByAccount_Username(courseEditDto.getTeacherUsername());
            if (byAccount_username.isEmpty()) throw new Exception("teacher with this id does not exist");
            course.get().setTeacher(byAccount_username.get());
        }
        course.get().setCourseTitle(courseEditDto.getCourseTitle());
        course.get().setStartDate(convertStringToDate(courseEditDto.getStartDate(), "yyyy-MM-dd"));
        course.get().setEndDate(convertStringToDate(courseEditDto.getEndDate(), "yyyy-MM-dd"));
        Course save = courseRepository.save(course.get());
        String teacherName;
        if (save.getTeacher() == null) {
            teacherName = "Not assigned!";
        } else {
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
        List<StudentInCourseDto> studentInCourseDtos = new LinkedList<>();
        if (!studentList.isEmpty()) {
            studentInCourseDtos = studentList.stream().map(student -> new StudentInCourseDto(student.getFirstName(),
                    student.getLastName(),
                    student.getAccount().getUsername(),
                    student.getAccount().getEmail())).collect(Collectors.toList());
        }
        if (!byId.get().getTeachersStudent().isEmpty()) {
            studentInCourseDtos.addAll(byId.get().getTeachersStudent().stream().map(teacher -> new StudentInCourseDto(teacher.getFirstName(),
                    teacher.getLastName(),
                    teacher.getAccount().getUsername(),
                    teacher.getAccount().getEmail())).collect(Collectors.toList()));
        }
        return studentInCourseDtos;
    }

    @Override
    public List<StudentInCourseDto> loadAllStudentForAddingToCourse(CourseEditDto courseEditDto) {
        Optional<Course> course = courseRepository.findById(Long.valueOf(courseEditDto.getCourseId()));
        Optional<Role> studentRole = roleRepository.findByRoleName(RoleName.ROLE_STUDENT);
        if (studentRole.isPresent() && course.isPresent()) {
            List<Person> students = studentRole.get().getAccountList().stream()
                    .filter(Account::isEnabled)
                    .map(Account::getPerson)
                    .filter(person -> !checkIfPersonHasThisCourse(person, course.get())).collect(Collectors.toList());
            return students.stream().map(student -> new StudentInCourseDto(student.getFirstName(),
                    student.getLastName(),
                    student.getAccount().getUsername(),
                    student.getAccount().getEmail())).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Course addStudentToCourse(StudentListAssignForCourseDto students) throws Exception {
        Optional<Course> course = courseRepository.findById(Long.valueOf(students.getCourseId()));
        if (course.isEmpty()) throw new Exception("course with this id has been deleted");
        List<Person> people = students.getStudentForCourse().stream()
                .map(username -> accountRepository.findByUsername(username))
                .map(Account::getPerson)
                .filter(person -> !checkIfPersonHasThisCourse(person,course.get())).collect(Collectors.toList());

        List<Teacher> teacher = people.stream()
                .filter(person -> person instanceof Teacher)
                .map(person -> (Teacher)person).collect(Collectors.toList());

        List<Student> student = people.stream()
                .filter(person -> person instanceof Student)
                .map(person -> (Student)person).collect(Collectors.toList());

       course.get().getStudentList().addAll(student);
       course.get().getTeachersStudent().addAll(teacher);
        return courseRepository.save(course.get());

    }

    @Override
    public void deleteStudentFromCourse(StudentDeleteFromCourseDto studentCourse) throws Exception {
        Optional<Course> course = courseRepository.findById(Long.valueOf(studentCourse.getCourseId()));
        if(course.isEmpty())throw new Exception("this course does not exist");
        Account account = accountRepository.findByUsername(studentCourse.getStudentUsername());
        Person person = account.getPerson();
        if(person instanceof Teacher){
            course.get().getTeachersStudent().remove(person);
        }
        if (person instanceof Student){
            course.get().getStudentList().remove(person);
        }
        courseRepository.save(course.get());
    }

    @Override
    public List<CourseForTeacherDto> loadAllTeacherCourse(Account account) throws Exception {
        Optional<Teacher> teacher = teacherRepository.findByAccount_Username(account.getUsername());
        if(teacher.isEmpty()) throw new Exception("can't find teacher with this user name");
        Optional<List<Course>> courseList = courseRepository.findAllByTeacher(teacher.get());
        if(courseList.isEmpty()) throw new Exception("you dont have any course");
        return courseList.get().stream().map(course -> new CourseForTeacherDto(String.valueOf(course.getCourseId()),
                course.getCourseTitle(),
                String.valueOf(course.getStartDate()),
                String.valueOf(course.getEndDate()),
                String.valueOf(course.getStudentList().size()+course.getTeachersStudent().size()))).collect(Collectors.toList());
    }

    @Override
    public List<CourseOutDto> loadAllCourseForStudent(Account account) {
        Person username = personRepository.findByAccount_Username(account.getUsername());
        if(username instanceof Student) {
            List<Course> courseList = ((Student) username).getCourseList();
            if (courseList.isEmpty()) return null;
            return courseList.stream().map(course -> new CourseOutDto(course.getCourseId().toString(),
                    course.getStartDate().toString(),
                    course.getEndDate().toString(),
                    course.getCourseTitle(),
                    course.getTeacher().getLastName())).collect(Collectors.toList());
        }else if(username instanceof Teacher){
            List<Course> courseList = ((Teacher) username).getStudentInCourseList();
            return courseList.stream().map(course -> new CourseOutDto(course.getCourseId().toString(),
                    course.getStartDate().toString(),
                    course.getEndDate().toString(),
                    course.getCourseTitle(),
                    "Mr."+course.getTeacher().getLastName())).collect(Collectors.toList());
        }
        return null;
    }

    private boolean checkIfPersonHasThisCourse(Person person, Course course) {
        if (person instanceof Teacher) {
            return (((Teacher) person).getStudentInCourseList().contains(course));
        }
        if (person instanceof Student) {
            return ((Student) person).getCourseList().contains(course);
        }
        return false;
    }

    private LocalDate convertStringToDate(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(date, formatter);
    }

}
