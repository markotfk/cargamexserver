/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.maguz.cargamex.ejb;

import java.util.List;
import javax.ejb.Local;
import org.maguz.cargamex.entities.Team;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Local
public interface TeamManagementBeanLocal {
    
    StatusCode add(Team entity);
    
    StatusCode remove(Team entity);
    
    StatusCode edit(Team entity);
    
    Team find(Long id);
    
    List<Team> findAll();
}
