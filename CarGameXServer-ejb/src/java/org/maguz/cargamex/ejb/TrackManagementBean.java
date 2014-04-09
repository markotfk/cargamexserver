package org.maguz.cargamex.ejb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import org.maguz.cargamex.entities.Player;
import org.maguz.cargamex.entities.Track;

/**
 *
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
        Player p = em.find(Player.class, playerId);
        if (p == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (!p.getSessionId().equals(sessionId)) {
            return StatusCode.AuthenticationFailed;
        }
        track.setCreated(System.currentTimeMillis());
        try {
            em.persist(track);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return StatusCode.Error;
        }
        return StatusCode.OK;
    }

    @Override
    public StatusCode edit(Long playerId, String sessionId, Track track) {
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (track == null) {
            return StatusCode.NotFound;
        }
        Player player = em.find(Player.class, playerId);
        if (player != null && player.checkSessionId(sessionId)) {
            return merge(track);
        }
        return StatusCode.AuthenticationFailed;
    }

    @Override
    public StatusCode remove(Long playerId, String sessionId, Long track) {
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (track == null) {
            return StatusCode.NotFound;
        }
        Player player = em.find(Player.class, playerId);
        if (player != null && player.checkSessionId(sessionId)) {
            return remove(find(track));
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
}
