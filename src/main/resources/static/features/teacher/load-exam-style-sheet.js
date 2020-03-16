$(document).ready(function () {
    loadExamStyleSheet(window.examIdForStyleSheetPage)
});

function loadExamStyleSheet(data) {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;

    const newCourseCommand = {
        "examId": data
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/load-all-exam-style-sheet",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                fillExamAnswersSheet(data);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
}

function fillExamAnswersSheet(data) {
    globalAllExamForTeacherManagingExam = data;
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<th scope='row'>" + data[i].examinerName + "</th>";
        content += "<td >" + data[i].createdDate.substring(0,19) + "</td>";
        content += "<td >" + data[i].isOnTime.toString()+ "</td>";
        content += "<td >" + data[i].isCalculated + "</td>";
        content += "<td >" + data[i].finalScore + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-primary btn-sm' onclick='showAnswersOfStudent(" + data[i].sheetId + ")'>Details</button>" +
            "</td>";
        content += "</tr>";
    }
    $('#all-exams-answers-sheet-table').html(content);

}

function backToExamPage() {
    $('#app-content-load').load('features/teacher/show-teacher-exams.html');
}

function showAnswersOfStudent(data) {
    window.sheetIdForCorectionByTeacher = data;
    $('#app-content-load').load('features/teacher/show-student-answers.html')

}