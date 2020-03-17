$('#load-pending-account-list').ready(function () {
    loadCourseFromWindow(window.courseEditingData);
    loadAllTeacher();
    $("#courseSearch").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#table-body tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
    $("#tableSearch").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#modal-add-student-table tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
    // Material Select
    $('.mdb-select').materialSelect({});
});
var globalData;

function loadCourseFromWindow(data) {
    globalData = data;
    let content = '';
    content += "<tr>";
    content += "<th scope='row'>" + data.id + "</th>";
    content += "<td >" + data.courseName + "</td>";
    content += "<td >" + data.startDate + "</td>";
    content += "<td >" + data.endDate + "</td>";
    content += "<td >" + data.teacherName + "</td>";
    content += "<td >" +
        "<button type='button' class='btn btn-outline-info btn-sm' onclick='showEditCourseModal()'>Alter Course (Add Teacher)</button>" +
        "</td>";
    // content += "<td >" +
    //     "<button type='button' class='btn btn-outline-danger btn-sm' onclick='showDeleteCourseModalInEditPage()'>Delete</button>" +
    //     "</td>";
    content += "</tr>";
    $('#table-body').html(content);
}

function showDeleteCourseModalInEditPage() {
    $("#course-delete-idInput").val(globalData.id);
    $("#course-delete-titleInput").val(globalData.courseName);
    $("#course-delete-teacherInput").val(globalData.teacherName);
    $("#deleteCourse").modal('toggle');
}

function showEditCourseModal() {
    $("#course-delete-idInput-edit").val(globalData.id);
    $("#course-delete-titleInput-edit").val(globalData.courseName);
    $("#course-delete-startDateInput-edit").val(globalData.startDate);
    $("#course-delete-endDateInput-edit").val(globalData.endDate);
    $("#exampleFormControlSelect1").val(globalData.teacherName);
    $("#editCourse").modal('toggle');
}

function editCourseByManager() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const courseId = $("#course-delete-idInput-edit").val();
    const courseTitle = $("#course-delete-titleInput-edit").val();
    const startDate = $("#course-delete-startDateInput-edit").val();
    const endDate = $("#course-delete-endDateInput-edit").val();
    const teacherUsername = $("#exampleFormControlSelect1").find(":selected").val();
    const newCourseCommand = {
        "courseId": courseId,
        "courseTitle": courseTitle,
        "startDate": startDate,
        "endDate": endDate,
        "teacherUsername": teacherUsername
    };
    jQuery.ajax({
        url: serverUrl() + "/manager/edit-course",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data !== null) {
                loadCourseFromWindow(data)
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
    $("#editCourse").modal('hide');
}

function loadAllTeacher() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    jQuery.ajax({
        url: serverUrl() + "/manager/load-all-teacher",
        type: "POST",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus, jQxhr) {
            teacherDropDown(data);
        },
        error: function (errorMessage) {
            alert(errorMessage.toString())
        }
    });
}

function teacherDropDown(data) {
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<option value=" + data[i].username + ">" + data[i].firstName + " " + data[i].lastName + "</option>\n"
    }
    $("#exampleFormControlSelect1").html(content);

}

function showCourseStudent() {
    $("#course-students").fadeIn(1000).show();
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const newCourseCommand = {
        "courseId": globalData.id,
    };
    jQuery.ajax({
        url: serverUrl() + "/manager/load-all-course-student",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus, jQxhr) {
            fillStudentTable(data);
            console.table(data);
        },
        error: function (errorMessage) {
            alert(errorMessage.toString())
        }
    });
}
var allCourseStudent;
function fillStudentTable(data) {
    allCourseStudent = data;
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<th scope='row'>" + data[i].firstName + "</th>";
        content += "<td >" + data[i].lastName + "</td>";
        content += "<td >" + data[i].username + "</td>";
        content += "<td id=" + i + ">" + data[i].email + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-outline-danger btn-sm' onclick='deleteStudentFromCourse(\"" + data[i].username + "\")'>Delete</button>" +
            "</td>";
        content += "</tr>";
    }
    $('#student-table').html(content);
}

function showAddStudentModal() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const newCourseCommand = {
        "courseId": globalData.id
    };
    jQuery.ajax({
        url: serverUrl() + "/manager/load-all-student-for-adding-to-course",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data,) {
            fillAddStudentToCourseModal(data);
        },
        error: function (errorMessage) {
            alert(errorMessage.toString())
        }
    });
    $("#addStudentToCourseModal").modal('toggle');
}

function fillAddStudentToCourseModal(data) {
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<th scope='row'>" + data[i].firstName + "</th>";
        content += "<td >" + data[i].lastName + "</td>";
        content += "<td >" + data[i].username + "</td>";
        content += "<td id=" + i + ">" + data[i].email + "</td>";
        // content += "<td >" +
        //     "<button type='button' class='btn btn-outline-info btn-sm' onclick='showEditCourseModal()'>Alter Course (Add Teacher)</button>" +
        //     "</td>";
        content += "<td><input class='form-check-input' type='checkbox' id='" + data[i].username + "' name='student' value='" + data[i].username + "'>";
        content += "<label class='form-check-label' for='" + data[i].username + "'></label></td>";
        content += "</tr>";
    }
    $('#modal-add-student-table').html(content);
}

function addStudentToCourse() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const studentForCourse = [];
    $("input:checkbox[name='student']:checked").each(function (j) {
        studentForCourse[j] = $(this).val();
    });
    const newCourseCommand = {
        "studentForCourse": studentForCourse,
        "courseId": globalData.id
    };
    jQuery.ajax({
        url: serverUrl() + "/manager/add-students-to-course",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data.message !== null) {
                showSubmitMessage(data.message);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
    $("#addStudentToCourseModal").modal('hide');
}
function deleteStudentFromCourse(data) {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const newCourseCommand = {
        "studentUsername": data,
        "courseId": globalData.id
    };
    jQuery.ajax({
        url: serverUrl() + "/manager/delete-students-from-course",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data.message !== null) {
                showSubmitMessage(data.message);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });

    $("#showStudentButton").trigger('click');//todo student list doesnt refresh!!!!
}
function showSubmitMessage(message) {
    $("#submit-status-message-alert").html(message);
    $("#submit-status-message-alert").fadeIn().fadeOut(10000);
}