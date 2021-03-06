package ir.maktab.quizmaker.service;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.model.*;
import ir.maktab.quizmaker.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    CourseRepository courseRepository;
    TeacherRepository teacherRepository;
    ExamRepository examRepository;
    QuestionRepository questionRepository;
    ScoreRepository scoreRepository;

    @Autowired
    public QuestionServiceImpl(CourseRepository courseRepository,
                               TeacherRepository teacherRepository,
                               ExamRepository examRepository,
                               QuestionRepository questionRepository,
                               ScoreRepository scoreRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.scoreRepository = scoreRepository;
    }

    @Override
    public float createSimpleQuestion(CreateSimpleQuestionDto questionDto) {
        Course course = courseRepository.findById(questionDto.getCourseId()).get();
        Exam exam = examRepository.findById(questionDto.getExamId()).get();
        Teacher teacher = teacherRepository.findByAccount_Username(questionDto.getUsername()).get();
        Score score = new Score();
        SimpleQuestion question = new SimpleQuestion();
        score.setExam(exam);
        score.setPoint(questionDto.getQuestionScore());
        score.setQuestion(question);
        question.setAnswer(questionDto.getQuestionAnswer());
        question.setContext(questionDto.getQuestionContext());
        question.setTitle(questionDto.getQuestionTitle());
        question.setUsernameCreator(teacher);
        question.setCourseCreatedId(course);
        question.getScoreList().add(score);
        exam.getQuestionList().add(question);
        questionRepository.save(question);
        scoreRepository.save(score);
        return calculateExamTotalScore(exam);
    }

    @Override
    public float createOptionalQuestion(CreateOptionalQuestionDto questionDto) {
        Course course = courseRepository.findById(questionDto.getCourseId()).get();
        Exam exam = examRepository.findById(questionDto.getExamId()).get();
        Teacher teacher = teacherRepository.findByAccount_Username(questionDto.getUsername()).get();
        Score score = new Score();
        OptionalQuestion question = new OptionalQuestion();
        score.setExam(exam);
        score.setPoint(questionDto.getQuestionScore());
        score.setQuestion(question);
        question.setAnswer(questionDto.getQuestionAnswer());
        question.setContext(questionDto.getQuestionContext());
        question.setTitle(questionDto.getQuestionTitle());
        question.setUsernameCreator(teacher);
        question.setCourseCreatedId(course);
        question.setOptions(questionDto.getQuestionOption());
        question.getScoreList().add(score);
        exam.getQuestionList().add(question);
        questionRepository.save(question);
        scoreRepository.save(score);
        return calculateExamTotalScore(exam);
    }

    @Override
    public List<QuestionOutDto> loadAllQuestionOfExam(Exam exam) {
        Exam byId = examRepository.findById(exam.getExamId()).get();
        Set<Question> questionList = byId.getQuestionList();
        Map<Question, Score> questionScoreExam = new HashMap<>();
        byId.getScores().forEach(score -> questionScoreExam.put(score.getQuestion(), score));
        return questionList.stream().map(question -> new QuestionOutDto(question.getQuestionId(),
                question.getContext(),
                question.getAnswer(),
                question.getTitle(),
                questionScoreExam.get(question).getPoint(),
                convertQuestionTypeToString(question))).collect(Collectors.toList());
    }

    @Override
    public float editQuestionFromExam(QuestionChangeExamDto question) throws Exception {
        if(examRepository.findById(question.getExamId()).get().getStudentAnswerSheetList().size()!=0)throw new Exception("بعد از شروع آزمون نمی توانید نمره آزمون را تغییر دهید");
        Score score = scoreRepository.findByQuestion_QuestionIdAndExam_ExamId(question.getQuestionId(), question.getExamId());
        score.setPoint(question.getQuestionScore());
        scoreRepository.save(score);
        return calculateExamTotalScore(examRepository.findById(question.getExamId()).get());
    }

    @Override
    public float deleteQuestionFromExam(QuestionChangeExamDto question) {
        Score score = scoreRepository.findByQuestion_QuestionIdAndExam_ExamId(question.getQuestionId(), question.getExamId());
        Exam exam = examRepository.findById(question.getExamId()).get();
        Question question1 = questionRepository.findById(question.getQuestionId()).get();
        exam.getQuestionList().remove(question1);
        exam.getScores().remove(score);
        examRepository.save(exam);
        scoreRepository.delete(score);
        return calculateExamTotalScore(exam);
    }

    @Override
    public List<QuestionOutDto> loadAllTeacherQuestion(Account account) {
        Teacher teacher = teacherRepository.findByAccount_Username(account.getUsername()).get();
        List<Question> questionList = teacher.getQuestionList();

        return questionList.stream().map(question -> new QuestionOutDto(question.getQuestionId(),
                question.getContext(),
                question.getAnswer(),
                question.getTitle(),
                suggestQuestionScore(question.getScoreList()),
                convertQuestionTypeToString(question))).collect(Collectors.toList());
    }

    @Override
    public float addQuestionFromBankToExam(QuestionIdScoreDto questionIdScoreDto) throws Exception {
        if (questionIdScoreDto.getScoreQuestionForExam().size() != questionIdScoreDto.getQuestionForExam().size())
            throw new Exception("ورودی غلط");
        Exam exam = examRepository.findById(questionIdScoreDto.getExamId()).get();
        List<Long> oldExamQuestion = exam.getQuestionList().stream().map(Question::getQuestionId).collect(Collectors.toList());
        Map<Long, Float> questionIdScore = new HashMap<>();
        for (int i = 0; i < questionIdScoreDto.getQuestionForExam().size(); i++) {
            questionIdScore.put(questionIdScoreDto.getQuestionForExam().get(i), questionIdScoreDto.getScoreQuestionForExam().get(i));
        }
        //remove duplicate question
        questionIdScoreDto.getQuestionForExam().removeAll(oldExamQuestion);
        for (Long aLong : oldExamQuestion) {
            questionIdScore.remove(aLong);
        }
        if(questionIdScore.size()==0){
            return calculateExamTotalScore(exam);
        }
        //
        List<Question> questions = questionRepository.findAllByQuestionIdIn(questionIdScoreDto.getQuestionForExam()).get();
        exam.getQuestionList().addAll(questions);
        List<Score> scoreList = new ArrayList<>();
        for (int i = 0; i < questionIdScore.size(); i++) {
            Question q = questions.get(i);
            float point = questionIdScore.get(q.getQuestionId());
            scoreList.add(new Score(null, point, q, exam));
        }
        exam.getScores().addAll(scoreList);
        examRepository.save(exam);
        scoreRepository.saveAll(scoreList);
        return calculateExamTotalScore(exam);
    }

    @Override
    public List<QuestionOutDto> loadQuestionFromBank(Account account) {
        Optional<Teacher> teacher = teacherRepository.findByAccount_Username(account.getUsername());
        return teacher.get().getQuestionList().stream().map(a->new QuestionOutDto(a.getQuestionId(),
                a.getContext(),
                a.getAnswer(),
                a.getTitle(),
                a.getScoreList().stream().map(Score::getPoint).reduce((float) 0,(aFloat, aFloat2) -> (aFloat+aFloat2)/2),
                convertQuestionTypeToString(a))).collect(Collectors.toList());
    }

    @Override
    public QuestionOutExamDto loadOptionalQuestionForChange(Question question) {
        Question question1 = questionRepository.findById(question.getQuestionId()).get();
        return new QuestionOutExamDto(null,null,0,null,0,convertStringToQuestionOptions(question1));
    }

    @Override
    public void changeOptionalQuestionByTeacher(ChangeOptionalQuestionDto question) {
        OptionalQuestion question1 = (OptionalQuestion) questionRepository.findById(question.getQuestionId()).get();
        question1.setTitle(question.getQuestionTitle());
        question1.setContext(question.getQuestionContext());
        question1.setAnswer(question.getQuestionAnswer());
        question1.setOptions(question.getQuestionOption());
        questionRepository.save(question1);
    }

    @Override
    public void changeSimpleQuestionByTeacher(ChangeSimpleQuestionDto question) {
        SimpleQuestion question1 = (SimpleQuestion) questionRepository.findById(question.getQuestionId()).get();
        question1.setTitle(question.getQuestionTitle());
        question1.setContext(question.getQuestionContext());
        question1.setAnswer(question.getQuestionAnswer());
        questionRepository.save(question1);
    }

    private float calculateExamTotalScore(Exam exam) {
        return exam.getScores().stream().map(Score::getPoint).reduce((float) 0, Float::sum);
    }

    private float suggestQuestionScore(List<Score> scoreList) {
        if (scoreList.isEmpty()) {
            return 0;
        } else if (scoreList.size() == 1) {
            return Math.round(scoreList.get(0).getPoint()*4)/((float)4);
        }
        float val = scoreList.stream().map(Score::getPoint).reduce((aFloat, aFloat2) -> (aFloat + aFloat2) / 2).get();
        return Math.round(val*4)/((float)4);
    }

    private List<String> convertStringToQuestionOptions(Question question) {
        if (question instanceof OptionalQuestion) {
            String separatorKey = "&/!/@";
            String options = ((OptionalQuestion) question).getOptions();
            List<String> option = new ArrayList<>(Arrays.asList(options.split(separatorKey)));
            return option;
        }
        return null;
    }

    private String convertQuestionTypeToString(Question q){
        String replace = q.getClass().getName().replace("ir.maktab.quizmaker.model.", "");
        if(replace.equals("OptionalQuestion"))return "چند گزینه ای";
        if(replace.equals("SimpleQuestion"))return "تشریحی";
        return "";
    }
}
