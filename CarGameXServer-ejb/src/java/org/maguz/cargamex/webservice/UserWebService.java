/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.maguz.cargamex.webservice;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@WebService(serviceName = "UserWebService")
@Stateless()
public class UserWebService {

    /**
     * Add user
     */
    @WebMethod(operationName = "adduser")
    public boolean adduser(@WebParam(name = "name") String name,
    @WebParam(name = "login") String login, @WebParam(name="password") String passwd) {
        return true;
    }
}
