/**
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
package org.maguz.cargamex.ejb;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import org.maguz.cargamex.entities.Player;

@Stateless
public class PlayerManagementBean extends ManagementBean implements PlayerManagementBeanLocal {

    // Add player instance to database.

    @Override
    public StatusCode add(Player player) {
        log(Level.INFO, "Add player");
        StatusCode code = checkNewPlayer(player);
        if (code != StatusCode.OK) {
            return code;
        }
        log(Level.INFO, "New player login " + player.getLogin());
        try {
            player.setCreated(System.currentTimeMillis());
            player.setPassword(player.getPassword()); // hashes plain-text password
            player.setLastActivity(System.currentTimeMillis());
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
        if (player.getPassword() == null || player.getPassword().length() < 6) {
            log(Level.WARNING, String.format("Cannot add player %s: too short password!", player.getLogin()));
            return StatusCode.Forbidden;
        }
        log(Level.INFO, String.format("checkNewPlayer %s", player.getLogin()));
        Player existing = findByLogin(player.getLogin());
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
        Player existing = findByLogin(player.getLogin());
        if (existing == null) {
            log(Level.INFO, "Existing player not found, return AuthenticationFailed");
            return StatusCode.AuthenticationFailed;
        }
        if (!login && !existing.getPassword().equals(player.getPassword())) {
            return StatusCode.AuthenticationFailed;
        }
        if (login && !existing.checkPassword(player.getPassword())) {
            return StatusCode.AuthenticationFailed;
        }
        if (!login) {
            if (existing.checkSessionId(player.getSessionId())) {
                existing.setSessionId(null);
                StatusCode code = merge(existing);
                player.setSessionId(null);
                return code;
            }
            else {
                return StatusCode.AuthenticationFailed;
            }
        } else {
            existing.setSessionId(player.getSessionId());
            existing.setLastActivity(System.currentTimeMillis());
            StatusCode code = merge(existing);
            player.setPasswordNoHash(existing.getPassword());
            player.setId(existing.getId());
            return code;
        }
    }
    
    private Player findByLogin(String login) {
        if (login == null) {
            return null;
        }
        log(Level.INFO, "findByLogin " + login);
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Player.class));
        List<Player> players = em.createQuery(cq).getResultList();
        for (Player p : players) {
            if (p.getLogin().equals(login)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public StatusCode edit(Long id, Player player) {
        if (id == null) {
            return StatusCode.NotFound;
        }
        if (player == null) {
            return StatusCode.NotFound;
        }
        log(Level.INFO, "Edit player " + id);
        if (checkPlayer(id, player.getSessionId()) == StatusCode.OK) {
            return merge(player);
        }
        return StatusCode.AuthenticationFailed;
    }

    @Override
    public StatusCode remove(Long id, String sessionId) {
        if (checkPlayer(id, sessionId) == StatusCode.OK) {
            log(Level.INFO, "Remove player " + id);
            Player existing = find(id);
            if (existing != null) {
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
        return em.find(Player.class, id);
    }

    @Override
    public List<Player> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Player.class));
        return em.createQuery(cq).getResultList();
    }
}
