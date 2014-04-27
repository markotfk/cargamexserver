package org.maguz.cargamex.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.maguz.cargamex.entities.Player;

/**
 * Abstract base class for EJBs implementing common functionality.
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
public abstract class ManagementBean {
    
    DateFormat dateFormat;
    protected Calendar cal;
    protected static final Logger logger = Logger.getLogger(ManagementBean.class.getName());
    
    @PersistenceContext(unitName = "CarGameXServer-ejbPU")
    protected EntityManager em;
    
    protected ManagementBean() {
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        cal = Calendar.getInstance();
    }
    
    protected void log(Level level, String message) {
        logger.log(level, "{0}: {1}: {2}", new Object[]{dateFormat.format(cal.getTime()), 
            this.getClass().getSimpleName(), message});
    }
    protected StatusCode merge(Object entity) {
        if (entity == null) {
            return StatusCode.NotFound;
        }
        try {
            em.merge(entity);
        } catch (Exception e) {
            log(Level.SEVERE, String.format("Error merge: %s", e.getMessage()));
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
            log(Level.SEVERE, String.format("Error remove: %s", e.getMessage()));
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
