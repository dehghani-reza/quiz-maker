$(document).ready(function () {
    loadExamFromWindows(window.examDataForTeacher);
});

function loadExamFromWindows(data) {
    console.table(data);
    let content = '';
        content += "<tr>";
        content += "<th scope='row'>" + data.examTitle + "</th>";
        content += "<td >" + data.explanation.substring(0, 30) + "</td>";
        content += "<td >" + data.duration + "</td>";
        content += "<td >" + data.totalScore + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-outline-warning btn-sm' onclick='showEditExamModal()'>Edit Exam</button>" +
            "</td>";
        content += "</tr>";
    $('#exam-info-table').html(content);
}

function backToCoursePage() {
    $('#app-content-load').load('features/teacher/edit-course-by-teacher.html');
}

function showEditExamModal() {
    var data = window.examDataForTeacher;
    $("#exam-edit-titleInput").val(data.examTitle);
    $("#exam-edit-explanationInput").val(data.explanation);
    $("#exam-edit-durationInput").val(data.duration);
    $("#editExamModal").modal('toggle');
}
function editExamFromModalToDB() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const examTitle = $("#exam-edit-titleInput").val();
    const examExplanation = $("#exam-edit-explanationInput").val();
    const examDuration = $("#exam-edit-durationInput").val();

    const newCourseCommand = {
        "examId": window.examDataForTeacher.examId,
        "examTitle": examTitle,
        "examExplanation": examExplanation,
        "examDuration": examDuration,
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/edit-exam-to-db",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data !== null) {
                loadExamFromWindows(data);
                window.examDataForTeacher=data;
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
    $("#editExamModal").modal('hide');
}