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
     * @param id
     * @param sessionId
     * @return 
     */
    StatusCode remove(Long id, String sessionId);
    
    /**
     * Finds single player by id.
     * @param id
     * @return
     */
    Player find(Long id);
    
    /**
     * Returns list of players
     * @return 
     */
    List<Player> findAll();
    
    /**
     * Find list of players filtered by login text.
     * @param login
     * @return
     */
    List<Player> findAllByLogin(String login);
}
