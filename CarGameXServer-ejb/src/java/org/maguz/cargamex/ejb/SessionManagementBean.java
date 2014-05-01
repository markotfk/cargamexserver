package org.maguz.cargamex.ejb;

import javax.ejb.Stateless;
import org.maguz.cargamex.entities.Player;

/**
 * Session handling for keeping player sessions alive.
 */
@Stateless
public class SessionManagementBean extends ManagementBean implements SessionManagementBeanLocal {

    public SessionManagementBean() {
    }
    
    /**
     * Update player's last activity time stamp in database.
     * @param player
     * @return
     */
    @Override
    public StatusCode keepAlive(Player player) {
        if (checkPlayer(player) == StatusCode.OK) {
            Player existing = em.find(Player.class, player.getId());
            if (existing != null) {
                existing.setLastActivity(System.currentTimeMillis());
                return merge(existing);
            } else {
                return StatusCode.NotFound;
            }
        }
        return StatusCode.AuthenticationFailed;
    }
}
