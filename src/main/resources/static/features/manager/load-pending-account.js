$('#load-pending-account-list').ready(function () {
    loadPendingFromDBForManager();
});

var globalData;

function loadPendingFromDBForManager() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    jQuery.ajax({
        url: serverUrl() + "/manager/load-pended-account",
        type: "POST",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus, jQxhr) {
            console.table(data.accountDtoList);
            prepareTable(data);
        },
        error: function (errorMessage) {
            alert(errorMessage)
        }
    });
}

function prepareTable(data) {
    globalData = data;
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<th scope='row'>" + data[i].firstName + "</th>";
        content += "<td >" + data[i].lastName + "</td>";
        content += "<td id=" + i + ">" + data[i].username + "</td>";
        content += "<td >" + data[i].email + "</td>";
        content += "<td >" + data[i].role + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-outline-success btn-sm' onclick='showSubmitIncidentByEmployeeModal(" + i + ")'>فعال کن</button>" +
            "</td>";
        content += "</tr>";
    }
    $('#table-body').html(content);
}

function showSubmitIncidentByEmployeeModal(data) {

    $("#usernameInput").val(globalData[data].username);
    $("#exampleModal").modal('toggle');
}

function submitAccountForDataBase() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const usernameInput = $("#usernameInput").val();
    const roleName = $("input:checkbox[name='position']:checked").val();
    const roleName1 = $("input:checkbox[name='position2']:checked").val();
    const submitCommand = {
        "username": usernameInput,
        "role": roleName+roleName1
    }
    jQuery.ajax({
        url: serverUrl() + "/manager/submit",
        type: "POST",
        data: JSON.stringify(submitCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data.message !== null) {
                showSubmitMessage(data.message);
                loadPendingFromDBForManager()
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong");
        }
    });
    $("#exampleModal").modal('hide');
}

function showSubmitMessage(message) {
    $("#submit-status-message-alert").html(message);
    $("#submit-status-message-alert").fadeIn().fadeOut(10000);
}