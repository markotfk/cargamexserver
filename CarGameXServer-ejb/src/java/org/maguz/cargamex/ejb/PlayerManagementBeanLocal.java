/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.maguz.cargamex.ejb;

import javax.ejb.Local;
import org.maguz.cargamex.entities.Player;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Local
public interface PlayerManagementBeanLocal {

    /**
     * Add player object.
     * @param player New player instance
     * @return StatusCode.OK if succeeded adding new player, 
     * StatusCode.DuplicateEntry if player with same login already present.
     * StatusCode.Error in case of other errors.
     */
    StatusCode addPlayer(Player player);
    
    
}
