$(document).ready(function () {
    loadStudentAnswersForStudent(window.studentAnswerSheetIdForDetain)
});
function loadStudentAnswersForStudent(data) {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;

    const newCourseCommand = {
        "answerSheetId": data
    };
    jQuery.ajax({
        url: serverUrl() + "/student/load-student-exam-sheet",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                fillStudentAnswersSheetForStudent(data);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
}

function fillStudentAnswersSheetForStudent(data) {
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr class='"+makeRowRed(data[i])+"'>";
        content += "<th scope='row'>" + data[i].questionContext + "</th>";
        content += "<td data-toggle='tooltip' data-placement='top' title='" + data[i].questionAnswer + "'>" + data[i].questionAnswer.substring(0, 19) + "</td>";
        content += "<td data-toggle='tooltip' data-placement='top' title='" + data[i].studentAnswer + "'>" + data[i].studentAnswer + "</td>";
        content += "<td >" + data[i].isCorrected + "</td>";
        content += "<td >" + data[i].score+ "</td>";
        content += "<td >" + data[i].studentScore + "</td>";
        content += "<td >" + data[i].averageScore + "</td>";
        content += "</tr>";
    }
    content += "<tr>";
    content += "<th scope='row'>TotalScore:</th>";
    content += "<td colspan='6'>" + calculateTotalScore(data)+"/"+calculateTotalExamScore(data) + "</td>";
    content += "</tr>";
    $('#all-answers-from-sheet-for-student').html(content);

}
function calculateTotalScore(data) {
    var addition = 0;
    for (let i = 0; i < data.length; i++) {
        addition += data[i].studentScore;
    }
    return addition;
}

function calculateTotalExamScore(data) {
    var addition = 0;
    for (let i = 0; i < data.length; i++) {
        addition += data[i].score;
    }
    return addition;
}

function makeRowRed(data) {
    if (data.isCorrected === "Yes") {
        return null;
    } else return "bg-danger";
}