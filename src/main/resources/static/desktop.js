$(".app-content").ready(function () {

    // receive first login message
    receiveFirstLoginMessage();

    // side bar menu clicked
    $(".desktop-side-bar-menu").click(function () {
        const submenu = $(this).next();
        submenu.slideToggle(500);
        submenu.siblings("ul").hide(500);
    });
})

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
}
