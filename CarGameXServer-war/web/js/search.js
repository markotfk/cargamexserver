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
            html += '<a href="' + urlPart + players[i].id + '">' +  players[i].login + '</a><br>';
        }
        html += '</p>';
        $('#player_search_results').html(html);
    }
}

function searchFailed(jqXHR, textStatus, errorString) {
    log('Search failed:' + errorString);
    $('#player_search_results').html('<p>Search failed: ' + errorString + '</p>');
}
