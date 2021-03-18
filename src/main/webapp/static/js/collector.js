function buildUser() {
    return {
        email: $("#email").val(),
        firstname: $("#firstname").val(),
        middlename: $("#middlename").val(),
        lastname: $("#lastname").val(),
        phone: $("#phone").val(),
        residence: $("#residence").val()
    };
}

function buildAuth() {
    return {
        user: buildUser(),
        isadmin: $("#isadmin").prop("checked"),
        password: $("#password").val(),
        verificationcode: $("#verificationcode").val()
    };
}

function createUser() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/user",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(buildAuth()),
        statusCode: {
            201: function(responseObject, textStatus, jqXHR) {
                $(':input').val('');
            }
        },
        complete: function(jqXHR, textStatus) {
            alert(jqXHR.responseText);
        }
    });
}

function registerUser() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/register",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(buildAuth()),
        statusCode: {
            200: function(responseObject, textStatus, jqXHR) {
                $(location).attr('href', $SCRIPT_ROOT + "/login");
            }
        },
        complete: function(jqXHR, textStatus) {
            alert(jqXHR.responseText);
        }
    });
}

function loginUser() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/login",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(buildAuth()),
        statusCode: {
            200: function(responseObject, textStatus, jqXHR) {
                $(location).attr('href', $SCRIPT_ROOT + "/index");
            }
        },
        complete: function(jqXHR, textStatus) {
            alert(jqXHR.responseText);
        }
    });
}

$(function() {
    console.log("Hello World from static");
});
