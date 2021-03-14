
function registerUser() {
    var email = $("#email").val();
    var firstname = $("#first-name").val();
    var password = $("#password").val();

    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/register",
        data: {
            email: email,
            firstname: firstname,
            password: password
        },
        statusCode: {
            201: function(responseObject, textStatus, jqXHR) {
                $(location).attr('href', $SCRIPT_ROOT + "/login");
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

$(function() {
    console.log("Hello World from static");
});
