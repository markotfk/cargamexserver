// Author     : Marko Karjalainen <markotfk@gmail.com>

$(document).ready(function() {
    initSearchForm();
});

function initSearchForm() {
    $('#player_search_form').submit(function(event) {
        event.preventDefault();
        var searchStr = $('#search_string').val().trim();
        var url = PlayerRoot + 'findByLogin/' + searchStr;
        ajaxCallSearch(url, "GET", searchStr);
    });
}

function ajaxCallSearch(url, type) {
    $.ajax(url, {
        contentType: 'application/json',
        type: type,
        success: searchSucceed,
        error: searchFailed
    });
}

function searchSucceed(data, status, jqXHR) {
    if (!data) {
        log('Search got no data');
        $('#player_search_results').html('No Data');
    } else {
        var players = data;
        log('Search found players: ' + players.length);
        var html = '<p>Found ' + players.length + ' players<br>';
        var urlPart = '/rest/v1/players/';
        for (var i = 0; i < players.length; ++i) {
            if (players[i].isActive) {
                html += '<b>';
            }
            html += '<a href="" onClick="return onClickPlayer(' + players[i].id + ');">' +  players[i].login + '</a><br>';
            if (players[i].isActive) {
                html += '</b>';
            }
        }
        html += '</p>';
        $('#player_search_results').html(html);
    }
}

function searchFailed(jqXHR, textStatus, errorString) {
    log('Search failed:' + errorString);
    $('#player_search_results').html('<p>Search failed: ' + errorString + '</p>');
}

function onClickPlayer(playerId) {
    log('Get player data for ' + playerId);
    var url = PlayerRoot + playerId;
    
    $.ajax(url, {
        contentType: 'application/json',
        type: 'GET',
        success: getPlayerDataSucceed,
        error: getPlayerFailed
    });
    return false;
}

function getPlayerFailed(jqXHR, textStatus, errorString) {
    log('get player data failed: ' + errorString);
    $('#player_data').html('<p>Get player data failed: ' + errorString + '</p>');
}

function getPlayerDataSucceed(data, status, jqXHR) {
    if (!data) {
        log('Get player data got no data');
        $('#player_data').html('No Data');
    } else {
        log('getPlayerData found: ' + data.login);
        var html = '<p>Player: ' + data.login + '<br>';
        html += 'Registered: ' + new Date(data.created).toLocaleString() + '<br>';
        if (data.isActive) {
            html += '<b>Logged in</b>' + '<br>';
        } else {
            html += 'Not Logged in' + '<br>';
        }
        
        html += '</p>';
        $('#player_data').html(html);
    }
}

