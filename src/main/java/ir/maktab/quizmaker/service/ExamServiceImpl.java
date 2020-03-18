package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.model.*;
import ir.maktab.quizmaker.model.StudentAnswer;
import ir.maktab.quizmaker.repositories.*;
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
    private QuestionRepository questionRepository;
    private ScoreRepository scoreRepository;
    private StudentAnswerRepository studentAnswerRepository;

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository,
                           CourseRepository courseRepository,
                           PersonRepository personRepository,
                           StudentAnswerSheetRepository sheetRepository,
                           QuestionRepository questionRepository,
                           ScoreRepository scoreRepository,
                           StudentAnswerRepository studentAnswerRepository) {
        this.examRepository = examRepository;
        this.courseRepository = courseRepository;
        this.personRepository = personRepository;
        this.sheetRepository = sheetRepository;
        this.questionRepository = questionRepository;
        this.scoreRepository = scoreRepository;
        this.studentAnswerRepository = studentAnswerRepository;
    }


    @Override
    public Exam createExam(CreateExamDto createExamDto) throws Exception {
        Optional<Course> course = courseRepository.findById(Long.valueOf(createExamDto.getCourseId()));
        if (course.isEmpty()) throw new Exception("دوره یافت نشد");
        Exam exam = new Exam(null,
                createExamDto.getExamTitle(),
                createExamDto.getExamExplanation(),
                convertStringToTime(createExamDto.getExamDuration()),
                false,
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
        if (courseOptional.isEmpty()) throw new Exception("دوره ای با اطلاعات مذکور یافت نشد");
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
        List<Exam> exams = examRepository.findAllByCourse_CourseId(courseExamDto.getCourseId()).stream().filter(Exam::isStarted).filter(exam -> !exam.isEnded()).collect(Collectors.toList());
        Person examiner = personRepository.findByAccount_Username(courseExamDto.getUsername());
        Date now = new Date();
        now.setTime(new Date().getTime());
        List<Exam> doneExam = examiner.getStudentAnswerSheet().stream()
                .filter(sheet ->!checkIfAnswerSheetIsOnTimeBoolean(sheet))
                .map(StudentAnswerSheet::getExam).collect(Collectors.toList());
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
        Optional<StudentAnswerSheet> possibleSheet = sheetRepository.findByExaminer_Account_UsernameAndExam_ExamId(startExamDto.getUsername(), startExamDto.getExamId());
        if(possibleSheet.isEmpty()) {
            Date start = new Date();
            start.setTime(new Date().getTime());
            StudentAnswerSheet sheet = new StudentAnswerSheet(null,
                    start,
                    null,
                    false,
                    false,
                    0,
                    examiner,
                    exam.get(),
                    null);
            sheetRepository.save(sheet);
        }
            Map<Question, Score> questionPoint = new HashMap<>();
            exam.get().getScores().forEach(score -> questionPoint.put(score.getQuestion(), score));
            int hour = exam.get().getExamDuration().getHour();
            int minute = exam.get().getExamDuration().getMinute();
            int second = exam.get().getExamDuration().getSecond();
            int duration = hour * 3600 * 1000 + minute * 60 * 1000 + second * 1000;
            if(possibleSheet.isPresent()){
                Date now = new Date();
                now.setTime(new Date().getTime());
                duration-=(now.getTime()-possibleSheet.get().getCreatedDate().getTime());
            }
        int finalDuration = duration;
        return exam.get().getQuestionList().stream().map(question -> new QuestionOutExamDto(question.getQuestionId(),
                    question.getContext(),
                    questionPoint.get(question).getPoint(),
                    question.getClass().getName().replace("ir.maktab.quizmaker.model.", ""),
                finalDuration,
                    convertStringToQuestionOptions(question))).collect(Collectors.toSet());

    }

    @Override
    public int submitAnswersToAnswerSheet(SubmitAnswersDto submitAnswersDto) {
        Exam exam = examRepository.findById(submitAnswersDto.getExamId()).get();
        Person person = personRepository.findByAccount_Username(submitAnswersDto.getUsername());
        StudentAnswerSheet sheet = sheetRepository.findByExam_ExamIdAndExaminer_PersonId(exam.getExamId(), person.getPersonId());
        studentAnswerRepository.deleteAll(sheet.getStudentAnswers());
        List<StudentAnswer> answers = new ArrayList<>();
        for (int i = 0; i < submitAnswersDto.getAnswers().length; i++) {
            answers.add(new StudentAnswer(null,
                    submitAnswersDto.getAnswers()[i][1],
                    0,
                    false,
                    false,
                    person,
                    exam,
                    questionRepository.findById(Long.valueOf(submitAnswersDto.getAnswers()[i][0])).get(),
                    sheet));
        }
        checkIfAnswerSheetIsOnTime(sheet);
        sheet.setStudentAnswers(answers);
        correctionOfOptionalQuestion(sheet);
        StudentAnswerSheet save = sheetRepository.save(sheet);
        return save.getStudentAnswers().size();
    }

    @Override
    public List<ExamMoreOutDto> loadAllExamForTeacher(Account account) {
        Optional<List<Exam>> exams = examRepository.findAllByCourse_Teacher_Account_Username(account.getUsername());
        if (exams.isEmpty()) return null;
        return exams.get().stream().map(exam -> new ExamMoreOutDto(exam.getExamId(),
                exam.getCourse().getCourseTitle(),
                exam.getTitle(),
                examStatus(exam),
                exam.getCourse().getStudentList().size() + exam.getCourse().getTeachersStudent().size(),
                exam.getStudentAnswerSheetList().size(),
                exam.getScores().stream().map(Score::getPoint).reduce(((float) 0), Float::sum),
                calculateAverageScoreOfExam(exam))).collect(Collectors.toList());
    }

    @Override
    public List<AnswerSheetOutDto> loadAllExamStyleSheetForTeacher(Exam exam) {
        Optional<Exam> currentExam = examRepository.findById(exam.getExamId());
        return currentExam.get().getStudentAnswerSheetList().stream().map(sheet -> new AnswerSheetOutDto(sheet.getAnswerSheetId(),
                sheet.getCreatedDate(),
                sheet.getFilledDate(),
                convertBooleanToString(sheet.isOnTime()),
                convertBooleanToString(sheet.isCalculated()),
                sheet.getFinalScore(),
                sheet.getExaminer().getFirstName() + " " + sheet.getExaminer().getLastName())).collect(Collectors.toList());
    }

    @Override
    public List<StudentAnswersOutDto> loadStudentAnswerForCorrection(StudentAnswerSheet sheet) {
        Optional<StudentAnswerSheet> sheetC = sheetRepository.findById(sheet.getAnswerSheetId());
        return sheetC.get().getStudentAnswers().stream().map(a -> new StudentAnswersOutDto(a.getStudentAnswerId(),
                a.getContext(),
                a.getStudentScore(),
                scoreRepository.findByQuestion_QuestionIdAndExam_ExamId(a.getQuestion().getQuestionId(), sheetC.get().getExam().getExamId()).getPoint(),
                convertBooleanToString(a.isCorrected()),
                convertBooleanToString(a.isTrue()),
                a.getQuestion().getContext(),
                a.getQuestion().getAnswer())).collect(Collectors.toList());
    }

    @Override
    public List<StudentAnswersOutDto> correctOneAnswerByTeacher(StudentAnswerDto answer) throws Exception {
        StudentAnswerSheet studentAnswerSheet = correctOneAnswer(answer);
        return studentAnswerSheet.getStudentAnswers().stream().map(a -> new StudentAnswersOutDto(a.getStudentAnswerId(),
                a.getContext(),
                a.getStudentScore(),
                scoreRepository.findByQuestion_QuestionIdAndExam_ExamId(a.getQuestion().getQuestionId(), studentAnswerSheet.getExam().getExamId()).getPoint(),
                convertBooleanToString(a.isCorrected()),
                convertBooleanToString(a.isTrue()),
                a.getQuestion().getContext(),
                a.getQuestion().getAnswer())).collect(Collectors.toList());
    }

    @Override
    public List<StudentAnswersOutDto> correctAllAnswerByTeacher(AllAnswerScoreDto answer) throws Exception {
        StudentAnswerSheet studentAnswerSheet = null;
        for (int i = 0; i < answer.getScoreQuestionList().length; i++) {
            StudentAnswerDto answer1 = new StudentAnswerDto(Long.valueOf(answer.getScoreQuestionList()[i][0]),answer.getScoreQuestionList()[i][1]);
            studentAnswerSheet = correctOneAnswer(answer1);
            if (studentAnswerSheet.isCalculated()) break;
        }
        StudentAnswerSheet finalStudentAnswerSheet = studentAnswerSheet;
        assert studentAnswerSheet != null;
        return studentAnswerSheet.getStudentAnswers().stream().map(a -> new StudentAnswersOutDto(a.getStudentAnswerId(),
                a.getContext(),
                a.getStudentScore(),
                scoreRepository.findByQuestion_QuestionIdAndExam_ExamId(a.getQuestion().getQuestionId(), finalStudentAnswerSheet.getExam().getExamId()).getPoint(),
                convertBooleanToString(a.isCorrected()),
                convertBooleanToString(a.isTrue()),
                a.getQuestion().getContext(),
                a.getQuestion().getAnswer())).collect(Collectors.toList());
    }

    @Override
    public void startExamByTeacher(Exam exam) {
        Exam exam1 = examRepository.findById(exam.getExamId()).get();
        exam1.setStarted(true);
        exam1.setEnded(false);
        examRepository.save(exam1);
    }

    @Override
    public void endExamByTeacher(Exam exam) {
        Exam exam1 = examRepository.findById(exam.getExamId()).get();
        exam1.setEnded(true);
        examRepository.save(exam1);
    }

    @Override
    public List<StudentScoreOutDto> loadAllScoreOfStudent(Account account) {
        List<StudentAnswerSheet> all = sheetRepository.findAllByExaminer_Account_Username(account.getUsername());
        return all.stream().map(sheet -> new StudentScoreOutDto(sheet.getAnswerSheetId(),
                sheet.getExam().getTitle(),
                convertSheetStatusToString(sheet),
                sheet.getExam().getExplanation(),
                sheet.getFinalScore(),
                calculateAverageScoreOfExam(sheet.getExam()))).collect(Collectors.toList());
    }

    @Override
    public List<StudentOutSheetDto> loadStudentAnswersForSheet(StudentAnswerSheet sheet) {
        StudentAnswerSheet sheet1 = sheetRepository.findById(sheet.getAnswerSheetId()).get();
        if(!sheet1.getExam().isEnded())return null;
        return sheet1.getStudentAnswers().stream().map(answer -> new StudentOutSheetDto(answer.getQuestion().getContext(),
                answer.getQuestion().getAnswer(),
                answer.getContext(),
                convertBooleanToString(answer.isCorrected()),
                scoreRepository.findByQuestion_QuestionIdAndExam_ExamId(answer.getQuestion().getQuestionId(),answer.getExam().getExamId()).getPoint(),
                answer.getStudentScore(),
                studentAnswerRepository.findAllByExam_ExamIdAndQuestion_QuestionId(answer.getExam().getExamId(),answer.getQuestion().getQuestionId())
                        .stream().map(StudentAnswer::getStudentScore).reduce((float)0,(aFloat, aFloat2) -> (aFloat+aFloat2)/2))).collect(Collectors.toList());
    }

    private StudentAnswerSheet correctOneAnswer(StudentAnswerDto answer1) throws Exception {
        if (answer1.getStudentScore() == null || answer1.getStudentScore().equals("") || answer1.getStudentScore().isEmpty()) {
            return studentAnswerRepository.findById(answer1.getStudentAnswerId()).get().getStudentAnswerSheet();
        }
        if (Float.parseFloat(answer1.getStudentScore()) < 0) throw new Exception("نمره منفی مجاز نیست!");
        StudentAnswer byId = studentAnswerRepository.findById(answer1.getStudentAnswerId()).get();
        if (Float.parseFloat(answer1.getStudentScore()) > scoreRepository.findByQuestion_QuestionIdAndExam_ExamId(byId.getQuestion().getQuestionId(), byId.getExam().getExamId()).getPoint())
            throw new Exception("نمره بیش از حد مجاز است!");
        byId.setStudentScore(Float.parseFloat(answer1.getStudentScore()));
        byId.setCorrected(true);
        if (Float.parseFloat(answer1.getStudentScore()) != 0) byId.setTrue(true);
        StudentAnswerSheet studentAnswerSheet = byId.getStudentAnswerSheet();
        studentAnswerSheet.setFinalScore(studentAnswerSheet.getStudentAnswers().stream().map(StudentAnswer::getStudentScore).reduce(((float) 0), Float::sum));
        if (studentAnswerSheet.getStudentAnswers().stream().allMatch(StudentAnswer::isCorrected))
            studentAnswerSheet.setCalculated(true);
        studentAnswerRepository.save(byId);
        return sheetRepository.save(studentAnswerSheet);
    }

    private List<String> convertStringToQuestionOptions(Question question) {
        if (question instanceof OptionalQuestion) {
            String separatorKey = "&/!/@";
            String options = ((OptionalQuestion) question).getOptions();
            List<String> option = new ArrayList<>(Arrays.asList(options.split(separatorKey)));
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
        } else if (timer < 60 && timer >= 1) {
            return LocalTime.of(0, timer, 0, 0);
        }
        throw new Exception("cant convert to valid time for exam");
    }

    private void checkIfAnswerSheetIsOnTime(StudentAnswerSheet studentAnswerSheet) {
        Date createdDate = studentAnswerSheet.getCreatedDate();
        Date finished = new Date();
        finished.setTime(new Date().getTime());
        studentAnswerSheet.setFilledDate(finished);
        LocalTime examDuration = studentAnswerSheet.getExam().getExamDuration();
        int hour = examDuration.getHour();
        int minute = examDuration.getMinute();
        int second = examDuration.getSecond();
        int longTime = second * 1000 + minute * 60 * 1000 + hour * 3600 * 1000 + (12 * 1000);
        if (finished.getTime() - createdDate.getTime() > longTime) {
            studentAnswerSheet.setOnTime(false);
        } else {
            studentAnswerSheet.setOnTime(true);
        }
    }

    private boolean checkIfAnswerSheetIsOnTimeBoolean(StudentAnswerSheet studentAnswerSheet) {
        Date createdDate = studentAnswerSheet.getCreatedDate();
        Date now = new Date();
        now.setTime(new Date().getTime());
        LocalTime examDuration = studentAnswerSheet.getExam().getExamDuration();
        int hour = examDuration.getHour();
        int minute = examDuration.getMinute();
        int second = examDuration.getSecond();
        int longTime = second * 1000 + minute * 60 * 1000 + hour * 3600 * 1000 + (12 * 1000);
        if (now.getTime() - createdDate.getTime() > longTime) {
            return false;
        } else {
            return true;
        }
    }

    private void correctionOfOptionalQuestion(StudentAnswerSheet sheet) {
        if (sheet.isCalculated()) return;
        List<StudentAnswer> studentOptionalAnswers = sheet.getStudentAnswers().stream()
                .filter(studentAnswer -> studentAnswer.getQuestion() instanceof OptionalQuestion).collect(Collectors.toList());
        for (StudentAnswer studentAnswer : studentOptionalAnswers) {
            String answer = studentAnswer.getQuestion().getAnswer();
            if (answer.equals(studentAnswer.getContext())) {
                studentAnswer.setTrue(true);
                studentAnswer.setCorrected(true);
                Score score = scoreRepository.findByQuestion_QuestionIdAndExam_ExamId(studentAnswer.getQuestion().getQuestionId(), studentAnswer.getExam().getExamId());
                studentAnswer.setStudentScore(score.getPoint());
            } else {
                studentAnswer.setTrue(false);
                studentAnswer.setCorrected(true);
                studentAnswer.setStudentScore(0);
            }
            Float finalScore = studentOptionalAnswers.stream().map(StudentAnswer::getStudentScore).reduce(((float) 0), Float::sum);
            sheet.setFinalScore(finalScore);
            if (studentOptionalAnswers.size() == sheet.getStudentAnswers().size()) {
                sheet.setCalculated(true);
            }
        }
    }

    private double calculateAverageScoreOfExam(Exam exam) {
        List<StudentAnswerSheet> studentAnswerSheetList = exam.getStudentAnswerSheetList();
        double sum = studentAnswerSheetList.stream().map(StudentAnswerSheet::getFinalScore).reduce(((double) 0), Double::sum);
        return (sum / exam.getStudentAnswerSheetList().size());
    }

    private String convertBooleanToString(boolean b) {
        if (b) return "Yes";
        return "No";
    }

    private String examStatus(Exam exam){
        if(exam.isEnded())return "Finished";
        if(!exam.isStarted()) return "Not Started";
        if(!exam.isEnded()) return "On Going";
        return null;
    }

    private String convertSheetStatusToString(StudentAnswerSheet sheet){
        if(sheet.isCalculated()) return "corrected";
        return "waiting";
    }
}
