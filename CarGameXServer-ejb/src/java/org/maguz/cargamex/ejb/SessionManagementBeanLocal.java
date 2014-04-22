package org.maguz.cargamex.ejb;

import javax.ejb.Local;
import org.maguz.cargamex.entities.Player;

/**
 * Local interface for Session manager bean.
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Local
public interface SessionManagementBeanLocal {
    
    /**
     * Update player session activity.
     * @param player
     * @return
     */
    public StatusCode keepAlive(Player player);
}
