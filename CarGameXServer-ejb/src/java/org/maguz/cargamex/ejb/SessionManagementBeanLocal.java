/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.maguz.cargamex.ejb;

import javax.ejb.Local;
import org.maguz.cargamex.entities.Player;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Local
public interface SessionManagementBeanLocal {
    
    public StatusCode keepAlive(Player player);
}
