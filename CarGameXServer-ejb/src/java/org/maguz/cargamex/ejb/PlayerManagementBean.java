/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.maguz.cargamex.ejb;

import java.sql.Date;
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
        if (find(player.getLogin()) != null) {
            logger.warning(String.format("Rejecting duplicate player entry: '{0}'", player.getLogin()));
            return StatusCode.DuplicateEntry;
        }
        try {
            player.setRegistered(new Date(System.currentTimeMillis()));
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
        Player existing = find(player.getLogin());
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
                em.merge(existing);
                player.setSessionId(null);
            }
            else {
                return StatusCode.AuthenticationFailed;
            }
        } else {
            existing.setSessionId(player.getSessionId());
            em.merge(existing);
            player.setPasswordNoHash(existing.getPassword());
        }
        return StatusCode.OK;
    }

    @Override
    public StatusCode edit(Player player) {
        if (player == null) {
            return StatusCode.NotFound;
        }
        Player existing = find(player.getLogin());
        if (existing != null && existing.checkSessionId(player.getSessionId())) {
            em.merge(player);
            return StatusCode.OK;
        }
        return StatusCode.AuthenticationFailed;
    }

    @Override
    public StatusCode remove(Player player) {
        if (player == null) {
            return StatusCode.NotFound;
        }
        Player existing = find(player.getLogin());
        if (existing != null && existing.checkSessionId(player.getSessionId())) {
            em.remove(em.merge(existing));
            return StatusCode.OK;
        }
        return StatusCode.AuthenticationFailed;
    }
    
    @Override
    public Player find(String id) {
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
