package org.maguz.cargamex.ejb;

import javax.ejb.Stateless;
import org.maguz.cargamex.entities.Player;

/**
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Stateless
public class SessionManagementBean extends ManagementBean implements SessionManagementBeanLocal {

    public SessionManagementBean() {
    }
    
    @Override
    public StatusCode keepAlive(Player player) {
        if (player == null) {
            return StatusCode.NotFound;
        }
        if (player.getSessionId() == null) {
            return StatusCode.AuthenticationFailed;
        }
        Player existing = em.find(Player.class, player.getId());
        if (existing != null) {
            String session = existing.getSessionId();
            if (session != null && session.equals(player.getSessionId())) {
                existing.setLastActivity(System.currentTimeMillis());
                return merge(existing);
            }
        }
        return StatusCode.AuthenticationFailed;
    }
}
