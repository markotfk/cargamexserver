/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.maguz.cargamex.ejb;

import java.util.List;
import javax.ejb.Local;
import org.maguz.cargamex.entities.Track;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Local
public interface TrackManagementBeanLocal {
    /**
     * Add player object.
     * @param playerId
     * @param sessionId
     * @param track New track instance
     * @return StatusCode.OK if succeeded adding new player, 
     * StatusCode.DuplicateEntry if player with same login already present.
     * StatusCode.Error in case of other errors.
     */
    StatusCode add(Long playerId, String sessionId, Track track);
    
    /**
     * Edit player details.
     * @param playerId
     * @param sessionId
     * @param track
     * @return 
     */
    StatusCode edit(Long playerId, String sessionId, Track track);
    
    /**
     * Remove player from database.
     * @param playerId
     * @param sessionId
     * @param trackId
     * @return 
     */
    StatusCode remove(Long playerId, String sessionId, Long trackId);
    
    /**
     * Finds single track by id.
     * @param id
     * @param track
     * @return
     */
    Track find(Long id, Track track);
    
    /**
     * Finds single player by player id and session id.
     * @param id
     * @param sessionId
     * @return
     */
    Track find(Long id, String sessionId);
    
    /**
     * Returns list of players
     * @param track
     * @return 
     */
    List<Track> findAll(Track track);
}
