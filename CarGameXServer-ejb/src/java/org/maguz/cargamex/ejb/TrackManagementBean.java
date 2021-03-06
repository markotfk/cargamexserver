package org.maguz.cargamex.ejb;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.maguz.cargamex.entities.Player;
import org.maguz.cargamex.entities.Track;

/**
 * Track handling bean.
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Stateless
public class TrackManagementBean extends ManagementBean implements TrackManagementBeanLocal {

    @Override
    public StatusCode add(Long playerId, String sessionId, Track track) {
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (track == null) {
            return StatusCode.NotFound;
        }
        log(Level.INFO, String.format("Add track %s, by player %d.", track.getName(), playerId));
        Player p = em.find(Player.class, playerId);
        if (p == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (!p.checkSessionId(sessionId)) {
            return StatusCode.AuthenticationFailed;
        }
        try {
            track.initializeNew();
            em.persist(track);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return StatusCode.Error;
        }
        return StatusCode.OK;
    }

    @Override
    public StatusCode remove(Long playerId, String sessionId, Long trackId) {
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (trackId == null) {
            return StatusCode.NotFound;
        }
        log(Level.INFO, String.format("Remove track %d, by player %d.", trackId, playerId));
        Player player = em.find(Player.class, playerId);
        if (player != null && player.checkSessionId(sessionId)) {
            return remove(find(trackId));
        }
        return StatusCode.AuthenticationFailed;
    }

    @Override
    public Track find(Long id) {
        if (id == null) {
            return null;
        }
        return em.find(Track.class, id);
    }

    @Override
    public List<Track> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Track.class));
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public List<Track> findByName(String name) {
        if (name == null) {
            return null;
        }
        log(Level.INFO, "findByName " + name);
        TypedQuery<Track> query = em.createNamedQuery("Track.findTrackByName", Track.class);
        query.setParameter("name", name + "%");
        query.setMaxResults(100);
        return query.getResultList();
    }
}
