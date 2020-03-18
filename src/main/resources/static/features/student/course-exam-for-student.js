$(document).ready(function () {
    loadStudentCoursesExam(window.globalCourseForStudent.id);
});

function loadStudentCoursesExam(data) {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const newCourseCommand = {
        "username": username,
        "courseId": data,
    };
    jQuery.ajax({
        url: serverUrl() + "/student/load-all-course-exam",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                fillStudentCourseExamTable(data);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
}

var globalCourseExamForStudent;
function fillStudentCourseExamTable(data) {
    globalCourseExamForStudent = data;
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<td >" + data[i].examTitle + "</td>";
        content += "<td >" + data[i].explanation + "</td>";
        content += "<td id=" + i + ">" + data[i].duration + "</td>";
        content += "<td >" + data[i].totalScore + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-success btn-sm' onclick='startExamByStudent(" + data[i].examId + ")'>شروع آزمون</button>" +
            "</td>";
        content += "</tr>";
    }
    $('#student-exam-list').html(content);
}

function startExamByStudent(data) {
    window.globalExamStartedIdForStudent = data;
    $('#app-content-load').load('features/student/exam-page-for-student.html');
}