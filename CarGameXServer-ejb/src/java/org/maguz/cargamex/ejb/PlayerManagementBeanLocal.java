/**
 * @author Marko Karjalainen <markotfk@gmail.com>
 */

package org.maguz.cargamex.ejb;

import java.util.List;
import javax.ejb.Local;
import org.maguz.cargamex.entities.Player;

@Local
public interface PlayerManagementBeanLocal {

    /**
     * Add player object.
     * @param player New player instance
     * @return StatusCode.OK if succeeded adding new player, 
     * StatusCode.DuplicateEntry if player with same login already present.
     * StatusCode.Error in case of other errors.
     */
    StatusCode add(Player player);
    
    /**
     * Log in Player.
     * @param player Player login
     * @return Status code.
     */
    StatusCode login(Player player);
    
    /**
     * Log out Player.
     * @param player Player login.
     * @return Status code.
     */
    StatusCode logout(Player player);
    
    /**
     * Edit player details.
     * @param id
     * @param player
     * @return 
     */
    StatusCode edit(Long id, Player player);
    
    /**
     * Remove player from database.
     * @param player
     * @return 
     */
    StatusCode remove(Player player);
    
    /**
     * Finds single player by id.
     * @param id
     * @param player
     * @return
     */
    Player find(Long id, Player player);
    
    /**
     * Finds single player by player id and session id.
     * @param id
     * @param sessionId
     * @return
     */
    Player find(Long id, String sessionId);
    
    /**
     * Returns list of players
     * @return 
     */
    List<Player> findAll();
}
