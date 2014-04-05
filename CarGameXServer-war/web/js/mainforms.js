// Author     : Marko Karjalainen <markotfk@gmail.com>

$(document).ready(function() {
    
    updateForms(loggedIn);
    initLoginForm();
    initLogoutForm();
    initAddTeamForm();
    updateTeamStatus();
});

function initLoginForm() {
    $('#login_form').submit(function(event) {
        event.preventDefault();
        
        showStatus('Processing...');
        var player = { login: $('#login').val(), password: $('#password').val() };
        $.ajax(PlayerRoot + 'login', {
            contentType: 'application/json',
            dataType: 'json',
            type: 'POST',
            success: function(data, status, jqXHR) {
                log('login_form: Success login');
                if (data && data.sessionId) {
                    sessionStorage.setItem(playerKey, JSON.stringify(data));
                    showStatus('');
                } else {
                    sessionStorage.setItem(playerKey, null);
                    showStatus('Error in received data');
                }
                
                updateForms(true);
                updateTeamStatus();
            },
            error: function(jqXHR, textStatus, errorString) {
                log('login_form: Error:', errorString);
                showStatus('Error:' + errorString);
                updateForms(false);
            },
            data: JSON.stringify(player)
        });
    });
}

function initLogoutForm() {
    $('#logout_form').submit(function(event) {
        event.preventDefault();
        showStatus('Processing...');
        $.ajax(PlayerRoot + 'logout', {
            contentType: 'application/json',
            dataType: 'json',
            type: 'POST',
            success: function(data, status, jqXHR) {
                log('logout_form: Success logout');
                sessionStorage.setItem(playerKey, null);
                showStatus('');
                updateForms(false);
                updateTeamStatus();
            },
            error: function(jqXHR, textStatus, errorString) {
                log('logout_form: Error in logout: ' + textStatus + ':' + errorString);
                showStatus('Error:' + errorString);
                sessionStorage.setItem(playerKey, null);
                updateForms(false);
                updateTeamStatus();
            },
            data: sessionStorage.getItem(playerKey)
        });
    });
}

function initAddTeamForm() {
    $('#team_form').submit(function(event) {
        event.preventDefault();
        showStatus('Processing...');
        var player = getSessionData(playerKey);
        if (!player) {
            return false;
        }
        var addTeamUrl = TeamRoot + 'add/' + player.id + '/' + player.sessionId;
        var team = { name: $('#team_name').val(),description: $('#team_description').val() };
        $.ajax(addTeamUrl, {
            contentType: 'application/json',
            dataType: 'json',
            type: 'POST',
            success: function(data, status, jqXHR) {
                log('team_form: Success create team');
                sessionStorage.setItem(teamKey, JSON.stringify(data));
                showStatus('');
                updateTeamStatus();
            },
            error: function(jqXHR, textStatus, errorString) {
                log('team_form: Error in creating team:', errorString);
                showStatus('Error:' + errorString);
                sessionStorage.setItem(teamKey, null);
                updateTeamStatus();
            },
            data: JSON.stringify(team)
        });
    });
}

function showStatus(message) {
    $('#error_status').html(message);
}
function updateForms(logged) {
    loggedIn = logged;
    if (logged) {
         $('#login_form').hide();
         $('#logout_form').show();
         var player = getSessionData(playerKey);
         if (player) {
             $('#login_name').html(player.login + ', sessionId: ' + player.sessionId + ', id:' + player.id);
         } else {
             $('#login_name').html('Logged in');
         }
         $('#team_form').show();
     } else {
         $('#login_form').show();
         $('#logout_form').hide();
         $('#team_form').hide();
         $('#login_name').html('Not Logged in');
     }
}

function updateTeamStatus() {
    if (loggedIn) {
        $('#team_membership').show();
        var team = getSessionData(teamKey);
        if (team) {
            $('#team_membership').html('<br>Team id:' + team.id + '<br> name:' + team.name + '<br>Description:<br>' + team.description);
        }
    } else {
        $('#team_membership').hide();
    }
}

function getSessionData(key) {
    return JSON.parse(sessionStorage.getItem(key));
}
