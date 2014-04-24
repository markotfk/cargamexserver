// Author     : Marko Karjalainen <markotfk@gmail.com>

var passedCount = 0;
var failedCount = 0;
var errors = [];
var testName = "";
var testPlayer = {};
var testTeam = {};
var testTrack = {};
var testTrackRecord = {};
var receiveData = false;
var findTeamByPlayerId = false;
var randomId = 0;

$(document).ready(function() {
    runTests();
});

function runTests() {
    passedCount = 0;
    failedCount = 0;
    testName = "runTests";
    try {
        testTrackRecordRestApi();
    } catch (err) {
        failed(err);
    }
    try {
        testPlayerRestApi();
    } catch (err) {
        failed(err);
    }
    try {
        testTrackRestApi();
    } catch (err) {
        failed(err);
    }
    try {
        testTeamRestApi();
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

function createTestTrack() {
    var rand = getRandomNumber();
    testTrack = new Track('TestTrack' + rand, 'TestTrackDescription' + rand);
}

function createTestTrackRecord() {
    var testDate = new Date();
    testTrackRecord = new TrackRecord(new Date(testDate-1).getMilliseconds(), 
            testPlayer.id, testTrack.id);
}


function testTeamRestApi() {
    createTestPlayer();
    createTestTeam();

    addTestPlayer();
    loginTestPlayer();
    addTestTeam();
    findTeamByPlayer();
    findTeamByName();
    //deleteTestTeam();
    //deleteTestPlayer();
}

function testTrackRestApi() {
    createTestPlayer();
    addTestPlayer();
    loginTestPlayer();
    createTestTrack();
    // test add new track
    addTestTrack();
    
    findTrackByName();
    // test remove track
    //removeTestTrack();
    //deleteTestPlayer();
}
function testTrackRecordRestApi() {
    // initialization
    createTestPlayer();
    addTestPlayer();
    loginTestPlayer();
    
    createTestTeam();
    addTestTeam();
    
    createTestTrack();
    addTestTrack();
    // Track record object needs to be initialized last since it
    //  needs player.id and track.id
    createTestTrackRecord();
    
    // tests
    addTestTrackRecord();
    findTrackRecordsByPlayer();
    findTrackRecordsByTrack();
    
    removeTestTrackRecord();
    // cleanup
    //deleteTestPlayer();
}

function addTestTrack() {
    ajaxCallTrack('Add Test Track ' + testTrack.name, TrackRoot + testPlayer.id + '/' + testPlayer.sessionId, 'POST', true);
}

function removeTestTrack() {
    ajaxCallTrack('Remove Test Track ' + testTrack.name, TrackRoot + testPlayer.id + '/' + testPlayer.sessionId + '/' + testTrack.id, 'DELETE', false);
}

function addTestTrackRecord() {
    ajaxCallTrackRecord('Add Test Track Record', TrackRecordRoot + testPlayer.id + '/' + 
            testPlayer.sessionId + '/' + testTrack.id, 'POST', true);
}

function removeTestTrackRecord() {
    ajaxCallTrackRecord('Remove Test Track Record ' + testTrackRecord.id, TrackRecordRoot + 'remove/' + 
            testTrackRecord.id + '/' + testPlayer.id + '/' + 
            testPlayer.sessionId, 'POST', false);
}

function testPlayerRestApi() {
    createTestPlayer();
    addTestPlayer();
    loginTestPlayer();
    logoutTestPlayer();
    
    testFindByLogin();
    // Delete player
    // password must be reset for login
    //testPlayer.password = 'password' + randomId;
    //ajaxCallPlayer('Delete Player 1st step (Login Player)', PlayerRoot + 'login', 'POST', true);
    //deleteTestPlayer();
}

function ajaxCallTrackRecord(name, url, type, dataReceived) {
    testName = name;
    receiveData = dataReceived;
    $.ajax(url, {
        contentType: 'application/json',
        type: type,
        async: false,
        success: ajaxPassTrackRecord,
        error: ajaxFailTrackRecord,
        data: JSON.stringify(testTrackRecord)
    });
}

function ajaxCallTrack(name, url, type, dataReceived) {
    testName = name;
    receiveData = dataReceived;
    $.ajax(url, {
        contentType: 'application/json',
        type: type,
        async: false,
        success: ajaxPassTrack,
        error: ajaxFailTrack,
        data: JSON.stringify(testTrack)
    });
}

function ajaxPassTrackRecord(data, status, jqXHR) {
    if (receiveData) {
        try {
            testTrackRecord.id = data.id;
            testTrackRecord.created = data.created;
        } catch (err) {
            failed(err);
            return;
        }
    }
    pass();
}

function ajaxFailTrackRecord(jqXHR, textStatus, errorString) {
    failed(errorString);
}


function ajaxPassTrack(data, status, jqXHR) {
    if (receiveData) {
        try {
            testTrack.id = data.id;
            testTrack.created = data.created;
        } catch (err) {
            failed(err);
            return;
        }
    }
    pass();
}

function ajaxFailTrack(jqXHR, textStatus, errorString) {
    failed(errorString);
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


function ajaxPassPlayer(data, status, jqXHR) {
    if (receiveData) {
        try {
            // Get id, sessionId and password generated in server side
            testPlayer.id = data.id;
            testPlayer.created = data.created;
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

function ajaxPassTeam(data, status, jqXHR) {
    if (receiveData) {
        try {
            if (findTeamByPlayerId) {
                findTeamByPlayerId = false;
                if (data.id === testTeam.id) {
                    log('findByOwnerId: succeed finding team:', testTeam.name);
                    pass();
                    return;
                } else {
                    failed('findByOwnerId Team is not found');
                    return;
                }
            } else {
                testTeam.created = data.created;
                // Get id generated in server side
                testTeam.id = data.id;
            }
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

function findTeamByPlayer() {
    // Find team by owner id
    findTeamByPlayerId = true;
    ajaxCallTeam('Find Team by Player id ' + testPlayer.id, TeamRoot + 'findByPlayerId/' +
            testPlayer.id, 'GET', true);
}


function findTrackRecordsByPlayer() {
    ajaxCallTrackRecord('Find track records by player ' + testPlayer.id, TrackRecordRoot + 'findByPlayer/' +
            testPlayer.id, 'GET', false);
}
function findTrackRecordsByTrack() {
    ajaxCallTrackRecord('Find track records by track ' + testTrack.id, TrackRecordRoot + 'findByTrack/' +
            testTrack.id, 'GET', false);
}

function deleteTestTeam() {
    // Add team
    ajaxCallTeam('Delete Team ' + testTeam.name, TeamRoot + 'remove/' + testPlayer.id + '/' + testPlayer.sessionId
    + '/' + testTeam.id, 'POST', false);
}

function addTestPlayer() {
    // First login test player to allow creating new team
    ajaxCallPlayer('Add New Player ' + testPlayer.login, PlayerRoot, 'POST', false); 
}

function loginTestPlayer() {
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

function testFindByLogin() {
    ajaxCallPlayer('Find by Login ' + testPlayer.login, PlayerRoot + 'findByLogin/' + testPlayer.login, 'GET', false);
}

function findTrackByName() {
    ajaxCallTrack('Find Track by Name ' + testTrack.name, TrackRoot + 'findByName/' + testTrack.name, 'GET', false);
}

function findTeamByName() {
    ajaxCallTeam('Find Team by Name ' + testTeam.name, TeamRoot + 'findByName/' + testTeam.name, 'GET', false);
}

function pass() {
    passedCount++;
    log(testName + ' Passed.');
}

function failed(err) {
    failedCount++;
    if (err) {
        log(testName + ":" + err);
        errors.push(testName + ":" + err);
    }
}

function getRandomNumber() {
    randomId = Math.floor((Math.random()*10000)+1); 
    return randomId;
}