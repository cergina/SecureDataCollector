
function registerUser() {
    var email = $("#email").val();
    var firstname = $("#first-name").val();
    $.post( $SCRIPT_ROOT + "/register", { email: email, firstname: firstname } )
        .done(function( data ) {
            console.log(data);
            $(location).attr('href', $SCRIPT_ROOT + "/index");
        });
}

$(function() {
    console.log("Hello World from static");
});
