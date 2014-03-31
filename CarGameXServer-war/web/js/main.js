//Author     : Marko Karjalainen <markotfk@gmail.com>

DEBUG = true;
Version = 'v1';
RestDir = '/carx/rest/' + Version + '/';
PlayerRoot = RestDir + 'players/';
TeamRoot = RestDir + 'teams/';
playerKey = "carGamePlayer";
teamKey = "carGameTeam";
loggedIn = false;

function log(message) {
    if (DEBUG) {
        console.log(message);
    }
}
