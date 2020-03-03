$('#load-pending-account-list').ready(function () {
    loadAllCourseForManager();
    $("#courseSearch").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#table-body tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});

var globalData;

function loadAllCourseForManager() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    jQuery.ajax({
        url: serverUrl() + "/manager/load-all-course",
        type: "POST",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus, jQxhr) {
            console.table(data.accountDtoList);
            prepareCourseTable(data);
        },
        error: function (errorMessage) {
            alert(errorMessage)
        }
    });
}

function prepareCourseTable(data) {
    globalData = data;
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<th scope='row'>" + data[i].id + "</th>";
        content += "<td >" + data[i].courseName + "</td>";
        content += "<td >" + data[i].startDate + "</td>";
        content += "<td id=" + i + ">" + data[i].endDate + "</td>";
        content += "<td >" + data[i].teacherName + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-primary btn-sm' onclick='showEditCoursePage(" + i + ")'>Course Page</button>" +
            "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-outline-danger btn-sm' onclick='showDeleteCourseModal(" + i + ")'>Delete</button>" +
            "</td>";
        content += "</tr>";
    }
    $('#table-body').html(content);
}

function addCourseByManager() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const courseTitle = $("#course-created-title").val();
    const startDate = $("#course-created-start").val();
    const endDate = $("#course-created-end").val();
    const newCourseCommand = {
        "courseTitle": courseTitle,
        "startDate": startDate,
        "endDate": endDate,
    }
    jQuery.ajax({
        url: serverUrl() + "/manager/create-course",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data.message !== null) {
                showEditMessage(data.message);
                loadAllCourseForManager();
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
    $("#createCourse").modal('hide');

}

function deleteCourseByManager() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const courseId = $("#course-delete-idInput").val();
    const newCourseCommand = {
        "courseId": courseId,
    }
    jQuery.ajax({
        url: serverUrl() + "/manager/delete-course",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data.message !== null) {
                showEditMessage(data.message);
                loadAllCourseForManager();
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
    $("#deleteCourse").modal('hide');

}

function showCourseCreateModalForm() {
    $("#createCourse").modal('toggle');
}

function showDeleteCourseModal(data) {
    $("#course-delete-idInput").val(globalData[data].id);
    $("#course-delete-titleInput").val(globalData[data].courseName);
    $("#course-delete-teacherInput").val(globalData[data].teacherName);
    $("#deleteCourse").modal('toggle');
}

function showEditCoursePage(data) {
    window.courseEditingData = globalData[data];
    $('#app-content-load').load('features/manager/edit-course-by-manager.html');
}
function showEditMessage(message) {
    $("#submit-status-message-alert").html(message);
    $("#submit-status-message-alert").fadeIn().fadeOut(10000);
}


