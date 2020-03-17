function login() {
    recaptcha();
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
                loginSuccessful(username, password,data.role);
            } else {
                loginFailed();
            }
        },
        error: function (errorMessage) {
            loginFailed();
        }
    });
}

function loginSuccessful(username, password, role) {
    // put username and password in shared values container
    window.authenticatedUsername = username;
    window.authenticatedPassword = password;
    // show desktop
    // this function is in index.js
    showDesktopForm(role);
}

function loginFailed() {
    $("#loginFailedMessage").fadeIn().delay(2500).fadeOut();
}
//*******************************
function signUp() {
    const username = $("#newUsernameInput").val();
    const password = $("#newPasswordInput").val();
    const email = $("#emailAddress").val();
    const firstName = $("#firstName").val();
    const lastName = $("#lastName").val();
    const roleName = $("input:radio[name='position']:checked").val();
    const userSignUpCommand = {
        "username": username,
        "password": password,
        "email": email,
        "firstName": firstName,
        "lastName": lastName,
        "roleName": roleName
    }
    $.ajax({
        url: serverUrl() + "/signUp/data",
        type: "POST",
        data: JSON.stringify(userSignUpCommand),
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus) {
            if (data.message !== null) {
                alert(data.message+"\nplease wait until manager confirm you");
            } else {
                alert("some things went wrong");
            }
        },
        error: function (errorMessage) {
            alert("some things went wrong");
        }
    });
}
//**********************************
function recaptcha() {

    var recaptcha = $("#g-recaptcha-response").val();
    if (recaptcha === "") {
        // alert("Please check the recaptcha");
        // throw DOMException; todo
    }
}