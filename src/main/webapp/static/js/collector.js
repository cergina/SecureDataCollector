
function registerUser() {
    var email = $("#email").val();
    var verification = $("#verification").val();
    var password = $("#password").val();

    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/register",
        data: {
            email: email,
            verification: verification,
            password: password
        },
        statusCode: {
            201: function(responseObject, textStatus, jqXHR) {
                $(location).attr('href', $SCRIPT_ROOT + "/register-confirm");
            },
            403: function(responseObject, textStatus, jqXHR) {
                window.alert("This email cannot be registered.");
            },
            411: function(responseObject, textStatus, jqXHR) {
                window.alert("Enter a password that is between 6 and 15 characters.");
            },
            417: function(responseObject, textStatus, jqXHR) {
                window.alert("The verification code you entered is incorrect.");
            },
            500: function(responseObject, textStatus, jqXHR) {
                window.alert("Something went wrong with the registration. Contact support.");
            }
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log(textStatus + ": " + jqXHR.status + " " + errorThrown);
        },
        complete: function(jqXHR, textStatus) {
            console.log(textStatus);
        }
    });
}

function loginUser() {
    var email = $("#email").val();
    var password = $("#password").val();
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/login",
        data: {
            email: email,
            password: password
        },
        statusCode: {
            200: function(responseObject, textStatus, jqXHR) {
                $(location).attr('href', $SCRIPT_ROOT + "/index");
            }
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log(textStatus + ": " + jqXHR.status + " " + errorThrown);
        },
        complete: function(jqXHR, textStatus) {
            console.log(textStatus);
        }
    });
}

function preregisterUser() {
    var beforetitle = $("#beforetitle").val();
    var firstname = $("#firstname").val();
    var middlename = $("#middlename").val();
    var lastname = $("#lastname").val();
    var phone = $("#phone").val();
    var email = $("#email").val();
    var permanentresidence = $("#permanentresidence").val();
    var privilege = $("#privilege").val();

    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/admin-register",
        data: {
            beforetitle: beforetitle,
            firstname: firstname,
            middlename: middlename,
            lastname: lastname,
            phone: phone,
            email: email,
            permanentresidence: permanentresidence,
            privilege: privilege
        },
        statusCode: {
            201: function(responseObject, textStatus, jqXHR) {
                $(location).attr('href', $SCRIPT_ROOT + "/admin-register-confirm");
            },
            403: function(responseObject, textStatus, jqXHR) {
                window.alert("User can not be registered.");
            }
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log(textStatus + ": " + jqXHR.status + " " + errorThrown);
        },
        complete: function(jqXHR, textStatus) {
            console.log(textStatus);
        }
    });
}

$(function() {
    console.log("Hello World from static");
});
