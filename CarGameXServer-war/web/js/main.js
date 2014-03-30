/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

DEBUG = true;
Version = 'v1';
RestDir = '/carx/rest/' + Version + '/';
PlayerRoot = RestDir + 'players';
TeamRoot = RestDir + 'teams';

function log(message) {
    if (DEBUG) {
        console.log(message);
    }
}
