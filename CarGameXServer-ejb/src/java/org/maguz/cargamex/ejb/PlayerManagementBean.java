/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.maguz.cargamex.ejb;

import java.util.List;
import javax.ejb.Stateless;
import org.maguz.cargamex.entities.Player;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Stateless
public class PlayerManagementBean extends ManagementBean implements PlayerManagementBeanLocal {

    // Add player instance to database.

    @Override
    public StatusCode add(Player player) {
        if (player == null) {
            return StatusCode.NotFound;
        }
        try {
            player.setCreated(System.currentTimeMillis());
            player.setPassword(player.getPassword()); // hashes plain-text password
            em.persist(player);
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
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
        Player existing = findByLogin(player.getLogin());
        if (existing == null) {
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
            StatusCode code = merge(existing);
            player.setPasswordNoHash(existing.getPassword());
            player.setId(existing.getId());
            return code;
        }
    }
    
    private Player findByLogin(String login) {
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
        Player existing = find(id, player.getSessionId());
        if (existing != null) {
            return merge(player);
        }
        return StatusCode.AuthenticationFailed;
    }

    @Override
    public StatusCode remove(Player player) {
        return super.remove(player);
    }
    
    @Override
    public Player find(Long id, Player player) {
        if (id == null) {
            return null;
        }
        if (player == null) {
            return null;
        }
        Player existing = em.find(Player.class, id);
        if (existing != null && existing.checkSessionId(player.getSessionId())) {
            return existing;
        }
        return null;
    }

    @Override
    public Player find(Long id, String sessionId) {
        if (id == null) {
            return null;
        }
        if (sessionId == null) {
            return null;
        }
        Player existing = em.find(Player.class, id);
        if (existing != null && existing.checkSessionId(sessionId)) {
            return existing;
        }
        return null;
    }
    
    @Override
    public List<Player> findAll(Player player) {
        if (player == null) {
            return null;
        }
        Player existing = find(player.getId(), player);
        if (existing != null) {
            javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Player.class));
            return em.createQuery(cq).getResultList();
        }
        return null;
    }
}
