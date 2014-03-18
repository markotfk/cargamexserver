/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.maguz.cargamex.ejb;

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

    @PersistenceContext
    EntityManager em;
    
    @Override
    public String addPlayer(String email, String login, String password) {
        Player p = em.find(Player.class, login);
        if (p != null) {
            return "failed: duplicate login";
        }
        else {
            Player newPlayer = new Player();
            newPlayer.setEmail(email);
            newPlayer.setLogin(login);
            newPlayer.setPassword(password);
            em.persist(newPlayer);
            return "ok";
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}
