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

    /**
     * Add new team.
     * Requires valid player id and session id.
     * @param team
     * @param playerId
     * @param sessionId
     * @return
     */
    @Override
    public StatusCode add(Team team, Long playerId, String sessionId) {
        if (team == null) {
            return StatusCode.NotFound;
        }
        if (team.getName() == null || team.getName().trim().length() == 0) {
            // Must have name
            return StatusCode.Forbidden;
        }
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        log(Level.INFO, String.format("Add team %s", team.getName()));
        Player owner = em.find(Player.class, playerId);
        // Add only if player does not have a team already
        if (owner != null && owner.checkSessionId(sessionId)) {
            if (owner.getTeam() == null) {
                team.setOwner(owner);
                team.addAdmin(owner);
                team.addPlayer(owner);
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
            log(Level.SEVERE, String.format("Adding Team failed: %s", e.getMessage()));
            return StatusCode.DuplicateEntry;
        }
        
        return StatusCode.OK;
    }

    /**
     * Removes a team.
     * Requires valid player id and session id.
     * @param teamId
     * @param playerId
     * @param sessionId
     * @return
     */
    @Override
    public StatusCode remove(Long teamId, Long playerId, String sessionId) {
        if (teamId == null) {
            return StatusCode.NotFound;
        }
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        log(Level.INFO, String.format("Remove team: %d", teamId));
        Team existingTeam = find(teamId);
        if (existingTeam != null) {
            Player existingPlayer = em.find(Player.class, playerId);
            if (existingPlayer != null && existingPlayer.checkSessionId(sessionId)) {
                // Only allow owners to remove team
                if (existingTeam.isOwner(existingPlayer)) {
                    for (Player p : existingTeam.getPlayers()) {
                        p.setTeam(null);
                    }
                    remove(existingTeam);
                    return merge(existingPlayer);
                }
            }
        }
        return StatusCode.AuthenticationFailed;
    }

    /**
     * Find team by team id.
     * @param id
     * @return
     */
    @Override
    public Team find(Long id) {
        return em.find(Team.class, id);
    }

    /**
     * Find player's team.
     * @param playerId
     * @return
     */
    @Override
    public Team findByPlayerId(Long playerId) {
        if (playerId == null) {
            return null;
        }
        Player player = em.find(Player.class, playerId);
        if (player != null) {
            return player.getTeam();
        }
        return null;
    }
    
    /**
     * Get all teams.
     * @return
     */
    @Override
    public List<Team> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Team.class));
        return em.createQuery(cq).getResultList();
    }

    /**
     * Add player to team.
     * @param team
     * @param playerId
     * @param sessionId
     * @param newPlayerId
     * @return
     */
    @Override
    public StatusCode addPlayer(Team team, Long playerId, String sessionId, Long newPlayerId) {
        if (team == null) {
            return StatusCode.NotFound;
        }
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        log(Level.INFO, String.format("Add player %d to team: %d", playerId, team.getId()));
        Team existingTeam = find(team.getId());
        if (existingTeam != null) {
            Player player = em.find(Player.class, playerId);
            // player must be admin of team to add player
            if (player != null && player.checkSessionId(sessionId) &&
                    existingTeam.isAdmin(player)) {
                Player newPlayer = em.find(Player.class, newPlayerId);
                if (newPlayer != null) {
                    // First check if new player belongs to a team
                    if (newPlayer.getTeam() != null ) {
                        return StatusCode.DuplicateEntry;
                    }
                    existingTeam.addPlayer(newPlayer);
                    team.addPlayer(newPlayer);
                    newPlayer.setTeam(existingTeam);
                    StatusCode code = merge(newPlayer);
                    if (code == StatusCode.OK) {
                        code = merge(existingTeam);
                    }
                    return code;
                } else {
                    return StatusCode.NotFound;
                }
            }
        } else {
            return StatusCode.NotFound;
        }
        return StatusCode.AuthenticationFailed;
    }

    /**
     * Remove player from team.
     * @param team
     * @param playerId
     * @param sessionId
     * @param removePlayerId
     * @return
     */
    @Override
    public StatusCode removePlayer(Team team, Long playerId, String sessionId, Long removePlayerId) {
        if (team == null) {
            return StatusCode.NotFound;
        }
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        log(Level.INFO, String.format("Remove player %d from team: %d", playerId, team.getId()));
        Team existingTeam = find(team.getId());
        if (existingTeam != null) {
            Player player = em.find(Player.class, playerId);
            if (player != null && player.checkSessionId(sessionId)) {
                Player removePlayer = em.find(Player.class, removePlayerId);
                if (removePlayer != null && removePlayer.belongsTo(existingTeam)) {
                    if (existingTeam.isOwner(removePlayer)) {
                            // owner cannot be removed
                            return StatusCode.Forbidden;
                        }
                    if (removePlayer.equals(player) || existingTeam.isAdmin(player)) {
                        if (!existingTeam.removePlayer(removePlayer)) {
                            return StatusCode.NotFound;
                        } else {
                            existingTeam.removeAdmin(removePlayer);
                            team.removeAdmin(removePlayer);
                            removePlayer.setTeam(null);
                            StatusCode code = merge(removePlayer);
                            if (code == StatusCode.OK) {
                                code = merge(existingTeam);
                            }
                            return code;
                        }
                    }
                    
                } else {
                    return StatusCode.NotFound;
                }
            }
        } else {
            return StatusCode.NotFound;
        }
        return StatusCode.AuthenticationFailed;
    }

    /**
     * Add administrator to team.
     * @param team
     * @param playerId
     * @param sessionId
     * @param adminPlayerId
     * @return
     */
    @Override
    public StatusCode addAdmin(Team team, Long playerId, String sessionId, Long adminPlayerId) {
        if (team == null) {
            return StatusCode.NotFound;
        }
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (adminPlayerId == null) {
            return StatusCode.NotFound;
        }
        log(Level.INFO, String.format("Add admin player %d to team: %d", playerId, team.getId()));
        Team existingTeam = find(team.getId());
        if (existingTeam != null) {
            Player player = em.find(Player.class, playerId);
            // Admin can add other admins
            if (player != null && player.checkSessionId(sessionId) &&
                    existingTeam.isAdmin(player)) {
                Player adminPlayer = em.find(Player.class, adminPlayerId);
                if (adminPlayer != null && adminPlayer.belongsTo(existingTeam)) {
                    if (existingTeam.isOwner(adminPlayer)) {
                            // owner is always also admin
                            return StatusCode.DuplicateEntry;
                        }
                    existingTeam.addAdmin(adminPlayer);
                    team.addAdmin(adminPlayer);
                    return merge(existingTeam);
                } else {
                    return StatusCode.NotFound;
                }
            }
        } else {
            return StatusCode.NotFound;
        }
        return StatusCode.AuthenticationFailed;
    }

    /**
     * Remove administrator from team.
     * @param team
     * @param playerId
     * @param sessionId
     * @param adminPlayerId
     * @return
     */
    @Override
    public StatusCode removeAdmin(Team team, Long playerId, String sessionId, Long adminPlayerId) {
        if (team == null) {
            return StatusCode.NotFound;
        }
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (adminPlayerId == null) {
            return StatusCode.NotFound;
        }
        log(Level.INFO, String.format("Remove admin player %d from team: %d", playerId, team.getId()));
        Team existingTeam = find(team.getId());
        if (existingTeam != null) {
            Player player = em.find(Player.class, playerId);
            // Admin can remove other admins
            if (player != null && player.checkSessionId(sessionId) &&
                    existingTeam.isAdmin(player)) {
                Player adminPlayer = em.find(Player.class, adminPlayerId);
                if (adminPlayer != null && adminPlayer.belongsTo(existingTeam)) {
                    if (existingTeam.isOwner(adminPlayer)) {
                            // cannot de-admin owner
                            return StatusCode.Forbidden;
                        }
                    existingTeam.removeAdmin(adminPlayer);
                    team.removeAdmin(adminPlayer);
                    return merge(existingTeam);
                } else {
                    return StatusCode.NotFound;
                }
            }
        } else {
            return StatusCode.NotFound;
        }
        return StatusCode.AuthenticationFailed;
    }

    /**
     * Set team's owner.
     * @param team
     * @param playerId
     * @param sessionId
     * @param ownerPlayerId
     * @return
     */
    @Override
    public StatusCode setOwner(Team team, Long playerId, String sessionId, Long ownerPlayerId) {
        if (team == null) {
            return StatusCode.NotFound;
        }
        if (playerId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (sessionId == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (ownerPlayerId == null) {
            return StatusCode.NotFound;
        }
        log(Level.INFO, String.format("Set owner player %d to team: %d", playerId, team.getId()));
        Team existingTeam = find(team.getId());
        if (existingTeam != null) {
            Player player = em.find(Player.class, playerId);
            // Only current owner can change owner
            if (player != null && player.checkSessionId(sessionId) &&
                    existingTeam.isOwner(player)) {
                Player ownerPlayer = em.find(Player.class, ownerPlayerId);
                if (ownerPlayer != null && ownerPlayer.belongsTo(existingTeam)) {
                    if (existingTeam.isOwner(ownerPlayer)) {
                            // Already owner
                            return StatusCode.DuplicateEntry;
                        }
                    existingTeam.setOwner(ownerPlayer);
                    existingTeam.addAdmin(ownerPlayer);
                    team.setOwner(ownerPlayer);
                    team.addAdmin(ownerPlayer);
                    return merge(existingTeam);
                } else {
                    return StatusCode.NotFound;
                }
            }
        } else {
            return StatusCode.NotFound;
        }
        return StatusCode.AuthenticationFailed;
    }
}
