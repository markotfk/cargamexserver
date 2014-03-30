/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.maguz.cargamex.ejb;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import org.maguz.cargamex.entities.Player;
import org.maguz.cargamex.entities.Team;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Stateless
public class TeamManagementBean extends ManagementBean implements TeamManagementBeanLocal {

    
    @Override
    public StatusCode add(Team team, Long playerId, String sessionId) {
        if (team == null) {
            return StatusCode.NotFound;
        }
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        
        team.setCreated(System.currentTimeMillis());
        Player owner = em.find(Player.class, playerId);
        // Add only if player does not have a team already
        if (owner != null && owner.checkSessionId(sessionId)) {
            if (owner.getTeam() == null) {
                team.setOwner(owner);
            } else {
                return StatusCode.DuplicateEntry;
            }
        } else {
            return StatusCode.AuthenticationFailed;
        }
        try {
            em.persist(team);
            owner.setTeam(team);
            em.persist(owner);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Adding Team failed:{0}", e.getMessage());
            return StatusCode.DuplicateEntry;
        }
        
        return StatusCode.OK;
    }

    @Override
    public StatusCode remove(Team entity, Long playerId, String sessionId) {
        if (entity == null) {
            return StatusCode.NotFound;
        }
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        
        Team existingTeam = find(entity.getId());
        if (existingTeam != null) {
            Player existingPlayer = em.find(Player.class, playerId);
            if (existingPlayer != null && existingPlayer.checkSessionId(sessionId)) {
                // Only allow owners to remove team
                if (existingTeam.isOwner(existingPlayer)) {
                    existingPlayer.setTeam(null);
                    em.remove(em.merge(existingTeam));
                    return merge(existingPlayer);
                }
            }
        }
        return StatusCode.AuthenticationFailed;
    }

    @Override
    public StatusCode edit(Team entity, Long playerId, String sessionId) {
        if (entity == null) {
            return StatusCode.NotFound;
        }
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        Player existingPlayer = em.find(Player.class, playerId);
        if (existingPlayer != null && existingPlayer.checkSessionId(sessionId)) {
            Team existingTeam = find(entity.getId());
            if (existingTeam != null && existingTeam.isAdmin(existingPlayer)) {
                return merge(entity);
            }
        }
        return StatusCode.AuthenticationFailed;
    }

    @Override
    public Team find(Long id) {
        return em.find(Team.class, id);
    }

    @Override
    public List<Team> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Team.class));
        return em.createQuery(cq).getResultList();
    }
}
