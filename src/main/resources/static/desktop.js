$(".app-content").ready(function () {
    // receive first login message
    receiveFirstLoginMessage();
    $(".desktop-side-bar-menu").click(function () {

        const submenu = $(this).next();
        submenu.slideToggle(500);
        submenu.siblings("ul").hide(500);
    });
    // side bar menu clicked
    loadAccountStatus();
});

function receiveFirstLoginMessage() {
    // receive username and password from shared values window container
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    // create command
    const command = {"whoIAm": username};
    $.ajax({
        url: serverUrl() + "/message/first-login",
        type: "POST",
        data: JSON.stringify(command),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, status) {
            showFirstMessage(data.message);
        },
        error: function (errorMessage) {
            alert("Some thing wrong happened!")
        }
    });
}

var AccountDataaas;
function loadAccountStatus() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    // create command
    const command = {"whoIAm": username};
    $.ajax({
        url: serverUrl() + "/manager/load-account-status",
        type: "POST",
        data: JSON.stringify(command),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data, status) {
            AccountDataaas = data;
            google.charts.load("current", {packages:["corechart"]});
            google.charts.setOnLoadCallback(drawChart);
            function drawChart() {
                var data = google.visualization.arrayToDataTable( fillTheChartStatus(AccountDataaas));
                var options = {
                    title: 'وضعیت حساب ها',
                    pieHole: 0.6,
                };

                var chart = new google.visualization.PieChart(document.getElementById('donutchart'));
                chart.draw(data, options);
            }

        },
        error: function (errorMessage) {
            alert("Some thing wrong happened!")
        }
    });
}


function fillTheChartStatus(data) {
    var char =  [['وضعیت','حساب']];
    for (let i = 0; i <data.length ; i++) {
        char.push([data[i].condition, data[i].numberAccount]);
    }
    return char;
}


function showFirstMessage(message) {
    $("#first-login-message-alert").html(message);
    $("#first-login-message-alert").fadeIn(2000).fadeOut(10000);
}

function loadPage(page) {
    if (page === 'load-pending-account') {
        $('#app-content-load').load('features/manager/load-pending-account.html');
    }
    if(page==='load-all-account'){
        $('#app-content-load').load('features/manager/load-all-account.html');
    }
    if(page==='show-add-course-by-manager'){
        $('#app-content-load').load('features/manager/show-add-course-by-manager.html');
    }
    if(page==='show-teacher-courses'){
        $('#app-content-load').load('features/teacher/show-teacher-courses.html');
    }
    if(page==='show-teacher-exams'){
        $('#app-content-load').load('features/teacher/show-teacher-exams.html');
    }
    if(page==='show-student-courses'){
        $('#app-content-load').load('features/student/show-student-courses.html');
    }
}

window.onbeforeunload = function(e) {
    return 'Bye now!';
};