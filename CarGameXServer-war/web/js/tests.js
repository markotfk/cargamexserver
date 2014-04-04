/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var passed = 0;
var failed = 0;
var errors = []
var testName = "";
var testPlayer = {};
var receiveData = false;

$(document).ready(function() {
    passed = 0;
    failed = 0;
    try {
        testPlayerRestApi();
    } catch (err) {
        log('testPlayerRestApi threw exception');
        errors.push(err);
    }
    
    showResults();
});

function testPlayerRestApi() {
    // Add player, add some randomness to avoid conflicts
    var rand = Math.floor((Math.random()*10000)+1); 
    testPlayer = new Player('test' + rand + '@test.com', 'test' + rand, 'password' + rand);

    ajaxCallPlayer('Add New Player', PlayerRoot, 'POST', testPlayer, false); 
    // Login player that was added
    ajaxCallPlayer('Login Player', PlayerRoot + 'login', 'POST', testPlayer, true);
    // Logout player that was added
    ajaxCallPlayer('Logout Player', PlayerRoot + 'logout', 'POST', testPlayer, false);
    
    // Delete player
    // password must be reset for login
    testPlayer.password = 'password' + rand;
    ajaxCallPlayer('Delete Player 1st step (Login Player)', PlayerRoot + 'login', 'POST', testPlayer, true);
    ajaxCallPlayer('Delete Player 2nd step', PlayerRoot + testPlayer.id + '/' + testPlayer.sessionId, 'DELETE', testPlayer, false);
}

function ajaxCallPlayer(name, url, type, testPlayer, dataReceived) {
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
            testPlayer.id = data.id;
            testPlayer.sessionId = data.sessionId;
            testPlayer.password = data.password;
        } catch (err) {
            log(testName + ":" + err);
            errors.push(testName + ":" + err);
            failed++;
            return;
        }
    }
    passed++;
    log(testName + ' Passed.');
}

function ajaxFailPlayer(jqXHR, textStatus, errorString) {
    failed++;
    log(testName + ":" + errorString);
    errors.push(testName + ":" + errorString);
}

function showResults() {
    $('#tests_summary').html('<p>Tests passed: ' + passed + '</p><br>' + 
            '<p>Tests failed: ' + failed + '</p><br>');
    var failedHtml = "<p>Failed:<br><ul>";
    for (var i = 0; i < errors.length; i++) {
        failedHtml += "<li>" + errors[i] + "</li><br>";
    }
    failedHtml += '</ul></p>';
    $('#tests_failed').html(failedHtml);
}