function login() {
    const username = $("#usernameInput").val();
    const password = $("#passwordInput").val();
    const userAuthenticationCommand = {
        "username": username,
        "password": password
    }
    $.ajax({
        url: serverUrl() + "/user/login",
        type: "POST",
        data: JSON.stringify(userAuthenticationCommand),
        /*beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
        },*/
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus) {
            if (data.isAuthenticated === true) {
                loginSuccessful(username, password);
            } else {
                loginFailed();
            }
        },
        error: function (errorMessage) {
            loginFailed();
        }
    });
}

function loginSuccessful(username, password) {
    // put username and password in shared values container
    window.authenticatedUsername = username;
    window.authenticatedPassword = password;
    // show desktop
    // this function is in index.js
    showDesktopForm();
}

function loginFailed() {
    $("#loginFailedMessage").fadeIn().delay(5000).fadeOut();
}
