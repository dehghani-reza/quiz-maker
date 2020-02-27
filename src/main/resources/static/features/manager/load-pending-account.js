$('#load-pending-account-list').ready(function () {
    loadPendingFromDBForManager();
});

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
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<th scope='row'>" + data[i].firstName + "</th>";
        content += "<td >" + data[i].lastName + "</td>";
        content += "<td >" + data[i].username + "</td>";
        content += "<td >" + data[i].email + "</td>";
        content += "<td >" + data[i].role + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-outline-primary btn-sm' onclick='showSubmitIncidentByEmployeeModal(" + data[i].id + ")'>edit</button>" +
            "</td>";
        content += "</tr>";
    }
    $('#table-body').html(content);
}

function showSubmitIncidentByEmployeeModal(id) {
    $("#exampleModal").modal('toggle');
    $("#modal-body").html(ddddddddddddd);
}

