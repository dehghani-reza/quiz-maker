$(document).ready(function () {

    // load login form first into app-root div
    showLoginForm();
});

// show login form
function showLoginForm() {
    $("#app-root").load('login.html');
}

// show desktop form
function showDesktopForm() {
    $("#app-root").load('desktop.html');

//    todo we can redirect person base on role from here
}


