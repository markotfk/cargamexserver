package org.maguz.cargamex.ejb;

import java.util.List;
import javax.ejb.Local;
import org.maguz.cargamex.entities.TrackRecord;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Local
public interface TrackRecordManagementBeanLocal {

    /**
     * Add track record for player.
     * @param record
     * @param playerId
     * @param sessionId
     * @return
     */
    StatusCode add(TrackRecord record, Long playerId, String sessionId);
    
    /**
     * Remove track record.
     * @param recordId
     * @param playerId
     * @param sessionId
     * @return
     */
    StatusCode remove(Long recordId, Long playerId, String sessionId);
    
    /**
     * Update existing track record.
     * @param record
     * @param playerId
     * @param sessionId
     * @return
     */
    StatusCode edit(TrackRecord record, Long playerId, String sessionId);
    
    /**
     * Find track record for given id.
     * @param id
     * @return
     */
    TrackRecord find(Long id);
    
    /**
     * Gets all track records.
     * @return
     */
    List<TrackRecord> findAll();
   
    /**
     * Gets all track records by player id.
     * @param playerId
     * @return
     */
    List<TrackRecord> findAllByPlayerId(Long playerId);
    
    /**
     * Gets all track records by trackId id.
     * @param trackId
     * @return
     */
    List<TrackRecord> findAllByTrackId(Long trackId);
}
