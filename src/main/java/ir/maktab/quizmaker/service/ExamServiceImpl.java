package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.model.*;
import ir.maktab.quizmaker.repositories.CourseRepository;
import ir.maktab.quizmaker.repositories.ExamRepository;
import ir.maktab.quizmaker.repositories.PersonRepository;
import ir.maktab.quizmaker.repositories.StudentAnswerSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExamServiceImpl implements ExamService {

    private ExamRepository examRepository;
    private CourseRepository courseRepository;
    private PersonRepository personRepository;
    private StudentAnswerSheetRepository sheetRepository;

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository,
                           CourseRepository courseRepository,
                           PersonRepository personRepository,
                           StudentAnswerSheetRepository sheetRepository ) {
        this.examRepository = examRepository;
        this.courseRepository = courseRepository;
        this.personRepository = personRepository;
        this.sheetRepository = sheetRepository;
    }


    @Override
    public Exam createExam(CreateExamDto createExamDto) throws Exception {
        Optional<Course> course = courseRepository.findById(Long.valueOf(createExamDto.getCourseId()));
        if (course.isEmpty()) throw new Exception("course not found");
        Exam exam = new Exam(null,
                createExamDto.getExamTitle(),
                createExamDto.getExamExplanation(),
                convertStringToTime(createExamDto.getExamDuration()),
                false,
                null,
                null,
                course.get(),
                null,
                null);
        return examRepository.save(exam);
    }

    @Override
    public List<ExamOutDto> loadAllCourseExam(Course course) throws Exception {
        Optional<Course> courseOptional = courseRepository.findById(course.getCourseId());
        if (courseOptional.isEmpty()) throw new Exception("course with this id not found");
        List<Exam> examList = courseOptional.get().getExamList();
        Map<Exam, Float> examTotalScore = new HashMap<>();
        for (int i = 0; i < examList.size(); i++) {
            if (examList.get(i).getScores().isEmpty()) {
                examTotalScore.put(examList.get(i), (float) 0);
            }
            float sum = examList.get(i).getScores().stream().map(Score::getPoint).reduce((float) 0, Float::sum);
            examTotalScore.put(examList.get(i), sum);
        }
        return examList.stream().map(exam -> new ExamOutDto(exam.getExamId(),
                exam.getTitle(),
                exam.getExplanation(),
                exam.getExamDuration().toString(),
                examTotalScore.get(exam))).collect(Collectors.toList());
    }

    @Override
    public void deleteExamFromCourse(Exam exam) {
        examRepository.deleteById(exam.getExamId());
    }

    @Override
    public ExamOutDto changeExam(ExamChangeDto examChangeDto) throws Exception {
        Exam exam = examRepository.findById(Long.valueOf(examChangeDto.getExamId())).get();
        exam.setTitle(examChangeDto.getExamTitle());
        exam.setExplanation(examChangeDto.getExamExplanation());
        if (!(examChangeDto.getExamDuration() == null || examChangeDto.getExamDuration().equals(""))) {
            exam.setExamDuration(convertStringToTime(examChangeDto.getExamDuration()));
        }
        Exam save = examRepository.save(exam);
        return new ExamOutDto(save.getExamId(),
                save.getTitle(),
                save.getExplanation(),
                save.getExamDuration().toString(),
                save.getScores().stream().map(Score::getPoint).reduce((float) 0, Float::sum));
    }

    @Override
    public List<ExamOutDto> loadAllCourseExamForStudent(CourseExamDto courseExamDto) {
        List<Exam> exams = examRepository.findAllByCourse_CourseId(courseExamDto.getCourseId());
        Person examiner = personRepository.findByAccount_Username(courseExamDto.getUsername());
        List<Exam> doneExam = examiner.getStudentAnswerSheet().stream().map(StudentAnswerSheet::getExam).collect(Collectors.toList());
        exams.removeAll(doneExam);
        return exams.stream().map(exam -> new ExamOutDto(exam.getExamId(),
                exam.getTitle(),
                exam.getExplanation(),
                exam.getExamDuration().toString(),
                exam.getScores().stream().map(Score::getPoint).reduce(((float) 0), Float::sum))).collect(Collectors.toList());
    }

    @Override
    public Set<QuestionOutExamDto> startExamForStudent(StartExamDto startExamDto) {
        Person examiner = personRepository.findByAccount_Username(startExamDto.getUsername());
        Optional<Exam> exam = examRepository.findById(startExamDto.getExamId());
        Date start = new Date();
        start.setTime(new Date().getTime());
        StudentAnswerSheet sheet = new StudentAnswerSheet(null,
                start,
                null,
                false,
                0,
                examiner,
                exam.get(),
                null);
//        sheetRepository.save(sheet);todo
        Map<Question,Score> questionPoint = new HashMap<>();
        exam.get().getScores().forEach(score -> questionPoint.put(score.getQuestion(), score));
        int hour = exam.get().getExamDuration().getHour();
        int minute = exam.get().getExamDuration().getMinute();
        int second = exam.get().getExamDuration().getSecond();
        return exam.get().getQuestionList().stream().map(question -> new QuestionOutExamDto(question.getQuestionId(),
                question.getContext(),
                questionPoint.get(question).getPoint(),
                question.getClass().getName().replace("ir.maktab.quizmaker.model.", ""),
                hour*3600*1000+minute*60*1000+second*1000,
                convertStringToQuestionOptions(question))).collect(Collectors.toSet());
    }

    @Override
    public int submitAnswersToAnswerSheet(SubmitAnswersDto submitAnswersDto) {
        System.out.println(submitAnswersDto);
        return 1;
    }

    private List<String> convertStringToQuestionOptions(Question question){
        if(question instanceof OptionalQuestion){
            String separatorKey = "&/!/@";
            String options = ((OptionalQuestion) question).getOptions();
            List<String> option = new ArrayList<>(Arrays.asList(options.split(separatorKey)));
            System.out.println(question.getAnswer());
            option.add(question.getAnswer());
            Collections.shuffle(option);
            return option;
        }
        return null;
    }

    private LocalTime convertStringToTime(String time) throws Exception {
        int timer = Integer.parseInt(time);
        if (timer >= 60) {
            return LocalTime.of(1, 0, 0, 0);
        } else if (timer < 60 && timer >= 5) {
            return LocalTime.of(0, timer, 0, 0);
        }
        throw new Exception("cant convert to valid time for exam");
    }
}
