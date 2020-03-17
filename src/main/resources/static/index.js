$(document).ready(function () {

    // load login form first into app-root div
    showLoginForm();
});

// show login form
function showLoginForm() {
    $("#app-root").load('login.html');
}

// show desktop form
function showDesktopForm(data) {
    if(data==="teacher"){
        $("#app-root").load('teacher-desktop.html');
    }
    if(data==="student"){
        $("#app-root").load('student-desktop.html');
    }
    if(data==="manager"){
        $("#app-root").load('desktop.html');
    }
//    todo we can redirect person base on role from here
}


