var CONTENT_TYPE = "application/json; charset=utf-8";
var DATA_TYPE = "json";



// build JSON object by Api spec: User
function buildUser() {
    return {
        email: $("#email").val(),
        beforetitle: $("#beforetitle").val(),
        firstname: $("#firstname").val(),
        middlename: $("#middlename").val(),
        lastname: $("#lastname").val(),
        phone: $("#phone").val(),
        residence: $("#residence").val()
    };
}

// build JSON object by Api spec: Auth
function buildAuth() {
    return {
        user: buildUser(),
        isadmin: $("#isadmin").prop("checked"),
        password: $("#password").val(),
        verificationcode: $("#verificationcode").val()
    };
}

// Create new user
function createUser() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/admin/user/create",
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        data: JSON.stringify(buildAuth()),
        statusCode: {
            201: function(response) {
                $(':input').val('');
                alert('Verifikacny kod je: ' + response.data.verificationcode);
            },
            400: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            },
            401: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            }
        },
        complete: function(jqXHR) { // keep for DEBUG only
            console.log("---DEBUG---");
            console.log(jqXHR);
        }
    });
}

// Finish registration
function registerUser() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/register",
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        data: JSON.stringify(buildAuth()),
        statusCode: {
            200: function(response) {
                // TODO thank user for registration
                alert('Thank you for your registration\n' + response.message)
            },
            400: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            },
            401: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            }
        },
        complete: function(jqXHR) { // keep for DEBUG only
            console.log("---DEBUG---");
            console.log(jqXHR);
        }
    });
}

// Login user
function loginUser() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/login",
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        data: JSON.stringify(buildAuth()),
        statusCode: {
            200: function(response) {
                alert(response.message)
                $(location).attr('href', $SCRIPT_ROOT + "/index");
            },
            401: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            }
        },
        complete: function(jqXHR) { // keep for DEBUG only
            console.log("---DEBUG---");
            console.log(jqXHR);
        }
    });
}

// NAVIGATION
// Login user
function goHome() {
    $(location).attr('href', $SCRIPT_ROOT);
}

// Login user
function goToFlatId1() {
    $(location).attr('href', $SCRIPT_ROOT + '/action/projects/flats?fid=1');
}

// Login user
function goToSeeYourProjects() {
    $(location).attr('href', $SCRIPT_ROOT + '/action/projects');
}

$(function() {

});
