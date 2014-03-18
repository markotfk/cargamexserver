/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.maguz.cargamex.webservice;

import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import org.maguz.cargamex.ejb.PlayerManagementBean;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@WebService(serviceName = "UserWebService")
@Stateless()
public class UserWebService {

    @EJB
    PlayerManagementBean playerManager;
    
    /**
     * Add/Register new player
     */
    @WebMethod(operationName = "addplayer")
    public String addplayer(@WebParam(name = "email") String email,
    @WebParam(name = "login") String login, @WebParam(name="password") String passwd) {
        return playerManager.addPlayer(email, login, passwd);
    }
    
    
}
