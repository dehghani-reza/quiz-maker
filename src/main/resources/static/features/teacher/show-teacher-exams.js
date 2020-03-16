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
        content += "<td >" + data[i].studentNumber + "</td>";
        content += "<td id=" + i + ">" + data[i].studentParticipationNumber + "</td>";
        content += "<td >" + data[i].examScore + "</td>";
        content += "<td >" + data[i].averageScore + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-primary btn-sm' onclick='showExamStyleSheets(" + data[i].examId + ")'>Details</button>" +
            "</td>";
        content += "</tr>";
    }
    $('#all-exams-info-table').html(content);
}

function showExamStyleSheets(data) {
    window.examIdForStyleSheetPage = data;
    $('#app-content-load').load('features/teacher/load-exam-style-sheet.html');
}