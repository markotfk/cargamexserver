//Author     : Marko Karjalainen <markotfk@gmail.com>

DEBUG = true;
Version = 'v1';
RestDir = '/carx/rest/' + Version + '/';
PlayerRoot = RestDir + 'players/';
TeamRoot = RestDir + 'teams/';
playerKey = "carGamePlayer";
teamKey = "carGameTeam";
loggedIn = false;

function log() {
    if (!DEBUG) {
        return;
    }
    for (var i = 0; i < arguments.length; i++) {
       console.log(arguments[i]);
    }
}
