/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
            throw new NullPointerException("playerId is null");
        }
        if (sessionId == null) {
            throw new NullPointerException("sessionId is null");
        }
        if (track == null) {
            throw new NullPointerException("track is null");
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
        return StatusCode.OK;
    }

    @Override
    public StatusCode remove(Long playerId, String sessionId, Long trackId) {
        return StatusCode.OK;
    }

    @Override
    public Track find(Long id, Track track) {
        return null;
    }

    @Override
    public Track find(Long id, String sessionId) {
        return null;
    }

    @Override
    public List<Track> findAll(Track track) {
        List<Track> allTracks = new ArrayList<>();
        return allTracks;
    }
}
