package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.model.*;
import ir.maktab.quizmaker.service.CourseService;
import ir.maktab.quizmaker.service.ExamService;
import ir.maktab.quizmaker.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/teacher")
public class TeacherController {

    CourseService courseService;
    ExamService examService;
    QuestionService questionService;

    @Autowired
    public TeacherController(CourseService courseService, ExamService examService, QuestionService questionService) {
        this.courseService = courseService;
        this.examService = examService;
        this.questionService = questionService;
    }

    @PostMapping("/load-all-course")
    private List<CourseForTeacherDto> loadAllCourseForTeacher(@RequestBody Account account) throws Exception {
        return courseService.loadAllTeacherCourse(account);
    }


    @PostMapping("/add-exam-to-course")
    private OutMessage addExamToCourse(@RequestBody CreateExamDto createExamDto) throws Exception {
        Exam exam = examService.createExam(createExamDto);
        return new OutMessage("آزمون با شناسه" + exam.getExamId() + "اضافه شد");
    }

    @PostMapping("/load-all-course-exam")
    private List<ExamOutDto> loadAllCourseExam(@RequestBody Course course) throws Exception {
        return examService.loadAllCourseExam(course);
    }

    @PostMapping("/delete-exam-from-course")
    private OutMessage deleteExamFromCourse(@RequestBody Exam exam) throws Exception {
        examService.deleteExamFromCourse(exam);
        return new OutMessage("آزمون با شناسه " + exam.getExamId() + " حذف شد");
    }

    @PostMapping("/edit-exam-to-db")
    private ExamOutDto changeExamToDataBase(@RequestBody ExamChangeDto exam) throws Exception {
        return examService.changeExam(exam);
    }

    @PostMapping("/create-simple-question-to-exam")
    private OutMessageWithExamScoreDto createSimpleQuestionByTeacher(@RequestBody CreateSimpleQuestionDto questionDto) throws Exception {
        float v = questionService.createSimpleQuestion(questionDto);
        return new OutMessageWithExamScoreDto("سوال با موفقیت ساخته شد", v);
    }

    @PostMapping("/create-optional-question-to-exam")
    private OutMessageWithExamScoreDto createOptionalQuestionByTeacher(@RequestBody CreateOptionalQuestionDto questionDto) throws Exception {
        float v = questionService.createOptionalQuestion(questionDto);
        return new OutMessageWithExamScoreDto("سوال با موفقیت ساخته شد", v);
    }

    @PostMapping("/load-all-exam-question")
    private List<QuestionOutDto> loadAllQuestionOfEXam(@RequestBody Exam exam) throws Exception {
        return questionService.loadAllQuestionOfExam(exam);
    }

    @PostMapping("/edit-question-from-exam")
    private OutMessageWithExamScoreDto editQuestionFromExam(@RequestBody QuestionChangeExamDto question) throws Exception {
        float v = questionService.editQuestionFromExam(question);
        return new OutMessageWithExamScoreDto("نمره سوال تغییر یافت", v);
    }

    @PostMapping("/delete-question-from-exam")
    private OutMessageWithExamScoreDto deleteQuestionFromExam(@RequestBody QuestionChangeExamDto question) throws Exception {
        float v = questionService.deleteQuestionFromExam(question);
        return new OutMessageWithExamScoreDto("سوال از آزمون حذف شد", v);
    }

    @PostMapping("/start-exam")
    private OutMessage startExamByTeacher(@RequestBody Exam exam) throws Exception {
       examService.startExamByTeacher(exam);
        return new OutMessage("آزمون آغاز شد");
    }

    @PostMapping("/end-exam")
    private OutMessage endExamByTeacher(@RequestBody Exam exam) throws Exception {
        examService.endExamByTeacher(exam);
        return new OutMessage("آزمون خاتمه یافت");
    }

    @PostMapping("/load-all-question-from-bank")
    private List<QuestionOutDto> loadAllTeacherQuestionFromBank(@RequestBody Account account) throws Exception {
        return questionService.loadAllTeacherQuestion(account);
    }

    @PostMapping("/add-question-to-exam-from-bank")
    private OutMessageWithExamScoreDto addQuestionToExamFromBank(@RequestBody QuestionIdScoreDto questionIdScoreDto) throws Exception {
        float v = questionService.addQuestionFromBankToExam(questionIdScoreDto);
        return new OutMessageWithExamScoreDto("سوال اضافه شد",v);
    }


    @PostMapping("/load-all-exam-for-teacher")
    private List<ExamMoreOutDto> loadAllExamForTeacher(@RequestBody Account account) throws Exception {
        return examService.loadAllExamForTeacher(account);
    }

    @PostMapping("/load-all-exam-style-sheet")
    private List<AnswerSheetOutDto> loadAllExamStyleSheetForTeacher(@RequestBody Exam exam) throws Exception {
        return examService.loadAllExamStyleSheetForTeacher(exam);
    }

    @PostMapping("/load-student-exam-Answers")
    private List<StudentAnswersOutDto> loadStudentAnswersForCorrection(@RequestBody StudentAnswerSheet sheet) throws Exception {
        return examService.loadStudentAnswerForCorrection(sheet);
    }

    @PostMapping("/set-score-for-one-student-Answers")
    private List<StudentAnswersOutDto> correctOneStudentAnswer(@RequestBody StudentAnswerDto answer) throws Exception {
        return examService.correctOneAnswerByTeacher(answer);
    }

    @PostMapping("/set-score-for-all-student-Answers")
    private List<StudentAnswersOutDto> correctAllStudentAnswer(@RequestBody AllAnswerScoreDto answer) throws Exception {
        return examService.correctAllAnswerByTeacher(answer);
    }

    @PostMapping("/load-all-question-bank")
    private List<QuestionOutDto> loadQuestionBank(@RequestBody Account account) throws Exception {
        return questionService.loadQuestionFromBank(account);
    }

    @PostMapping("/load-optional-question-for-change")
    private QuestionOutExamDto loadOptionalQuestionForChange(@RequestBody Question question) throws Exception {
        return questionService.loadOptionalQuestionForChange(question);
    }

    @PostMapping("/change-optional-question")
    private OutMessage changeOptionalQuestionByTeacher(@RequestBody ChangeOptionalQuestionDto question) throws Exception {
        questionService.changeOptionalQuestionByTeacher(question);
        return new OutMessage("question successfully changed");
    }

    @PostMapping("/change-simple-question")
    private OutMessage changeSimpleQuestionByTeacher(@RequestBody ChangeSimpleQuestionDto question) throws Exception {
        questionService.changeSimpleQuestionByTeacher(question);
        return new OutMessage("question successfully changed");
    }
}
