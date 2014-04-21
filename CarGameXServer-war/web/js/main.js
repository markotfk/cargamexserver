//Author     : Marko Karjalainen <markotfk@gmail.com>

DEBUG = true;
Version = 'v1';
RestDir = '/carx/rest/' + Version + '/';
PlayerRoot = RestDir + 'players/';
SessionRoot = RestDir + 'session/';
TeamRoot = RestDir + 'teams/';
TrackRoot = RestDir + 'tracks/';
TrackRecordRoot = RestDir + 'trackrecords/';
playerKey = "carGamePlayer";
teamKey = "carGameTeam";
loggedIn = false;
SessionTimer = 0;
SessionTimerRunning = false;

function log() {
    if (!DEBUG) {
        return;
    }
    for (var i = 0; i < arguments.length; i++) {
        if (arguments[i]) {
            console.log(new Date().toTimeString() + ": " + arguments[i]);
        }
    }
}

function getSessionData(key) {
    return JSON.parse(localStorage.getItem(key));
}

function startSessionTimer() {
    log('startSessionTimer');
    SessionTimer = window.setInterval(function() { updateSession(); }, 60000);
}

function stopSessionTimer() {
    log('stopSessionTimer');
    window.clearInterval(SessionTimer);
}

function updateSession() {
    log('updateSession');
    $.ajax(SessionRoot, {
        contentType: 'application/json',
        dataType: 'json',
        type: 'POST',
        success: function(data, status, jqXHR) {
            
        },
        error: function(jqXHR, textStatus, errorString) {
            logoutPlayer();
        },
        data: localStorage.getItem(playerKey)
    });
}