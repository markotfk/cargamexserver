/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var playerKey = "carGamePlayer";

$(document).ready(function() {
    
     updateLoginForms();
    
    $('#login_form').submit(function(event) {
        event.preventDefault();
        
        $('#login_status').html('Processing...');
        var player = { login: $('#login').val(), password: $('#password').val() };
        $.ajax(PlayerRoot + '/login', {
            contentType: 'application/json',
            dataType: 'json',
            type: 'POST',
            success: function(data, status, jqXHR) {
                log('Success login');
                if (data && data.sessionId) {
                    sessionStorage.setItem(playerKey, JSON.stringify(data));
                    $('#login_status').html('');
                } else {
                    sessionStorage.setItem(playerKey, null);
                    $('#login_status').html('Error in received data');
                }
                
                updateLoginForms(true);
            },
            error: function(jqXHR, textStatus, errorString) {
                log('Error in login: ' + textStatus + ':' + errorString);
                $('#login_status').html('Error:' + errorString);
                updateLoginForms(false);
            },
            data: JSON.stringify(player)
        });
    });
    
    $('#logout_form').submit(function(event) {
        event.preventDefault();
        
        $('#login_status').html('Processing...');

        
        
        $.ajax(PlayerRoot + '/logout', {
            contentType: 'application/json',
            dataType: 'json',
            type: 'POST',
            success: function(data, status, jqXHR) {
                log('Success logout');
                sessionStorage.setItem(playerKey, null);
                $('#login_status').html('');
                updateLoginForms(false);
            },
            error: function(jqXHR, textStatus, errorString) {
                log('Error in logout: ' + textStatus + ':' + errorString);
                $('#login_status').html('Error:' + errorString);
                sessionStorage.setItem(playerKey, null);
                updateLoginForms(false);
            },
            data: sessionStorage.getItem(playerKey)
        });
    });
});

function updateLoginForms(logged) {
    if (logged) {
         $('#login_form').hide();
         $('#logout_form').show();
         var player = JSON.parse(sessionStorage.getItem(playerKey));
         if (player) {
             $('#login_name').html(player.login + ': ' + player.sessionId);
         } else {
             $('#login_name').html('Logged in');
         }
     } else {
         $('#login_form').show();
         $('#logout_form').hide();
         $('#login_name').html('Not Logged in');
     }
}
