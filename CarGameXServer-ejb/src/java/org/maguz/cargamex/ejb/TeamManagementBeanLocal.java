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
    
    StatusCode add(Team team, Long playerId, String sessionId);
    
    StatusCode remove(Long teamId, Long playerId, String sessionId);
    
    StatusCode edit(Team team, Long playerId, String sessionId);
    
    Team find(Long id);
    
    List<Team> findAll();
    
    StatusCode addPlayer(Team team, Long playerId, String sessionId, Long newPlayerId);
    StatusCode removePlayer(Team team, Long playerId, String sessionId, Long removePlayerId);
    StatusCode addAdmin(Team team, Long playerId, String sessionId, Long adminPlayerId);
    StatusCode removeAdmin(Team team, Long playerId, String sessionId, Long adminPlayerId);
    StatusCode setOwner(Team team, Long playerId, String sessionId, Long ownerPlayerId);
}
