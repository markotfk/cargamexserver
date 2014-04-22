package org.maguz.cargamex.service;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.maguz.cargamex.ejb.PlayerManagementBeanLocal;
import org.maguz.cargamex.entities.Player;

/**
 * Player REST API.
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Path("v1/players")
public class PlayerServiceRest extends ServiceRest {
    
    @EJB 
    private PlayerManagementBeanLocal pm;
    
    public PlayerServiceRest() {
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Player entity) {
        handleStatusCode(pm.add(entity));
    }

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces ({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Player loginPlayer(Player player, @Context HttpServletRequest request) {
        player.setSessionId(request.getSession(true).getId());
        handleStatusCode(pm.login(player));
        return player;
    }
    
    @POST
    @Path("logout")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void logoutPlayer(Player entity, @Context HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        handleStatusCode(pm.logout(entity));
    }
    
    @DELETE
    @Path("{player_id}/{session_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void remove(@PathParam("player_id") Long playerId, 
            @PathParam("session_id") String sessionId) {
        handleStatusCode(pm.remove(playerId, sessionId));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Player find(@PathParam("id") Long id) {
        Player player = pm.find(id);
        if (player != null) {
            return player;
        } 
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Player> findAll() {
        List<Player> players = pm.findAll();
        if (players != null) {
            return players;
        }
        return new ArrayList<>();
    }
    
    @GET
    @Path("findByLogin/{login}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Player> findByLogin(@PathParam("login")String login) {
        if (login == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return pm.findAllByLogin(login);
    }
}
