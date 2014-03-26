/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.maguz.cargamex.ejb;

import java.sql.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.maguz.cargamex.entities.Player;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Stateless
public class PlayerManagementBean implements PlayerManagementBeanLocal {

    private static final Logger logger = Logger.getLogger(PlayerManagementBean.class.toString());
    
    @PersistenceContext( unitName = "CarGameXServer-ejbPU")
    EntityManager em;
    
    // Add player instance to database.

    @Override
    public StatusCode addPlayer(Player player) {
        if (player == null) {
            return StatusCode.NotFound;
        }
        if (findPlayer(player.getLogin()) != null) {
            logger.warning(String.format("Rejecting duplicate player entry: '{0}'", player.getLogin()));
            return StatusCode.DuplicateEntry;
        }
        try {
            player.setRegistered(new Date(System.currentTimeMillis()));
            player.setPassword(player.getPassword()); // hashes plain-text password
            em.persist(player);
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            return StatusCode.Error;
        }
        return StatusCode.OK;
    }

    @Override
    public StatusCode loginPlayer(Player player) {
        return authenticatePlayer(player, true);
    }

    @Override
    public StatusCode logoutPlayer(Player player) {
        return authenticatePlayer(player, false);
    }
    
    private StatusCode authenticatePlayer(Player player, boolean loggedIn) {
        if (player == null) {
            return StatusCode.NotFound;
        }
        Player existing = findPlayer(player.getLogin());
        if (existing == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (!existing.checkPassword(player.getPassword())) {
            return StatusCode.AuthenticationFailed;
        }
        existing.setLoggedIn(loggedIn);
        return StatusCode.OK;
    }

    @Override
    public StatusCode editPlayer(Player player) {
        if (player == null) {
            return StatusCode.NotFound;
        }
        Player existing = findPlayer(player.getLogin());
        if (existing != null) {
            em.merge(player);
            return StatusCode.OK;
        }
        return StatusCode.NotFound;
    }

    @Override
    public StatusCode deletePlayer(Player player) {
        if (player == null) {
            return StatusCode.NotFound;
        }
        Player existing = findPlayer(player.getLogin());
        if (existing != null) {
            em.remove(em.merge(existing));
            return StatusCode.OK;
        }
        return StatusCode.NotFound;
    }
    
    @Override
    public Player findPlayer(String id) {
        if (id == null) {
            return null;
        }
        return em.find(Player.class, id);
    }

    @Override
    public List<Player> findAllPlayers() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Player.class));
        return em.createQuery(cq).getResultList();
    }

}
