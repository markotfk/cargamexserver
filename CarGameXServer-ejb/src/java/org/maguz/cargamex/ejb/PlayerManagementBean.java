/**
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
package org.maguz.cargamex.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.maguz.cargamex.entities.Player;
import org.maguz.cargamex.entities.Team;

/**
 * Player entity handling.
 */
@Stateless
public class PlayerManagementBean extends ManagementBean implements PlayerManagementBeanLocal {

    @EJB 
    private TeamManagementBeanLocal tm;
    
    // Add player instance to database.

    @Override
    public StatusCode add(Player player) {
        StatusCode code = checkNewPlayer(player);
        if (code != StatusCode.OK) {
            return code;
        }
        log(Level.INFO, "Add new player " + player.getLogin());
        try {
            player.setPassword(player.getPassword().trim()); // hashes plain-text password
            player.setLastActivity(System.currentTimeMillis());
            player.setCreated(System.currentTimeMillis());
            em.persist(player);
        } catch (Exception ex) {
            log(Level.SEVERE, ex.getMessage());
            return StatusCode.DuplicateEntry;
        }
        return StatusCode.OK;
    }

    private StatusCode checkNewPlayer(Player player) {
        if (player == null) {
            return StatusCode.NotFound;
        }
        if (player.getLogin() == null || player.getLogin().trim().length() == 0) {
            log(Level.WARNING, "Cannot add player: login string empty!");
            return StatusCode.Forbidden;
        }
        if (player.getPassword() == null || player.getPassword().trim().length() < 6) {
            log(Level.WARNING, String.format("Cannot add player %s: too short password!", player.getLogin()));
            return StatusCode.Forbidden;
        }
        log(Level.INFO, String.format("checkNewPlayer %s", player.getLogin()));
        Player existing = findSingleByLogin(player.getLogin());
        if (existing != null) {
            log(Level.INFO, "Login is in use, return StatusCode.DuplicateEntry");
            return StatusCode.DuplicateEntry;
        }
        return StatusCode.OK;
    }
    @Override
    public StatusCode login(Player player) {
        return authenticatePlayer(player, true);
    }

    @Override
    public StatusCode logout(Player player) {
        return authenticatePlayer(player, false);
    }
    
    private StatusCode authenticatePlayer(Player player, boolean login) {
        if (player == null) {
            return StatusCode.NotFound;
        }
        log(Level.INFO, String.format("authenticatePlayer %s", player.getLogin()));
        Player existing = findSingleByLogin(player.getLogin());
        if (existing == null) {
            log(Level.INFO, "Existing player not found, return AuthenticationFailed");
            return StatusCode.AuthenticationFailed;
        }
        if (!login && !existing.checkSessionId(player.getSessionId())) {
            return StatusCode.AuthenticationFailed;
        }
        if (login && !existing.checkPassword(player.getPassword())) {
            return StatusCode.AuthenticationFailed;
        }
        player.setCreated(existing.getCreated());
        if (!login) {
            existing.setSessionId(null);
            player.setSessionId(null);
            return merge(existing);
        } else {
            existing.setSessionId(player.getSessionId());
            existing.setLastActivity(System.currentTimeMillis());
            StatusCode code = merge(existing);
            player.setPasswordNoHash("");
            player.setId(existing.getId());
            return code;
        }
    }
    
    private Player findSingleByLogin(String login) {
        List<Player> players = doFindByLogin(login, true);
        if (players != null && players.size() > 0) {
            return players.get(0);
        }
        return null;
    }

    @Override
    public StatusCode remove(Long id, String sessionId) {
        if (checkPlayer(id, sessionId) == StatusCode.OK) {
            log(Level.INFO, "Remove player " + id);
            Player existing = em.find(Player.class, id);
            if (existing != null) {
                if (existing.getTeam() != null) {
                    Team team = tm.find(existing.getTeam().getId());
                    StatusCode code = StatusCode.OK;
                    if (team.isOwner(existing)) {
                        // Removed player is owner, remove whole team
                        code = tm.remove(team.getId(), id, sessionId);
                    } else if (team.isAdmin(existing)) {
                        // Removed player is admin of team
                        code = tm.removeAdmin(team, id, sessionId, existing.getId());
                        if (code == StatusCode.OK) {
                            // Remove also from player list
                            code = tm.removePlayer(team, id, sessionId, existing.getId());
                        }
                    } else if (team.isPlayer(existing)) {
                        // Removed player is player of team
                        code = tm.removePlayer(team, id, sessionId, existing.getId());
                    }
                    if (code != StatusCode.OK) {
                        return code;
                    }
                }
                return super.remove(existing);
            } else {
                return StatusCode.NotFound;
            }
        }
        return StatusCode.AuthenticationFailed;
    }
    
    @Override
    public Player find(Long id) {
        if (id == null) {
            return null;
        }
        Player player = em.find(Player.class, id);
        return copyPlayer(player);
    }
    
    private Player copyPlayer(Player player) {
        if (player != null) {
            return new Player(player.getId(), player.getCreated(), player.getLogin(),
                player.getLastActivity(), player.getPoints());
        }
        return null;
    }
    
    private List<Player> copyPlayers(List<Player> players) {
        if (players != null) {
            List<Player> copy = new ArrayList<>();
            for (Player p : players) {
                copy.add(copyPlayer(p));
            }
            return copy;
        }
        return null;
    }

    @Override
    public List<Player> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Player.class));
        List<Player> players = em.createQuery(cq).getResultList();
        return copyPlayers(players);
    }

    @Override
    public List<Player> findByLogin(String login) {
        List<Player> players = doFindByLogin(login, false);
        return copyPlayers(players);
    }
    
    @Override
    public List<Player> findByActiveSession() {
        log(Level.INFO, "findByActiveSession");
        TypedQuery<Player> query = em.createNamedQuery("Player.findByActiveSession", Player.class);
        List<Player> players = query.getResultList();
        return players;
    }
    
    private List<Player> doFindByLogin(String login, boolean exactMatch) {
        if (login == null) {
            return null;
        }
        log(Level.INFO, "doFindByLogin " + login);
        TypedQuery<Player> query = em.createNamedQuery("Player.findByLogin", Player.class);
        final String searchLogin = exactMatch ? login : login + "%";
        query.setParameter("playerLogin", searchLogin);
        List<Player> players = query.getResultList();
        return players;
    }
}
