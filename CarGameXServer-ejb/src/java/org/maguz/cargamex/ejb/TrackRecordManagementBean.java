package org.maguz.cargamex.ejb;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import org.maguz.cargamex.entities.TrackRecord;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Stateless
public class TrackRecordManagementBean extends ManagementBean implements TrackRecordManagementBeanLocal {

    @Override
    public StatusCode add(TrackRecord record, Long playerId, String sessionId) {
        if (record == null) {
            return StatusCode.NotFound;
        }
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (checkPlayer(playerId, sessionId) == StatusCode.OK) {
            try {
                em.persist(record);
                return StatusCode.OK;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error while adding track record:{0}", e.getMessage());
                return StatusCode.DuplicateEntry;
            }
        }
        return StatusCode.AuthenticationFailed;
    }

    @Override
    public StatusCode remove(Long recordId, Long playerId, String sessionId) {
        if (checkPlayer(playerId, sessionId) == StatusCode.OK) {
            try {
                TrackRecord record = em.find(TrackRecord.class, recordId);
                if (record != null) {
                    em.remove(record);
                    return StatusCode.OK;
                } else {
                    return StatusCode.NotFound;
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error while removing track record:{0}", e.getMessage());
                return StatusCode.Forbidden;
            }
        }
        return StatusCode.AuthenticationFailed;
    }

    @Override
    public StatusCode edit(TrackRecord record, Long playerId, String sessionId) {
        if (record == null) {
            return StatusCode.NotFound;
        }
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        
        if (checkPlayer(playerId, sessionId) == StatusCode.OK) {
            return merge(record);
        }
        return StatusCode.AuthenticationFailed;
    }

    @Override
    public TrackRecord find(Long id) {
        if (id == null) {
            return null;
        }
        return em.find(TrackRecord.class, id);
    }

    @Override
    public List<TrackRecord> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(TrackRecord.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<TrackRecord> findAllByPlayerId(Long playerId) {
        return null;
    }

    @Override
    public List<TrackRecord> findAllByTrackId(Long trackId) {
        return null;
    }

}
