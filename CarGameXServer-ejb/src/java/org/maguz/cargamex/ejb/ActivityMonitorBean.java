/**
 * @author Marko Karjalainen <markotfk@gmail.com>
 */

package org.maguz.cargamex.ejb;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import org.maguz.cargamex.entities.Player;

/**
 * Periodically checks player activity and resets session id to null
 * if player has been inactive for too long.
 */
@Singleton
@LocalBean
public class ActivityMonitorBean extends ManagementBean {

    private static final int PLAYER_TIMEOUT_MINS = 60;
    
    @EJB 
    PlayerManagementBeanLocal pm;
    
    /**
     * Player session inactivity check.
     */
    @Schedule(minute = "0-59", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "0-23", dayOfWeek = "*")
    public void playerSessionActivityCheck() {
        List<Player> activePlayers = pm.findByActiveSession();
        if (activePlayers == null) {
            return;
        }
        for (Player p : activePlayers) {
            Long lastActivity = p.getLastActivity();
            if (lastActivity == null) {
                if (p.getSessionId() != null) {
                    p.setSessionId(null);
                    StatusCode code = merge(p);
                    if (code != StatusCode.OK) {
                        log(Level.SEVERE, String.format("playerSessionActivityCheck: player %s, error merging: %d", 
                                p.getLogin(), code));
                    }
                }
                
            } else {
                if (timeDifferenceTooBig(lastActivity)) {
                    log(Level.INFO, String.format("Player %s session expired.", p.getLogin()));
                    p.setSessionId(null);
                    StatusCode code = merge(p);
                    if (code != StatusCode.OK) {
                        log(Level.SEVERE, String.format("playerSessionActivityCheck: error merging: %d", code));
                    }
                }
            }
        }
    }
    
    private boolean timeDifferenceTooBig(long lastActivity) {
        long differenceMs = System.currentTimeMillis() - lastActivity;
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = differenceMs / daysInMilli;
        if (elapsedDays > 0) {
            return true;
        }
        differenceMs = differenceMs % daysInMilli;
        long elapsedHours = differenceMs / hoursInMilli;
        if (elapsedHours > 0) {
            return true;
        }
        
        differenceMs = differenceMs % hoursInMilli;
        long elapsedMinutes = differenceMs / minutesInMilli;
        return elapsedMinutes >= PLAYER_TIMEOUT_MINS;
    }
}
