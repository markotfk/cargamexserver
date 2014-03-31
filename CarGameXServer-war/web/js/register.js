//Author     : Marko Karjalainen <markotfk@gmail.com>

$(document).ready(function() {
    $('#register_form').submit(function(event) {
        event.preventDefault();
        
        if ($('#register_password').val() !== $('#register_password2').val()) {
            alert("Please check password");
            return false;
        }
        $('#register_status').html('Processing...');
        
        var player = { 
            email: $('#register_email').val(), 
            login: $('#register_login').val(), 
            password: $('#register_password').val()
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
