$(document).ready(function(){
    loadAllCourseForTeacher();
});

function loadAllCourseForTeacher() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const newCourseCommand = {
        "username": username,
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/load-all-course",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data.message !== null) {
                fillCourseTable(data);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
}
var globalCourseForTeacher;
function fillCourseTable(data) {
    globalCourseForTeacher = data;
        let content = '';
        for (let i = 0; i < data.length; i++) {
            content += "<tr>";
            content += "<th scope='row'>" + data[i].courseId + "</th>";
            content += "<td >" + data[i].courseName + "</td>";
            content += "<td >" + data[i].startDate + "</td>";
            content += "<td id=" + i + ">" + data[i].endDate + "</td>";
            content += "<td >" + data[i].studentNumber + "</td>";
            content += "<td >" +
                "<button type='button' class='btn btn-primary btn-sm' onclick='showCoursePageForTeacher(" + i + ")'>Course Page</button>" +
                "</td>";
            content += "</tr>";
        }
        $('#teacher-course-list').html(content);
}

function showCoursePageForTeacher(data) {
    window.courseDataForTeacher = globalCourseForTeacher[data];
    $('#app-content-load').load('features/teacher/edit-course-by-teacher.html');
}