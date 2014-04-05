package org.maguz.cargamex.service;

import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.maguz.cargamex.ejb.TeamManagementBeanLocal;
import org.maguz.cargamex.entities.Team;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Path("v1/teams")
public class TeamServiceRest extends ServiceRest {
    
    @EJB 
    private TeamManagementBeanLocal tm;
    
    public TeamServiceRest() {
    }

    @POST
    @Path("add/{player_id}/{session_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Team create(Team team, @PathParam("player_id") Long playerId,
            @PathParam("session_id") String sessionId) {
        handleStatusCode(tm.add(team, playerId, sessionId));
        return team;
    }

    @PUT
    @Path("edit/{player_id}/{session_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("player_id") Long playerId, 
            @PathParam("session_id") String sessionId, Team entity) {
        handleStatusCode(tm.edit(entity, playerId, sessionId));
    }

    @POST
    @Path("remove/{player_id}/{session_id}/{team_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void remove(@PathParam("player_id") Long playerId,
            @PathParam("session_id") String sessionId,
            @PathParam("team_id") Long teamId) {
        handleStatusCode(tm.remove(teamId, playerId, sessionId));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Team find(@PathParam("id") Long id) {
        return tm.find(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Team> findAll() {
        return tm.findAll();
    }
    
    @POST
    @Path("addPlayer/{player_id}/{session_id}/{new_player_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Team addPlayer(Team team, @PathParam("player_id") Long playerId,
            @PathParam("session_id") String sessionId,
            @PathParam("new_player_id") Long id) {
        handleStatusCode(tm.addPlayer(team, playerId, sessionId, id));
        return team;
    }
    
    @POST
    @Path("removePlayer/{player_id}/{session_id}/{remove_player_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Team removePlayer(Team team, @PathParam("player_id") Long playerId,
            @PathParam("session_id") String sessionId,
            @PathParam("remove_player_id") Long id) {
        handleStatusCode(tm.removePlayer(team, playerId, sessionId, id));
        return team;
    }
    
    @POST
    @Path("addAdmin/{player_id}/{session_id}/{admin_player_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Team addAdmin(Team team, @PathParam("player_id") Long playerId,
            @PathParam("session_id") String sessionId,
            @PathParam("admin_player_id") Long id) {
        handleStatusCode(tm.addAdmin(team, playerId, sessionId, id));
        return team;
    }
    @POST
    @Path("removeAdmin/{player_id}/{session_id}/{admin_player_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Team removeAdmin(Team team, @PathParam("player_id") Long playerId,
            @PathParam("session_id") String sessionId,
            @PathParam("admin_player_id") Long id) {
        handleStatusCode(tm.removeAdmin(team, playerId, sessionId, id));
        return team;
    }
    
    @POST
    @Path("setOwner/{player_id}/{session_id}/{owner_player_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Team setOwner(Team team, @PathParam("player_id") Long playerId,
            @PathParam("session_id") String sessionId,
            @PathParam("owner_player_id") Long id) {
        handleStatusCode(tm.setOwner(team, playerId, sessionId, id));
        return team;
    }
}
