package org.maguz.cargamex.ejb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.maguz.cargamex.entities.Player;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
public abstract class ManagementBean {
    
    protected Logger logger;
    
    protected ManagementBean() {
        logger = Logger.getLogger(this.getClass().toString());
    }
    @PersistenceContext(unitName = "CarGameXServer-ejbPU")
    protected EntityManager em;
    
    
    protected StatusCode merge(Object entity) {
        if (entity == null) {
            return StatusCode.NotFound;
        }
        try {
            em.merge(entity);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error merge: {0}", e.getMessage());
            return StatusCode.AuthenticationFailed;
        }
        return StatusCode.OK;
    }
    
    protected StatusCode remove(Object entity) {
        if (entity == null) {
            return StatusCode.NotFound;
        }
        try {
            em.remove(em.merge(entity));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error remove: {0}", e.getMessage());
            return StatusCode.AuthenticationFailed;
        }
        return StatusCode.OK;
    }
    
    protected StatusCode checkPlayer(Long id, String sessionId) {
        if (id == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        Player p = em.find(Player.class, id);
        if (p != null && p.checkSessionId(sessionId)) {
            return StatusCode.OK;
        }
        return StatusCode.AuthenticationFailed;
    }
}
