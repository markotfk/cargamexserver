/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.maguz.cargamex.webservice;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.maguz.cargamex.entities.Player;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@WebService(serviceName = "UserWebService")
@Stateless()
public class UserWebService {

    @PersistenceContext
    EntityManager em;
    /**
     * Add user
     */
    @WebMethod(operationName = "addplayer")
    public String addplayer(@WebParam(name = "email") String email,
    @WebParam(name = "login") String login, @WebParam(name="password") String passwd) {
        Player p = em.find(Player.class, login);
        if (p != null) {
            return "fail, duplicate login found";
        }
        else {
            Player newPlayer = new Player();
            newPlayer.setEmail(email);
            newPlayer.setLogin(login);
            newPlayer.setPassword(passwd);
            em.persist(newPlayer);
            return "ok";
        }
        
    }
}
