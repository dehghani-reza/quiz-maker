$(document).ready(function () {
    loadExamFromWindows(window.examDataForTeacher);
    $("#tableSearch").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#question-bank-table tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});

function loadExamFromWindows(data) {
    let content = '';
    content += "<tr>";
    content += "<th scope='row'>" + data.examTitle + "</th>";
    content += "<td >" + data.explanation.substring(0, 30) + "</td>";
    content += "<td >" + data.duration + "</td>";
    content += "<td >" + data.totalScore + "</td>";
    content += "<td >" +
        "<button type='button' class='btn btn-outline-warning btn-sm' onclick='showEditExamModal()'>ویرایش آزمون</button>" +
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
                window.examDataForTeacher = data;
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

function showQuestionOfExamForTeacher() {
    $("#question-list").fadeIn(1000);
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const newCourseCommand = {
        "examId": window.examDataForTeacher.examId,
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/load-all-exam-question",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data !== null) {
                fillQuestionTable(data);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
}

var allQuestionData;

function fillQuestionTable(data) {
    allQuestionData = data;
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<th scope='row'>" + data[i].title + "</th>";
        content += "<td >" + data[i].context.substring(0, 30) + "</td>";
        content += "<td >" + data[i].answer.substring(0, 30) + "</td>";
        content += "<td>" + data[i].score + "</td>";
        content += "<td>" + data[i].type + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-outline-warning btn-sm' onclick='showQuestionScoreEditModal(\"" + i + "\")'>ویرایش</button>" +
            "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-outline-danger btn-sm' onclick='deleteQuestionFromExam(\"" + i + "\")'>حذف</button>" +
            "</td>";
        content += "</tr>";
    }
    $('#question-table').html(content);
}

function deleteQuestionFromExam(data) {
    var r = confirm("سوال حذف شود؟");
    if (r === true) {
        const username = window.authenticatedUsername;
        const password = window.authenticatedPassword;
        const newCourseCommand = {
            "questionId": allQuestionData[data].questionId,
            "examId": window.examDataForTeacher.examId,
        };
        jQuery.ajax({
            url: serverUrl() + "/teacher/delete-question-from-exam",
            type: "POST",
            data: JSON.stringify(newCourseCommand),
            contentType: "application/json; charset=utf-8",
            headers: {
                "Authorization": "Basic " + btoa(username + ":" + password)
            },
            success: function (data) {
                console.table(data);
                window.examDataForTeacher.totalScore = data.examScore;
                loadExamFromWindows(window.examDataForTeacher);
                showSubmitMessage(data.message);
                showExamListForTeacher()
            },
            error: function (errorMessage) {
                alert(errorMessage.toString())
            }
        });
    }
}

function showQuestionScoreEditModal(data) {
    $("#question-edit-idInput").val(allQuestionData[data].questionId);
    $("#question-edit-titleInput").val(allQuestionData[data].title);
    $("#question-edit-ScoreInput").val(allQuestionData[data].score);
    $("#editQuestionModal").modal('toggle');
}

function editQuestionFromModalToDB() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const questionId = $("#question-edit-idInput").val();
    const questionScore = $("#question-edit-ScoreInput").val();

    const newCourseCommand = {
        "examId": window.examDataForTeacher.examId,
        "questionId": questionId,
        "questionScore": questionScore,
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/edit-question-from-exam",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                window.examDataForTeacher.totalScore = data.examScore;
                loadExamFromWindows(window.examDataForTeacher);
                showSubmitMessage(data.message);
                showQuestionOfExamForTeacher();
            } else {
                alert("some things went wrong");
            }
        },
        error: function () {
            alert("some things went wrong!");
        }
    });
    $("#editQuestionModal").modal('hide');
}


function showModalCreateQuestionByTeacher() {
    $("#createQuestion").modal('toggle');
}

function addQuestionByTeacher() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const questionTitle = $("#question-add-titleInput").val();
    const questionContext = $("#question-add-contextInput").val();
    const questionAnswer = $("#question-add-answerInput").val();
    const questionScore = $("#question-add-scoreInput").val();

    const newCourseCommand = {
        "username": window.authenticatedUsername,
        "examId": window.examDataForTeacher.examId,
        "courseId": window.courseDataForTeacher.courseId,
        "questionTitle": questionTitle,
        "questionContext": questionContext,
        "questionAnswer": questionAnswer,
        "questionScore": questionScore,
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/create-simple-question-to-exam",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data !== null) {
                window.examDataForTeacher.totalScore = data.examScore;
                loadExamFromWindows(window.examDataForTeacher);
                showSubmitMessage(data.message);
                showQuestionOfExamForTeacher();
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
    $("#createQuestion").modal('hide');
}

