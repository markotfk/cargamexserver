/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.maguz.cargamex.ejb;

import java.sql.Date;
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
        
        if (em.find(Player.class, player.getLogin()) != null) {
            logger.warning(String.format("Rejecting duplicate player entry: '{0}'", player.getLogin()));
            return StatusCode.DuplicateEntry;
        }
        try {
            player.setRegistered(new Date(System.currentTimeMillis()));
            player.setLoggedIn(true);
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
        Player existing = em.find(Player.class, player.getLogin());
        if (existing == null) {
            return StatusCode.AuthenticationFailed;
        }
        if (!existing.getPassword().equals(player.getPassword())) {
            return StatusCode.AuthenticationFailed;
        }
        existing.setLoggedIn(loggedIn);
        return StatusCode.OK;
    }

}
