$(document).ready(function () {
    loadStudentCourses(window.courseDataForTeacher);
});

function loadStudentCourses() {
    const username = window.authenticatedUsername;
    const password = window.authenticatedPassword;
    const newCourseCommand = {
        "username": username,
    };
    jQuery.ajax({
        url: serverUrl() + "/student/load-all-scores",
        type: "POST",
        data: JSON.stringify(newCourseCommand),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        success: function (data) {
            if (data !== null) {
                fillStudentScoreList(data);
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert(errorMessage.responseJSON.message);
        }
    });
}

var studentGlobalScoresData;

function fillStudentScoreList(data) {
    studentGlobalScoresData = data;
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<th scope='row'>" + data[i].examTitle + "</th>";
        content += "<td >" + data[i].condition + "</td>";
        content += "<td data-toggle=\"tooltip\" data-placement=\"bottom\" title=" + data[i].examExplanation + ">" + data[i].examExplanation.substring(0, 20) + "</td>";
        content += "<td id=" + i + ">" + data[i].yourScore + "</td>";
        content += "<td >" + data[i].averageScore + "</td>";
        content += "<td >" +
            "<button type='button' class='btn btn-primary btn-sm' onclick='loadAnswerSheetForStudent(" + i + ")'>جزئیات</button>" +
            "</td>";
        content += "</tr>";
    }
    $('#student-scores-list').html(content);
}

function loadAnswerSheetForStudent(data) {
    window.studentAnswerSheetIdForDetain = studentGlobalScoresData[data].sheetId;
    $('#app-content-load').load('features/student/sheet-exam-for-student.html');
}

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    var data = google.visualization.arrayToDataTable(
        fillTheChart(studentGlobalScoresData)
    );

    var options = {
        title: 'نگاه کلی به آزمون ها',
        hAxis: {title: 'عنوان آزمون',  titleTextStyle: {color: '#333'}},
        vAxis: {minValue: 0}
    };

    var chart = new google.visualization.AreaChart(document.getElementById('chart_div'));
    chart.draw(data, options);
}

function fillTheChart(data) {
    var char =  [['عنوان آزمون', 'نمره شما', 'میانگین نمرات']];
    for (let i = 0; i <data.length ; i++) {
        char.push([data[i].examTitle, data[i].yourScore,data[i].averageScore]);
    }
    return char;
}