//edit multiple options

var counter = 1;

$("#add-option-to-question").on("click", function () {
    var cols = "";
    cols += '<div class="form-group">';
    cols += '<div style="display: flex; justify-content: space-between">';
    cols += '<label for="optional-question-add-option' + counter + '" class="col-form-label">گزینه سوال:</label>';
    cols += '<input type="button" style="border-radius: 50px" class="ibtnDel btn btn-md btn-danger "  value="حذف">';
    cols += '</div>';
    cols += '<input type="text" class="form-control" name="question-options" id="optional-question-add-option' + counter + '" >';
    cols += '</div>';
    $("#other-options").append(cols);
    counter++;
});


$("#other-options").on("click", ".ibtnDel", function () {
    $(this).closest("div").parent().remove();
    counter -= 1
});


//

function addOptionalQuestionByTeacher() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const questionTitle = $("#optional-question-add-titleInput").val();
    const questionContext = $("#optional-question-add-contextInput").val();
    const questionAnswer = $("#optional-question-add-answerInput").val();
    var questionOption = "";
    $("#optional-question-form").find('input[name="question-options"]').each(function () {
        questionOption += $(this).val()+"&/!/@";
    });
    const questionScore = $("#optional-question-add-scoreInput").val();

    const newCourseCommand = {
        "username": window.authenticatedUsername,
        "examId": window.examDataForTeacher.examId,
        "courseId": window.courseDataForTeacher.courseId,
        "questionTitle": questionTitle,
        "questionContext": questionContext,
        "questionAnswer": questionAnswer,
        "questionOption": questionOption,
        "questionScore": questionScore,
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/create-optional-question-to-exam",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                window.examDataForTeacher.totalScore = data.examScore;
                loadExamFromWindows(window.examDataForTeacher);
                showSubmitMessage(data.message);
                showQuestionOfExamForTeacher();
            } else {
                alert("some things went wrong");
            }
        },
        error: function () {
            alert("some things went wrong!");
        }
    });
    $("#createQuestion").modal('hide');
}

function loadQuestionOfTeacherForExam() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const newCourseCommand = {
        "username": window.authenticatedUsername
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/load-all-question-from-bank",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data,) {
            fillAddQuestionToExamModal(data);
        },
        error: function (errorMessage) {
            alert(errorMessage.toString())
        }
    });
    $("#addQuestionFromBank").modal('toggle');
}

function fillAddQuestionToExamModal(data) {
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<th scope='row'>" + data[i].title + "</th>";
        content += "<td >" + data[i].context + "</td>";
        content += "<td >" + data[i].type + "</td>";
        content += "<td><input class='form-check-input  position-static' style='width: 75px' type='number' step='0.25' id='score" + data[i].questionId + "' name='score' value='" + data[i].score + "'>";
        content += "<label class='form-check-label' for='score" + data[i].questionId + "'></label></td>";
        content += "<td><div class='form-check'><input class='form-check-input position-static' type='checkbox' id='" + data[i].questionId + "' name='question' value='" + data[i].questionId + "'>";
        content += "<label class='form-check-label' for='" + data[i].questionId + "'></label></div></td>";
        content += "</tr>";
    }
    $('#question-bank-table').html(content);
}

function addQuestionToExamFromBankByTeacher() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const questionForExam = [];
    const scoreQuestionForExam = [];
    $("input:checkbox[name='question']:checked").each(function (j) {
        questionForExam[j] = $(this).val();
        scoreQuestionForExam[j] = document.getElementById('score' + questionForExam[j]).value;
    });
    const newCourseCommand = {
        "scoreQuestionForExam": scoreQuestionForExam,
        "questionForExam": questionForExam,
        "examId": window.examDataForTeacher.examId
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/add-question-to-exam-from-bank",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data.message !== null) {
                window.examDataForTeacher.totalScore = data.examScore;
                loadExamFromWindows(window.examDataForTeacher);
                showSubmitMessage(data.message);
                showQuestionOfExamForTeacher();
            } else {
                alert("some things went wrong");
            }
        },
        error: function () {
            alert("some things went wrong!");
        }
    });
    $("#addQuestionFromBank").modal('hide');
}

function showSubmitMessage(message) {
    $("#submit-status-message-alert").html(message);
    $("#submit-status-message-alert").fadeIn().fadeOut(10000);
}