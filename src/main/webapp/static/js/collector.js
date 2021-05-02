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

// build JSON object by Api spec: Address
function buildAddress() {
    return {
        country: $("#country").val(),
        city: $("#city").val(),
        street: $("#street").val(),
        houseno: $("#houseno").val(),
        zip: $("#zip").val()
    };
}

function buildBuilding() {
    return {
        addressId: $("#address").val(),
        projectId: $("#project-id").val()
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

// build JSON object by Api spec: ControllerUnit
function buildControllerUnit() {
    return {
        uid: $("#controller-uid").val(),
        dipAddress: $("#controller-dip_address").val(),
        zwave: $("#controller-zwave").val(),
        flatId: $("#controller-flat-id").val()
    };
}

// build JSON object by Api spec: CentralUnit
function buildCentralUnit() {
    return {
        uid: $("#central-uid").val(),
        dipAddress: $("#central-dip_address").val(),
        friendlyName: $("#central-friendly_name").val(),
        simNo: $("#central-sim_no").val(),
        imei: $("#central-imei").val(),
        zwave: $("#central-zwave").val(),
        buildingId: getUrlParameter('id')
    };
}

function buildFlatForBuilding() { // TODO nazvoslovie u vsetkych znamena build + meno .java modelovej triedy ktoru stavas, v tomto pripade Flat_FlatOwners_Creation
    return {
        flat: {
            apartmentNO: $("#apartment-No").val(),
            buildingId: getUrlParameter('id')
        },
        owner1: {
            title: $("#owner1-title").val(),
            firstName: $("#owner1-name").val(),
            middleName: $("#owner1-middlename").val(),
            lastName: $("#owner1-lastname").val(),
            phone: $("#owner1-phone").val(),
            email: $("#owner1-email").val(),
            address: $("#owner1-address").val()
        },
        owner2: {
            title: $("#owner2-title").val(),
            firstName: $("#owner2-name").val(),
            middleName: $("#owner2-middlename").val(),
            lastName: $("#owner2-lastname").val(),
            phone: $("#owner2-phone").val(),
            email: $("#owner2-email").val(),
            address: $("#owner2-address").val()
        }
    };
}

// POST calls

function createNewFlat() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/admin/flat/create",
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        data: JSON.stringify(buildFlatForBuilding()),
        statusCode: {
            201: function(response) {
                $(".rounded-input").val('');
                $(":checkbox").prop("checked", false).removeAttr("checked");
                alert('Flat and owners successfully created.');
                window.location.reload();
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
                    $(':input').val('');
                    alert('Thank you for your registration\n' + response.message)
                    $(location).attr('href', $SCRIPT_ROOT + "/login");
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
                //alert(response.message)
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
    var controllerUnit = buildControllerUnit();
    if (controllerUnit.uid == "" ||
        controllerUnit.dipAddress == "" ||
        controllerUnit.zwave == "") {
        alert("All fields have to be filled.");
    } else {
        $.ajax({
            method: "POST",
            url: $SCRIPT_ROOT + "/admin/controllerUnits/create",
            contentType: CONTENT_TYPE,
            dataType: DATA_TYPE,
            data: JSON.stringify(controllerUnit),
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
}

// Create new central unit
function createCentralUnit() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/admin/centralUnits/create",
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        data: JSON.stringify(buildCentralUnit()),
        statusCode: {
            201: function(response) {
                alert('Vytvorený nový central unit.');
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

// Create new Building
function createNewBuildingForProject() {
    $.ajax({
        method: "POST",
        url: $SCRIPT_ROOT + "/admin/buildings/create",
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        data: JSON.stringify(buildBuilding()),
        statusCode: {
            201: function(response) {
                alert('Vytvorená nová budova.');
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
        data: JSON.stringify(buildAddress()),
        statusCode: {
            201: function(response) {
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

// NAVIGATION
// Login user
function goHome() {
    $(location).prop('href', $SCRIPT_ROOT);
}

// TEST ONLY
function goToFlatId1() {
    $(location).attr('href', $SCRIPT_ROOT + '/action/projects/flats?id=1');
}

// Login user
function goToSeeYourProjects() {
    $(location).attr('href', $SCRIPT_ROOT + '/action/projects');
}

// used in index to visit flat with id in the box
function accessFlatWithId() {
    var idToVisit = idToVisit = $("#access-flatId").val().replace(/\s+/g, '');

    if (isInputIdUsable(idToVisit)) {
        $(location).attr('href', $SCRIPT_ROOT + '/action/projects/flats?id=' + idToVisit);
    } else {
        alert('Please provide a valid integer id as input to request FLAT VIEW.');
    }
}


// used in index to visit flat with id in the box
function accessCentralWithId() {
    var idToVisit = idToVisit = $("#access-centralId").val().replace(/\s+/g, '');

    if (isInputIdUsable(idToVisit)) {
        $(location).attr('href', $SCRIPT_ROOT + '/action/centralUnits?id=' + idToVisit);
    } else {
        alert('Please provide a valid integer id as input to request Central Unit VIEW.');
    }
}

// used in public request for consumption to visit by uid
function requestViewConsumption() {
    var uidToVisit = uidToVisit = $("#request-uid").val().replace(/\s+/g, '');

    if (isInputIdUsable(uidToVisit)) {
        $(location).attr('href', $SCRIPT_ROOT + '/consumption-view?uid=' + uidToVisit);
    } else {
        alert('Please provide a valid identification as provided in your contract with us.');
    }
}

// used for input id checking
const isInputIdUsable = (foo ="") => {
    if ("" === foo || Number.isInteger(+foo) == false)
        return false;

    return true;
}

function showVisibilityOfAdditionElement() {
    switchVisibilityOfAdditionElement();

    switchToggles();
}

function hideVisibilityOfAdditionElement() {
    switchVisibilityOfAdditionElement();

    switchToggles();
}


function handleAddressOwnerClick(cb) {
    var x;
    if (cb.id == "owner1-sameaddress") {
        x = document.getElementById("owner1-address");
    }

    if (cb.id == "owner2-sameaddress") {
        x = document.getElementById("owner2-address");
    }

    if (cb.checked) {
        x.value = document.getElementById("building-country").textContent + ", " +
            document.getElementById("building-city").textContent + ", " +
            document.getElementById("building-street").textContent + " " +
            document.getElementById("building-houseno").textContent + ", " +
            document.getElementById("building-zip").textContent;
        x.disabled = true;
    } else {
        x.value = "";
        x.disabled = false;
    }
}

function switchVisibilityOfAdditionElement() {
    var x = document.getElementById("addition-toggle");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}

function switchBetweenAdditionOfOptUser() {
    var x = document.getElementById("toggle-optional-flatowner");
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
    // add link to create sensor type page
    $(".sensortype_linkCreate").each(function(index, value){
        $(this).html(
            '<a href="' + $SCRIPT_ROOT + '/admin/sensor-type/create">Create new sensor type</a>'
        );
    });
    // add link to create communication type page
    $(".commtype_linkCreate").each(function(index, value){
        $(this).html(
            '<a href="' + $SCRIPT_ROOT + '/admin/comm-type/create">Create new communication type</a>'
        );
    });
    // add link to single user page
    $(".user_link").each(function(index, value){
        var id = $(value).text();
        $(this).html(
            '<a href="' + $SCRIPT_ROOT + '/admin/users?id=' + id + '">open</a>'
        );
    });
    // add link to single project page
    $(".project_link").each(function(index, value){
        var id = $(value).text();
        $(this).html(
            '<a href="' + $SCRIPT_ROOT + '/action/projects?id=' + id + '">open</a>'
        );
    });
    // add link to single building page
    $(".building_link").each(function(index, value){
        var id = $(value).text();
        $(this).html(
            '<a href="' + $SCRIPT_ROOT + '/action/buildings?id=' + id + '">open</a>'
        );
    });
    // add link to single flat page
    $(".flat_link").each(function(index, value){
        var id = $(value).text();
        $(this).html(
            '<a href="' + $SCRIPT_ROOT + '/action/projects/flats?id=' + id + '">open</a>'
        );
    });
    // add link to single controller unit page
    $(".controllerUnit_link").each(function(index, value){
        var id = $(value).text();
        $(this).html(
            '<a href="' + $SCRIPT_ROOT + '/action/controllerUnit?id=' + id + '">open</a>'
        );
    });
    // add link to single central unit page
    $(".centralUnit_link").each(function(index, value){
        var id = $(value).text();
        $(this).html(
            '<a href="' + $SCRIPT_ROOT + '/action/centralUnits?id=' + id + '">open</a>'
        );
    });
});
