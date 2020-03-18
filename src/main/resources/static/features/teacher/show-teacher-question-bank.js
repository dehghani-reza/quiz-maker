$(document).ready(function () {
    loadAllQuestionFromBank();
    $("#tableSearch").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#all-exams-question-bank tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});

function loadAllQuestionFromBank() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const newCourseCommand = {
        "username": username,
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/load-all-question-bank",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data !== null) {
                fillQuestionBank(data)
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert(errorMessage.responseJSON.message);
        }
    });
}

var globalQuestionBank;

function fillQuestionBank(data) {
    globalQuestionBank = data;
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<th scope='row'>" + data[i].questionId + "</th>";
        content += "<td >" + data[i].context + "</td>";
        content += "<td >" + data[i].answer + "</td>";
        content += "<td id=" + i + ">" + data[i].title + "</td>";
        content += "<td >" + data[i].score + "</td>";
        content += "<td >" + data[i].type + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-warning btn-sm' onclick='toggleChangeModal(" + i + ")'>تغییر</button>" +
            "</td>";
        content += "</tr>";
    }
    $('#all-exams-question-bank').html(content);
}

function toggleChangeModal(data) {
    var current = globalQuestionBank[data];
    if (current.type === "تشریحی") {
        $("#changed-question-id").val(current.questionId);
        $("#changed-question-answer").val(current.answer);
        $("#changed-question-title").val(current.title);
        $("#changed-question-context").val(current.context);
        $("#simple-question-modal-change").modal('toggle');
    } else{
        $("#changed-optional-question-id").val(current.questionId);
        $("#changed-optional-question-answer").val(current.answer);
        $("#changed-optional-question-title").val(current.title);
        $("#changed-optional-question-context").val(current.context);
        bringOptionalQuestionFromDbToChange(current.questionId);
        $("#changeOptionalQuestion").modal('toggle');
    }

}
var counter;
function bringOptionalQuestionFromDbToChange(dara) {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const newCourseCommand = {
        "questionId": dara,
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/load-optional-question-for-change",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data !== null) {
                counter=data.options.length;
                $('#optional-question-add-option0Input').val(data.options[0]);
                var cols = "";
                for (let i = 0; i < ((data.options.length)-1); i++) {
                    cols += '<div class="form-group">';
                    cols += '<div style="display: flex; justify-content: space-between">';
                    cols += '<label for="optional-question-add-option'+(i+1)+'" class="col-form-label">گزینه سوال:</label>';
                    cols += '<input type="button" style="border-radius: 50px" class="ibtnDel btn btn-md btn-danger "  value="حذف">';
                    cols += '</div>';
                    cols += '<input type="text" class="form-control" name="question-options" id="optional-question-add-option' +(i+1)+ '" value="'+data.options[i+1]+'">';
                    cols += '</div>';
                }
                $("#other-changing-options").html(cols);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert(errorMessage.responseJSON.message);
        }
    });
}
$("#other-changing-options").on("click", ".ibtnDel", function () {
    $(this).closest("div").parent().remove();
    counter -= 1
});
$("#add-option-to-question").on("click", function () {
    var cols = "";
    cols += '<div class="form-group">';
    cols += '<div style="display: flex; justify-content: space-between">';
    cols += '<label for="optional-question-add-option' + counter + '" class="col-form-label">گزینه سوال:</label>';
    cols += '<input type="button" style="border-radius: 50px" class="ibtnDel btn btn-md btn-danger "  value="حذف">';
    cols += '</div>';
    cols += '<input type="text" class="form-control" name="question-options" id="optional-question-add-option' + counter + '" >';
    cols += '</div>';
    $("#other-changing-options").append(cols);
    counter++;
});


function changeOptionalQuestionByTeacher() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const questionTitle = $("#changed-optional-question-title").val();
    const questionContext = $("#changed-optional-question-context").val();
    const questionAnswer = $("#changed-optional-question-answer").val();
    var newQuestionOption = "";
    $("#optional-question-form").find('input[name="question-options"]').each(function () {
        newQuestionOption += $(this).val()+"&/!/@";
    });
    const questionId = $("#changed-optional-question-id").val();

    const newCourseCommand = {
        "questionTitle": questionTitle,
        "questionContext": questionContext,
        "questionAnswer": questionAnswer,
        "questionOption": newQuestionOption,
        "questionId": questionId,
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/change-optional-question",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                loadAllQuestionFromBank();
                showSubmitMessage(data.message)
            } else {
                alert("some things went wrong");
            }
        },
        error: function () {
            alert("some things went wrong!");
        }
    });
    $("#changeOptionalQuestion").modal('hide');
}


function changeSimpleQuestion() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const questionTitle = $("#changed-question-title").val();
    const questionContext = $("#changed-question-context").val();
    const questionAnswer = $("#changed-question-answer").val();
    const questionId = $("#changed-question-id").val();

    const newCourseCommand = {
        "questionTitle": questionTitle,
        "questionContext": questionContext,
        "questionAnswer": questionAnswer,
        "questionId": questionId,
    };
    jQuery.ajax({
        url: serverUrl() + "/teacher/change-simple-question",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data !== null) {
               loadAllQuestionFromBank();
               showSubmitMessage(data.message)
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
    $("#simple-question-modal-change").modal('hide');
}

function showSubmitMessage(message) {
    $("#submit-status-message-alert").html(message);
    $("#submit-status-message-alert").fadeIn().fadeOut(10000);
}