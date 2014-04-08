package org.maguz.cargamex.ejb;

import javax.ejb.Local;
import org.maguz.cargamex.entities.Player;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Local
public interface SessionManagementBeanLocal {
    
    public StatusCode keepAlive(Player player);
}
