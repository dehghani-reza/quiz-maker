package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.CreateExamDto;
import ir.maktab.quizmaker.dto.CreateSimpleQuestionDto;
import ir.maktab.quizmaker.dto.ExamChangeDto;
import ir.maktab.quizmaker.dto.ExamOutDto;
import ir.maktab.quizmaker.model.Course;
import ir.maktab.quizmaker.model.Exam;
import ir.maktab.quizmaker.model.Score;
import ir.maktab.quizmaker.repositories.CourseRepository;
import ir.maktab.quizmaker.repositories.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExamServiceImpl implements ExamService {

    private ExamRepository examRepository;

    private CourseRepository courseRepository;

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository, CourseRepository courseRepository) {
        this.examRepository = examRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public Exam createExam(CreateExamDto createExamDto) throws Exception {
        Optional<Course> course = courseRepository.findById(Long.valueOf(createExamDto.getCourseId()));
        if(course.isEmpty()) throw new Exception("course not found");
        Exam exam = new Exam(null,
                createExamDto.getExamTitle(),
                createExamDto.getExamExplanation(),
                convertStringToTime(createExamDto.getExamDuration()),
                false,
                null,
                null,
                course.get());
        return examRepository.save(exam);
    }

    @Override
    public List<ExamOutDto> loadAllCourseExam(Course course) throws Exception {
        Optional<Course> courseOptional = courseRepository.findById(course.getCourseId());
        if (courseOptional.isEmpty())throw new Exception("course with this id not found");
        List<Exam> examList = courseOptional.get().getExamList();
        Map<Exam,Float> examTotalScore = new HashMap<>();
        for (int i = 0; i < examList.size(); i++) {
            if (examList.get(i).getScores().isEmpty()){
                examTotalScore.put(examList.get(i), (float) 0);
            }
            float sum = examList.get(i).getScores().stream().map(Score::getPoint).reduce((float) 0,Float::sum);
            examTotalScore.put(examList.get(i),sum);
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
        if(!(examChangeDto.getExamDuration()==null||examChangeDto.getExamDuration().equals(""))) {
            exam.setExamDuration(convertStringToTime(examChangeDto.getExamDuration()));
        }
        Exam save = examRepository.save(exam);
        return new ExamOutDto(save.getExamId(),
                save.getTitle(),
                save.getExplanation(),
                save.getExamDuration().toString(),
                save.getScores().stream().map(Score::getPoint).reduce((float) 0, Float::sum));
    }

    private LocalTime convertStringToTime(String time) throws Exception {
        int timer = Integer.parseInt(time);
        if(timer>=60) {
            return LocalTime.of(1,0,0,0);
        }else if(timer<60&&timer>=5){
            return LocalTime.of(0,timer,0,0);
        }
        throw new Exception("cant convert to valid time for exam");
    }
}
