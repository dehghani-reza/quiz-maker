$(document).ready(function () {
    loadStudentAnswers(window.sheetIdForCorectionByTeacher)
});

function loadStudentAnswers(data) {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;

    const newCourseCommand = {
        "answerSheetId": data
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/load-student-exam-Answers",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                fillStudentAnswersSheet(data);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
}

var answersGlobalData;

function fillStudentAnswersSheet(data) {
    answersGlobalData = data;
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr class='"+makeRowRed(data[i])+"'>";
        content += "<th scope='row'>" + data[i].questionContent + "</th>";
        content += "<td data-toggle='tooltip' data-placement='top' title='" + data[i].questionAnswer + "'>" + data[i].questionAnswer.substring(0, 19) + "</td>";
        content += "<td data-toggle='tooltip' data-placement='top' title='" + data[i].context + "'>" + data[i].context + "</td>";
        content += "<td >" + data[i].isCorrected + "</td>";
        content += "<td > <input step='0.25' min='0' type='number' max='" + data[i].questionScore + "' id='" + data[i].answerId + "' class='form-control' placeholder='حداکثر:" + data[i].questionScore.toString() + "'></td>";
        content += "<td >" + data[i].studentScore + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-success btn-sm' onclick='setScore(" + data[i].answerId + ")'>ذخیره نمره</button>" +
            "</td>";
        content += "</tr>";
    }
    content += "<tr>";
    content += "<th scope='row'>جمع کل:</th>";
    content += "<td colspan='6'>" + calculateTotalScore(answersGlobalData) + "</td>";
    content += "</tr>";
    $('#all-answers-from-sheet').html(content);

}

function setScore(data) {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const score = document.getElementById(data).value;

    const newCourseCommand = {
        "studentAnswerId": data,
        "studentScore": score
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/set-score-for-one-student-Answers",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                fillStudentAnswersSheet(data);
            } else {
                alert("خطایی رخ داده");
            }
        },
        error: function (errorMessage) {
            alert(errorMessage.responseJSON.message);
        }
    });
}

function setAllScore() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    var scoreQuestionList = [];
    for (let i = 0; i < answersGlobalData.length; i++) {
        scoreQuestionList[i] = [answersGlobalData[i].answerId, document.getElementById(answersGlobalData[i].answerId).value]
    }
    const newCourseCommand = {
        "username": username,
        "scoreQuestionList": scoreQuestionList
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/set-score-for-all-student-Answers",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                $('#app-content-load').load('features/teacher/load-exam-style-sheet.html');
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
}

function calculateTotalScore(data) {
    var addition = 0;
    for (let i = 0; i < data.length; i++) {
        addition += data[i].studentScore;
    }
    return addition;
}

function makeRowRed(data) {
    if (data.isCorrected === "Yes") {
        return null;
    } else return "bg-danger";
}

function backToSheetPage() {
    $('#app-content-load').load('features/teacher/load-exam-style-sheet.html');
}