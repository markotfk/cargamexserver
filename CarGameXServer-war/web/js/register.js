//Author     : Marko Karjalainen <markotfk@gmail.com>

$(document).ready(function() {
    $('#register_form').submit(function(event) {
        event.preventDefault();
        // Validate fields, email is validated by browser
        var login = $('#register_login').val();
        if (login.length === 0) {
            alert("Please check login")
            return false;
        }
        var passwd = $('#register_password').val();
        if (passwd !== $('#register_password2').val()) {
            alert("Please check password");
            return false;
        }
        if (passwd.length < 6) {
            alert("Password must be at least 6 characters.");
            return false;
        }
        $('#register_status').html('Processing...');
        
        var player = { 
            email: $('#register_email').val(), 
            login: login, 
            password: passwd
        };
        
        $.ajax(PlayerRoot, {
            contentType: 'application/json',
            type: 'POST',
            success: function(data, status, jqXHR) {
                log('Success Register');
                $('#register_status').html('Player registered ok!');
            },
            error: function(jqXHR, textStatus, errorString) {
                log('error Register: ' + textStatus + ': ' + errorString);
                $('#register_status').html('Register failed: ' + errorString);
            },
            data: JSON.stringify(player)
        });
    });
});
