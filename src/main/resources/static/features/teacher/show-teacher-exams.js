$(document).ready(function () {
    loadAllExamForTeacher();
});

function loadAllExamForTeacher() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;

    const newCourseCommand = {
        "username": username
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/load-all-exam-for-teacher",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                fillAllExamList(data);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
}
var globalAllExamForTeacherManagingExam;
function fillAllExamList(data) {
    globalAllExamForTeacherManagingExam = data;
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<th scope='row'>" + data[i].courseName + "</th>";
        content += "<td >" + data[i].examTitle + "</td>";
        content += "<td >" + data[i].status + "</td>";
        content += "<td id=" + i + ">"+data[i].studentParticipationNumber+" from " +data[i].studentNumber+ "</td>";
        content += "<td >" + data[i].examScore + "</td>";
        content += "<td >" + data[i].averageScore + "</td>";
        content += "<td >" ;
        content += "<div class=\"btn-group\" role=\"group\" aria-label=\"Basic example\">" +
            "<button type='button' class='btn btn-primary btn-sm' onclick='showExamStyleSheets(" + data[i].examId + ")'>Details</button>" +
            "<button type='button' class='btn btn-success btn-sm' onclick='startExamByTeacher(" + data[i].examId + ")'>Start</button>" +
            "<button type='button' class='btn btn-danger btn-sm' onclick='endExamByTeacher(" + data[i].examId + ")'>End</button>" +
            "</div>"+
            "</td>";
        content += "</tr>";
    }
    $('#all-exams-info-table').html(content);
}

function startExamByTeacher(data) {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;

    const newCourseCommand = {
        "examId": data
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/start-exam",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                loadAllExamForTeacher();
                showSubmitMessage(data.message);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
}

function endExamByTeacher(data) {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;

    const newCourseCommand = {
        "examId": data
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/end-exam",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                loadAllExamForTeacher();
                showSubmitMessage(data.message);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
}

function showExamStyleSheets(data) {
    window.examIdForStyleSheetPage = data;
    $('#app-content-load').load('features/teacher/load-exam-style-sheet.html');
}

function showSubmitMessage(message) {
    $("#submit-status-message-alert").html(message);
    $("#submit-status-message-alert").fadeIn().fadeOut(10000);
}