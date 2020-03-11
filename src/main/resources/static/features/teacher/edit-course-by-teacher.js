$(document).ready(function () {
    loadCourseFromWindowFotTeacher(window.courseDataForTeacher);
});
var teacherCourseData;

function loadCourseFromWindowFotTeacher(data) {
    teacherCourseData = data;
    let content = '';
    content += "<tr>";
    content += "<th scope='row'>" + data.courseId + "</th>";
    content += "<td >" + data.courseName + "</td>";
    content += "<td >" + data.startDate + "</td>";
    content += "<td >" + data.endDate + "</td>";
    content += "<td >" + data.studentNumber + "</td>";
    content += "<td >" +
        "<button type='button' class='btn btn-primary' onclick='showAddExamTOCourseModal()'>Add exam</button>" +
        "</td>";
    content += "</tr>";
    $('#teacher-courses-table').html(content);

}

function showAddExamTOCourseModal() {
    $("#course-exam-idInput").val(teacherCourseData.courseId);
    $("#course-exam-titleInput").val(teacherCourseData.courseName);
    $("#addExamToCourse").modal('toggle');
}

function addExamToCourseByTeacher() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const courseId = $("#course-exam-idInput").val();
    const examTitle = $("#course-edit-titleInput").val();
    const examExplanation = $("#course-edit-explanationInput").val();
    const examDuration = $("#course-edit-durationInput").val();

    const newCourseCommand = {
        "courseId": courseId,
        "examTitle": examTitle,
        "examExplanation": examExplanation,
        "examDuration": examDuration,
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/add-exam-to-course",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data !== null) {
                showSubmitMessage(data.message);
                showExamListForTeacher()
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
    $("#addExamToCourse").modal('hide');
}

function showExamListForTeacher() {
    $("#exam-list").fadeIn(1000);
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const newCourseCommand = {
        "courseId": teacherCourseData.courseId,
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/load-all-course-exam",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus, jQxhr) {
            fillExamTable(data);
        },
        error: function (errorMessage) {
            alert(errorMessage.toString())
        }
    });
}

var allCourseStudent;

function fillExamTable(data) {
    allCourseStudent = data;
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<th scope='row'>" + data[i].examTitle + "</th>";
        content += "<td >" + data[i].explanation.substring(0, 30) + "</td>";
        content += "<td >" + data[i].duration + "</td>";
        content += "<td id=" + i + ">" + data[i].totalScore + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-outline-info btn-sm' onclick='showExamEditPage(\"" + i + "\")'>Exam Page</button>" +
            "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-outline-danger btn-sm' onclick='deleteExamFromCourse(\"" + data[i].examId + "\")'>DeleteExam</button>" +
            "</td>";
        content += "</tr>";
    }
    $('#exam-table').html(content);
}

function deleteExamFromCourse(data) {
    var r = confirm("Delete the Exam?");
    if (r === true) {
        const username = window.authenticatedUsername;
        const password = window.authenticatedPassword;
        const newCourseCommand = {
            "examId": data,
        };
        jQuery.ajax({
            url: serverUrl() + "/teacher/delete-exam-from-course",
            type: "POST",
            data: JSON.stringify(newCourseCommand),
            contentType: "application/json; charset=utf-8",
            headers: {
                "Authorization": "Basic " + btoa(username + ":" + password)
            },
            success: function (data, textStatus, jQxhr) {
                showSubmitMessage(data.message);
                showExamListForTeacher()
            },
            error: function (errorMessage) {
                alert(errorMessage.toString())
            }
        });
    }
}

function showExamEditPage(data) {
    window.examDataForTeacher = allCourseStudent[data];
    $('#app-content-load').load('features/teacher/edit-exam-by-teacher.html');
}

function showSubmitMessage(message) {
    $("#submit-status-message-alert").html(message);
    $("#submit-status-message-alert").fadeIn().fadeOut(10000);
}