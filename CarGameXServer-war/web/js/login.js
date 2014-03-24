/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function() {
    $('#login_form').submit(function(event) {
        event.preventDefault();
        
        $('#login_status').html('Processing...');
        var player = { login: $('#login').val(), password: $('#password').val() };
        $.ajax(PlayerRoot + '/login', {
            contentType: 'application/json',
            type: 'POST',
            success: function(data, status, jqXHR) {
                log('Success login');
                $('#login_status').html('Login ok');
            },
            error: function(jqXHR, textStatus, errorString) {
                log('error login' + textStatus + ':' + errorString);
                $('#login_status').html('Login failed: ' + errorString);
            },
            data: JSON.stringify(player)
        });
    });
});
