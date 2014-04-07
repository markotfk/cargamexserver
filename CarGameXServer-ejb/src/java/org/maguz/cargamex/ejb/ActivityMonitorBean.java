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

@Singleton
@LocalBean
public class ActivityMonitorBean extends ManagementBean {

    private static final int PLAYER_TIMEOUT_MINS = 10;
    
    @EJB 
    PlayerManagementBeanLocal pm;
    
    @Schedule(minute = "0-59", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "0-23", dayOfWeek = "*")
    public void playerSessionActivityCheck() {
        List<Player> allPlayers = pm.findAll(); // todo: find only players with valid sessionId
        for (Player p : allPlayers) {
            if (p.getSessionId() != null) {
                Long activity = p.getLastActivity();
                if (activity == null) {
                    p.setSessionId(null);
                    merge(p);
                } else {
                    if (timeDifferenceTooBig(System.currentTimeMillis() - activity)) {
                        logger.log(Level.INFO, "Player {0} session expired.", p.getLogin());
                        p.setSessionId(null);
                        merge(p);
                    }
                }
            }
        }
    }
    
    private boolean timeDifferenceTooBig(long differenceMs) {
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
