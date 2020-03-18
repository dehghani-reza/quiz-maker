$(document).ready(function () {
    loadStudentCourses(window.courseDataForTeacher);
});

function loadStudentCourses() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const newCourseCommand = {
        "username": username,
    };
    jQuery.ajax({
        url: serverUrl() + "/student/load-all-course",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                fillStudentCourseTable(data);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert(errorMessage.responseJSON.message);
        }
    });
}
var globalCourseForStudent;
function fillStudentCourseTable(data) {
    globalCourseForStudent = data;
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<th scope='row'>" + data[i].id + "</th>";
        content += "<td >" + data[i].courseName + "</td>";
        content += "<td >" + data[i].startDate + "</td>";
        content += "<td id=" + i + ">" + data[i].endDate + "</td>";
        content += "<td >" + data[i].teacherName + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-primary btn-sm' onclick='showCourseExamsForStudent(" + i + ")'>آزمون های دوره</button>" +
            "</td>";
        content += "</tr>";
    }
    $('#student-course-list').html(content);
}

function showCourseExamsForStudent(data) {
    window.globalCourseForStudent = globalCourseForStudent[data];
    $('#app-content-load').load('features/student/course-exam-for-student.html');
}
