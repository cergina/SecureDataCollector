var CONTENT_TYPE = "application/json; charset=utf-8";
var DATA_TYPE = "json";


// REQUEST BODY BUILDERS:

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


// build JSON object by Api spec: ProjectCreation
function buildProjectCreation() {
    return {
        project_name: $("#project_name").val(),
        required_email: $("#project_email_0").val(),
        additional_emails: [
            $("#project_email_1").val(),
            $("#project_email_2").val()
        ]
    };
}


function buildAddressCreation() {
    return {
        street: $("#street").val(),
        houseno: $("#houseno").val(),
        city: $("#city").val(),
        zip: $("#zip").val(),
        country: $("#country").val(),
    };
}

// build JSON object by Api spec: CommType
function buildCommType() {
    return {
        name: $("#comm-type_name").val()
    };
}

// build JSON object by Api spec: SensorType
function buildSensorType() {
    return {
        name: $("#sensor-type_name").val(),
        measuredin: $("#sensor-type_measured-in").val(),
        commType: buildCommType()
    };
}

// build JSON object by Api spec: ControllerCreation
function buildControllerCreation() {
    return {
        uid: $("#controller-uid").val(),
        dipAddress: $("#controller-dip_address").val(),
        zwave: $("#controller-zwave").val(),
        flatId: $("#controller-flat-id").val()
    };
}

// POST calls

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
            },
            500: function(jqXHR) {
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
    if ($("#password-retype").val() != $("#password").val()) {
      alert("Passwords does not match!")
    } else {
        $.ajax({
            method: "POST",
            url: $SCRIPT_ROOT + "/register",
            contentType: CONTENT_TYPE,
            dataType: DATA_TYPE,
            data: JSON.stringify(buildAuth()),
            statusCode: {
                200: function(response) {
                    // TODO thank user for registration
                    $(':input').val('');
                    alert('Thank you for your registration\n' + response.message)
                },
                400: function(jqXHR) {
                    var response = JSON.parse(jqXHR.responseText);
                    alert(response.message); // TODO impact layout
                },
                401: function(jqXHR) {
                    var response = JSON.parse(jqXHR.responseText);
                    alert(response.message); // TODO impact layout
                },
                500: function(jqXHR) {
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

// CREATING STUFF
// Create new controller unit from inside of flat
function createControllerUnitForThisFlat() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/admin/controllers/create",
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        data: JSON.stringify(buildControllerCreation()),
        statusCode: {
            201: function(response) {
                $("#controller-uid").val('');
                $("#controller-dip_address").val('');
                $("#controller-zwave").val('');
                alert('Vytvorený nový controller unit.');
                window.location.reload();
            },
            409: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            },
            400: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            },
            500: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            }
        }
    });
}

// Create new project from data acquired from another js function
function createProject() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/admin/projects/create",
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        data: JSON.stringify(buildProjectCreation()),
        statusCode: {
            201: function(response) {
                $(':input').val('');
                alert('Vytvorený nový projekt.');
            },
            409: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            },
            400: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            },
            500: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            }
        }
    });
}

function createAddress() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/admin/addresses/create",
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        data: JSON.stringify(buildAddressCreation()),
        statusCode: {
            201: function(response) {
                $(':input').val('');
                alert('Úspešne sa podarilo vytvoriť novú adresu.');
            },
            409: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            },
            400: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            },
            500: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            }
        }
    });
}

// Create new communication type
function createCommType() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/admin/comm-type/create",
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        data: JSON.stringify(buildCommType()),
        statusCode: {
            201: function(response) {
                $(':input').val('');
                alert('Vytvorený nový typ komunikácie: ' + response.data.name);
            },
            400: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            },
            409: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            },
            500: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            }
        }
    });
}

// Create new sensor type
function createSensorType() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/admin/sensor-type/create",
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        data: JSON.stringify(buildSensorType()),
        statusCode: {
            201: function(response) {
                $(':input').val('');
                alert('Vytvorený nový typ sensora: ' + response.data.name);
            },
            400: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            },
            409: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            },
            500: function(jqXHR) {
                var response = JSON.parse(jqXHR.responseText);
                alert(response.message); // TODO impact layout
            }
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


function showVisibilityOfAdditionElement() {
    switchVisibilityOfAdditionElement();

    switchToggles();
}

function hideVisibilityOfAdditionElement() {
    switchVisibilityOfAdditionElement();

    switchToggles();
}

function switchVisibilityOfAdditionElement() {
    var x = document.getElementById("addition-toggle");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}

function switchToggles() {
    var x = document.getElementById("toggle-button-addition-show");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }

    var x = document.getElementById("toggle-button-addition-hide");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}

$(function() {

});
