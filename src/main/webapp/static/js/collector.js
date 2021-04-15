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
        oldPassword: $("#password-now").val(),
        password: $("#password").val(),
        verificationcode: $("#verificationcode").val()
    };
}

// build JSON object by Api spec: Sensor
function buildSensor() {
    return {
        input: $("#sensor_input").val(),
        name: $("#sensor_name").val(),
        sensorTypeName: $("#sensor-type_name").val(),
        controllerUnitId: getUrlParameter('id')
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
    if ($("#password-retype").val() !== $("#password").val()) {
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

// Change user's password
function changePassword() {
    if ($("#password-retype").val() != $("#password").val()) {
        alert("Passwords does not match!")
    } else {
        $.ajax({
            method: "POST",
            url: $SCRIPT_ROOT + "/action/change-password",
            contentType: CONTENT_TYPE,
            dataType: DATA_TYPE,
            timeout: 20000,
            data: JSON.stringify(buildAuth()),
            statusCode: {
                200: function(response) {
                    $(':input').val('');
                    alert('You\'ve successfully changed your password.\n' + response.message)
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
// Create new controller unit
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

// Create new sensor
function createSensor() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/admin/sensors/create",
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        data: JSON.stringify(buildSensor()),
        statusCode: {
            201: function(response) {
                alert('Vytvorený nový sensor.');
                window.location.reload();
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
    $(location).prop('href', $SCRIPT_ROOT);
}

// TEST ONLY
function goToFlatId1() {
    $(location).attr('href', $SCRIPT_ROOT + '/action/projects/flats?fid=1');
}

// Login user
function goToSeeYourProjects() {
    $(location).attr('href', $SCRIPT_ROOT + '/action/projects');
}

// used in index to visit flat with id in the box
function accessFlatWithId() {
    var idToVisit = idToVisit = $("#access-flatId").val();
    $(location).attr('href', $SCRIPT_ROOT + '/action/projects/flats?fid=' + idToVisit);
}

// used in public request for consumption to visit by uid
function requestViewConsumption() {
    var uidToVisit = uidToVisit = $("#request-uid").val();
    $(location).attr('href', $SCRIPT_ROOT + '/consumption-view?uid=' + uidToVisit);
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

function getUrlParameter(sParam) { // https://stackoverflow.com/a/21903119/5148218
    var sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return typeof sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
    return false;
}

$(function() {
    // add link to single controller page
    $(".controllerUnit_link").each(function(index, value){
        var controllerUnitId = $(value).text();
        $(this).html(
            '<a href="' + $SCRIPT_ROOT + '/action/controllerUnit?id=' + controllerUnitId + '">link</a>'
        );
    });
    // add link to create sensor type page
    $("._sensortype_createnewlink").each(function(index, value){
        $(this).html(
            '<a href="' + $SCRIPT_ROOT + '/admin/sensor-type/create">Create new sensor type</a>'
        );
    });
    // add link to create communication type page
    $("._commtype_createnewlink").each(function(index, value){
        $(this).html(
            '<a href="' + $SCRIPT_ROOT + '/admin/comm-type/create">Create new communication type</a>'
        );
    });
});

$(function() {
    // add link to single controller page
    $(".user_link").each(function(index, value){
        var userId = $(value).text();
        $(this).html(
            '<a href="' + $SCRIPT_ROOT + '/admin/users?id=' + userId + '">link</a>'
        );
    });
});
