// Author     : Marko Karjalainen <markotfk@gmail.com>

var passedCount = 0;
var failedCount = 0;
var errors = [];
var testName = "";
var testPlayer = {};
var testTeam = {};
var receiveData = false;
var randomId = 0;

$(document).ready(function() {
    runTests();
});

function runTests() {
    passedCount = 0;
    failedCount = 0;
    testName = "runTests";
    try {
        testPlayerRestApi();
    } catch (err) {
        failed(err);
    }
    try {
        testTeamRestApi();
    } catch (err) {
        failed(err);
    }
    try {
        testTrackRestApi();
    } catch (err) {
        failed(err);
    }
    try {
        testTrackRecordRestApi();
    } catch (err) {
        failed(err);
    }
    
    showResults();
}

function createTestPlayer() {
    var rand = getRandomNumber();
    testPlayer = new Player('test' + rand + '@test.com', 'test' + rand, 'password' + rand);
}

function createTestTeam() {
    var rand = getRandomNumber();
    testTeam = new Team('name' + rand, 'description' + rand);
}

function testTeamRestApi() {
    createTestPlayer();
    createTestTeam();

    addAndLoginTestPlayer();
    addTestTeam();
    deleteTestTeam();
    deleteTestPlayer();
    
}

function testTrackRestApi() {
    failed('testTrackRestApi not implemented yet');
}

function testTrackRecordRestApi() {
    failed('testTrackRecordRestApi not implemented yet');
}

function testPlayerRestApi() {
    createTestPlayer();
    addAndLoginTestPlayer();
    logoutTestPlayer();
    
    // Delete player
    // password must be reset for login
    testPlayer.password = 'password' + randomId;
    ajaxCallPlayer('Delete Player 1st step (Login Player)', PlayerRoot + 'login', 'POST', true);
    deleteTestPlayer();
}

function ajaxCallPlayer(name, url, type, dataReceived) {
    testName = name;
    receiveData = dataReceived;
    $.ajax(url, {
        contentType: 'application/json',
        type: type,
        async: false,
        success: ajaxPassPlayer,
        error: ajaxFailPlayer,
        data: JSON.stringify(testPlayer)
    });
}

function ajaxPassPlayer(data, status, jqXHR) {
    if (receiveData) {
        try {
            // Get id, sessionId and password generated in server side
            testPlayer.id = data.id;
            testPlayer.sessionId = data.sessionId;
            testPlayer.password = data.password;
        } catch (err) {
            failed(err);
            return;
        }
    }
    pass();
}

function ajaxFailPlayer(jqXHR, textStatus, errorString) {
    failed(errorString);
}

function ajaxCallTeam(name, url, type, dataReceived) {
    testName = name;
    receiveData = dataReceived;
    $.ajax(url, {
        contentType: 'application/json',
        type: type,
        async: false,
        success: ajaxPassTeam,
        error: ajaxFailTeam,
        data: JSON.stringify(testTeam)
    });
}

function ajaxPassTeam(data, status, jqXHR) {
    if (receiveData) {
        try {
            // Get id generated in server side
            testTeam.id = data.id;
        } catch (err) {
            failed(err);
            return;
        }
    }
    pass();
}

function ajaxFailTeam(jqXHR, textStatus, errorString) {
    failed(errorString);
}


function showResults() {
    $('#tests_summary').html('<p>Tests passed: ' + passedCount + '</p><br>' + 
            '<p>Tests failed: ' + failedCount + '</p><br>');
    var failedHtml = "<p>Failed:<br><ul>";
    for (var i = 0; i < errors.length; i++) {
        failedHtml += "<li>" + errors[i] + "</li><br>";
    }
    failedHtml += '</ul></p>';
    $('#tests_failed').html(failedHtml);
}

function addTestTeam() {
    // Add team
    ajaxCallTeam('Add New Team ' + testTeam.name, TeamRoot + 'add/' + testPlayer.id + '/' + testPlayer.sessionId, 'POST', true);
}

function deleteTestTeam() {
    // Add team
    ajaxCallTeam('Delete Team ' + testTeam.name, TeamRoot + 'remove/' + testPlayer.id + '/' + testPlayer.sessionId
    + '/' + testTeam.id, 'POST', false);
}

function addAndLoginTestPlayer() {
    // First login test player to allow creating new team
    ajaxCallPlayer('Add New Player ' + testPlayer.login, PlayerRoot, 'POST', false); 
    // Login player that was added
    ajaxCallPlayer('Login Player ' + testPlayer.login, PlayerRoot + 'login', 'POST', true);
}

function logoutTestPlayer() {
    // Logout player
    ajaxCallPlayer('Logout Player', PlayerRoot + 'logout', 'POST', false);
}

function deleteTestPlayer() {
    ajaxCallPlayer('Delete Player ' + testPlayer.id, PlayerRoot + testPlayer.id + '/' + testPlayer.sessionId, 'DELETE', false);
}

function pass() {
    passedCount++;
    log(testName + ' Passed.');
}

function failed(err) {
    failedCount++;
    if (err) {
        log(testName + ":" + err);
        errors.push(err);
    }
}

function getRandomNumber() {
    randomId = Math.floor((Math.random()*10000)+1); 
    return randomId;
}