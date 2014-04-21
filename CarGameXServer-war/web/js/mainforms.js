// Author     : Marko Karjalainen <markotfk@gmail.com>

$(document).ready(function() {
    initLoginForm();
    initLogoutForm();
    initAddTeamForm();
    initRemoveTeamForm();
    var player = getSessionData(playerKey);
    if (player) {
        loggedIn = true;
        updateForms(true);
    } else {
        updateForms(loggedIn);
    }
    updateTeamStatus();
});

function initLoginForm() {
    $('#login_form').submit(function(event) {
        event.preventDefault();
        var player = { login: $('#login').val(), password: $('#password').val() };
        loginPlayer(player);
    });
}

function loginPlayer(player) {
    showStatus('Processing...');
    
    $.ajax(PlayerRoot + 'login', {
        contentType: 'application/json',
        dataType: 'json',
        type: 'POST',
        success: function(data, status, jqXHR) {
            log('login_form: Success login');
            if (data && data.sessionId) {
                localStorage.setItem(playerKey, JSON.stringify(data));
                showStatus('');
                startSessionTimer();
            } else {
                localStorage.setItem(playerKey, null);
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
}

function initLogoutForm() {
    $('#logout_form').submit(function(event) {
        event.preventDefault();
        logoutPlayer();
    });
}

function logoutPlayer() {
    showStatus('Processing...');
    $.ajax(PlayerRoot + 'logout', {
        contentType: 'application/json',
        dataType: 'json',
        type: 'POST',
        success: function(data, status, jqXHR) {
            log('logout_form: Success logout');
            localStorage.setItem(playerKey, null);
            showStatus('');
            updateForms(false);
            updateTeamStatus();
            stopSessionTimer();
        },
        error: function(jqXHR, textStatus, errorString) {
            log('logout_form: Error in logout: ' + textStatus + ':' + errorString);
            showStatus('Error:' + errorString);
            localStorage.setItem(playerKey, null);
            localStorage.setItem(teamKey, null);
            updateForms(false);
            updateTeamStatus();
            stopSessionTimer();
        },
        data: localStorage.getItem(playerKey)
    });
}

function initAddTeamForm() {
    $('#team_form_add').submit(function(event) {
        event.preventDefault();
        showStatus('Processing...');
        var player = getSessionData(playerKey);
        if (!player) {
            log('Error: must be logged in to add team');
            showStatus('');
            return false;
        }
        var addTeamUrl = TeamRoot + 'add/' + player.id + '/' + player.sessionId;
        var team = { name: $('#team_name').val(),description: $('#team_description').val() };
        $.ajax(addTeamUrl, {
            contentType: 'application/json',
            dataType: 'json',
            type: 'POST',
            success: function(data, status, jqXHR) {
                log('team_form_add: Success create team');
                localStorage.setItem(teamKey, JSON.stringify(data));
                showStatus('');
                updateTeamStatus();
            },
            error: function(jqXHR, textStatus, errorString) {
                log('team_form_add: Error in creating team:', errorString);
                showStatus('Error:' + errorString);
                localStorage.setItem(teamKey, null);
                updateTeamStatus();
            },
            data: JSON.stringify(team)
        });
    });
}

function initRemoveTeamForm() {
    $('#team_form_remove').submit(function(event) {
        event.preventDefault();
        showStatus('Processing...');
        var player = getSessionData(playerKey);
        if (!player) {
            log('Error: must be logged in to remove team');
            showStatus('');
            $('#team_form_remove').hide();
            return false;
        }
        var team = getSessionData(teamKey);
        if (!team) {
            log('Error: must have team to remove team');
            showStatus('');
            $('#team_form_remove').hide();
            return false;
        }
        var removeTeamUrl = TeamRoot + 'remove/' + player.id + '/' + player.sessionId +
                '/' + team.id;
        $.ajax(removeTeamUrl, {
            contentType: 'application/json',
            dataType: 'json',
            type: 'POST',
            success: function(data, status, jqXHR) {
                log('team_form_remove: Success remove team');
                localStorage.setItem(teamKey, null);
                showStatus('');
                updateTeamStatus();
            },
            error: function(jqXHR, textStatus, errorString) {
                log('team_form_remove: Error in removing team:', errorString);
                showStatus('Error:' + errorString);
                localStorage.setItem(teamKey, null);
                updateTeamStatus();
            }
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
         $('#login_created').show();
         var player = getSessionData(playerKey);
         if (player) {
             $('#login_name').html(player.login + ', sessionId: ' + player.sessionId + ', id:' + player.id);
             $('#login_created').html('Account created: ' + new Date(player.created).toLocaleString());
         } else {
             $('#login_name').html('Logged in');
             $('#login_created').html('');
         }
         
     } else {
         $('#login_form').show();
         $('#logout_form').hide();
         $('#login_name').html('Not Logged in');
         $('#login_created').hide();
     }
}

function updateTeamStatus() {
    if (loggedIn) {
        $('#team_membership').show();
        $('#team_created').show();
        var team = getSessionData(teamKey);
        if (team) {
            $('#team_form_add').hide();
            $('#team_form_remove').show();
            $('#team_membership').html('<br>Team id:' + team.id + '<br> name:' + team.name + '<br>Description:<br>' + team.description);
            $('#team_created').html('Team created: ' + new Date(team.created).toLocaleString());
        } else {
            $('#team_form_add').show();
            $('#team_form_remove').hide();
            $('#team_membership').hide();
            $('#team_created').hide();
        }
    } else {
        $('#team_membership').hide();
        $('#team_created').hide();
        $('#team_form_add').hide();
        $('#team_form_remove').hide();
    }
}

function getSessionData(key) {
    return JSON.parse(localStorage.getItem(key));
}
