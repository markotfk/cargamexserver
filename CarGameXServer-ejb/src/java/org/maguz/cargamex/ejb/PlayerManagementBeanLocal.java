/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.maguz.cargamex.ejb;

import javax.ejb.Local;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Local
public interface PlayerManagementBeanLocal {

    String addPlayer(String email, String login, String password);
    
}
