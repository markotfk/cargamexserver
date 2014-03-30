package org.maguz.cargamex.service;

import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
    public void create(Team entity, @PathParam("player_id") String playerId,
            @PathParam("session_id") String sessionId) {
        handleStatusCode(tm.add(entity, parseId(playerId), sessionId));
    }

    @PUT
    @Path("edit/{player_id}/{session_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("player_id") String playerId, 
            @PathParam("session_id") String sessionId, Team entity) {
        handleStatusCode(tm.edit(entity, parseId(playerId), sessionId));
    }

    @DELETE
    @Path("remove/{player_id}/{session_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void remove(@PathParam("player_id") String playerId,
            @PathParam("session_id") String sessionId,
            Team entity) {
        handleStatusCode(tm.remove(entity, parseId(playerId), sessionId));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Team find(@PathParam("id") String id) {
        return tm.find(parseId(id));
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Team> findAll() {
        return tm.findAll();
    }
}
