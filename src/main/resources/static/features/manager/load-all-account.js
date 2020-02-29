$('#load-pending-account-list').ready(function () {
    loadAllFromDBForManager();
});

var globalData;

function loadAllFromDBForManager() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    jQuery.ajax({
        url: serverUrl() + "/manager/load-all-account",
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
        content += "<td >" + data[i].isEnable + "</td>";
        content += "<td >" + data[i].role + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-outline-warning btn-sm' onclick='showCurrentPersonForEdit(" + i + ")'>edit</button>" +
            "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-outline-danger btn-sm' onclick='showCurrentPersonForUnable(" + i + ")'>unable</button>" +
            "</td>";
        content += "</tr>";
    }
    $('#table-body').html(content);
}

function showCurrentPersonForEdit(data) {

    $("#usernameInput").val(globalData[data].username);
    $("#First-name-input").val(globalData[data].firstName);
    $("#Last-name-input").val(globalData[data].lastName);
    $("#exampleModal").modal('toggle');
}

function showCurrentPersonForUnable(data) {

    $("#usernameInputUnable").val(globalData[data].username);
    $("#unableModal").modal('toggle');
}
//modals actions//
function editAccountForDataBase() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const usernameInput = $("#usernameInput").val();
    const changedFirstName = $("#First-name-input").val();
    const changedLastName = $("#Last-name-input").val();
    const roleName = $("input:checkbox[name='position']:checked").val();
    const roleName1 = $("input:checkbox[name='position2']:checked").val();
    const editCommand = {
        "username": usernameInput,
        "changedFirstName": changedFirstName,
        "changedLastName": changedLastName,
        "role": roleName+roleName1
    }
    jQuery.ajax({
        url: serverUrl() + "/manager/editAccount",
        type: "POST",
        data: JSON.stringify(editCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data.message !== null) {
                showEditMessage(data.message);
                loadAllFromDBForManager()
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
    $("#exampleModal").modal('hide');
}

function unableAccountForDataBase() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const usernameInputUnable = $("#usernameInputUnable").val();
    const unableCommand = {
        "username": usernameInputUnable,
    };
    jQuery.ajax({
        url: serverUrl() + "/manager/unableAccount",
        type: "POST",
        data: JSON.stringify(unableCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, textStatus) {
            if (data.message !== null) {
                showEditMessage(data.message);
                loadAllFromDBForManager()
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong!");
        }
    });
    $("#unableModal").modal('hide');
}

function showEditMessage(message) {
    $("#submit-status-message-alert").html(message);
    $("#submit-status-message-alert").fadeIn().fadeOut(10000);
}