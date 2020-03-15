$(document).ready(function () {
    startExamForStudent(window.globalExamStartedIdForStudent);
});

function startExamForStudent(data) {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const newCourseCommand = {
        "username": username,
        "examId": data,
        "startTime": new Date().getTime()
    };
    jQuery.ajax({
        url: serverUrl() + "/student/start-exam",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                createExamForm(data);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
}

var globalExamQuestionForStudent;

function createExamForm(data) {
    globalExamQuestionForStudent = data;
    let content = '';
    // startTimer(data[0].duration);
    for (let i = 0; i < data.length; i++) {
        if (data[i].type === "SimpleQuestion") {
            content += "<p style='margin: 0px'>" + (i + 1) + ") " + data[i].context + " (score: " + data[i].score + ")" + "</p>";
            content += "<div style='padding-left: 50px; height: 100px'>";
            content += "<textarea rows=\"4\" cols=\"50\" class='form-check-input' placeholder='youre answer' type='text' id='answer" + data[i].questionId + "' name='answer" + data[i].questionId + "'></textarea>";
            content += "</div>"
        } else if (data[i].type === "OptionalQuestion") {
            content += "<p>" + (i + 1) + ") " + data[i].context + " (score: " + data[i].score + ")" + "</p>";
            content += "<div style='padding-left: 20px'>";
            for (let j = 0; j < data[i].options.length; j++) {
                content += "<input type=\"radio\" value='" + data[i].options[j] + "' id='answer" + data[i].questionId +"' name='answer" + data[i].questionId + "'>";
                content += "<label for='question" + i + "option" + j + "'>" + data[i].options[j] + "</label><br>"
            }
            content += "</div>";
        }
        content += "<br>";
        content += "<hr>";
    }
    $('#exam-question').html(content);
}

function submitAnswersByStudent() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    var answers = [];
    for (let i = 0; i < globalExamQuestionForStudent.length; i++) {
        var idCreated = "answer" + globalExamQuestionForStudent[i].questionId;
        if(document.getElementById(idCreated).tagName==='INPUT'){
            var input = document.querySelector('input[name=' + idCreated + ']:checked');
            if(input===null){
                answers[i] = [globalExamQuestionForStudent[i].questionId,null];
            }else {
                answers[i] = [globalExamQuestionForStudent[i].questionId, input.value];
            }
        }else if(document.getElementById(idCreated).tagName==='TEXTAREA'){
            answers[i] = [globalExamQuestionForStudent[i].questionId,document.getElementById(idCreated).value];
        }

    }

    const newCourseCommand = {
        "username": username,
        "answers": answers,
        "examId": window.globalExamStartedIdForStudent,
        "endTime": new Date().getTime()
    };
    jQuery.ajax({
        url: serverUrl() + "/student/submit-exam-answers",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data.message !== null) {
                alert(data.message);
                $('#app-content-load').load('features/student/course-exam-for-student.html');
            } else {
                alert("some things went wrong");
            }
        },
        error: function () {
            alert("some things went wrong!");
        }
    });
}

function startTimer(Duration) {
    var nowDay = new Date().getTime();
    var countDownDate = new Date(nowDay + Duration).getTime();
    var x = setInterval(function () {
        var now = new Date().getTime();
        var distance = countDownDate - now;
        var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((distance % (1000 * 60)) / 1000);
        document.getElementById("timer").innerHTML = hours + "h "
            + minutes + "m " + seconds + "s ";
        if (distance < 60 * 1000) {
            $("#timer").css("color", "darkred").fadeOut().fadeIn(200);
        }
        if (distance < 0) {
            clearInterval(x);
            $("#timer").innerHTML = "EXPIRED";
        }
        if (distance < -10000) {
            submitAnswersByStudent();
        }
        if ($("#timer") === null) {
            clearInterval(x);
        }
    }, 1000);

}
