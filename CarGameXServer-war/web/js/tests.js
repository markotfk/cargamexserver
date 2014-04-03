/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var passed = 0;
var failed = 0;

$(document).ready(function() {
    passed = 0;
    failed = 0;
    try {
        testPlayerRestApi();
    } catch (Exception) {
        log('testPlayerRestApi threw exception');
    }
    
    showResults();
});

function testPlayerRestApi() {
    // Add player
    ajaxCall(PlayerRoot, 'POST',  
    {
        email : "test@test.com",
        name : "PlayerTest",
        password : "PlayerPassword"
    });
    // Login player that was added
    ajaxCall(PlayerRoot + 'login', 'POST',  
    {
        name : "PlayerTest",
        password : "PlayerPassword"
    });
}

function ajaxCall(url, type, data, expected) {
    $.ajax(url, {
        contentType: 'application/json',
        type: type,
        success: ajaxPass(url, data, expected),
        error: ajaxFail(url),
        data: JSON.stringify(data)
    });
}

function ajaxPass(url, data, expected) {
    if (expected) {
        if (data.expected === expected ) {
            log('test passed (data was expected):' + url);
            passed++;
        } else {
            log('test failed (data was NOT expected):' + url);
            failed++;
        }
    } else {
        log('test passed:' + url)
        passed++;
    }
    
}

function ajaxFail(url) {
    log('test failed:' + url)
    failed++;
}

function showResults() {
    $('#test_results').html('<p>Tests passed: ' + passed + '</p><br>' + 
            '<p>Tests failed: ' + failed + '</p><br>');
}