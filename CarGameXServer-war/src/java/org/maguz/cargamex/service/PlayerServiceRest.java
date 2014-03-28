/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.maguz.cargamex.service;

import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.maguz.cargamex.ejb.PlayerManagementBeanLocal;
import org.maguz.cargamex.ejb.StatusCode;
import org.maguz.cargamex.entities.Player;

/**
 *
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
    public void loginPlayer(Player player) {
        handleStatusCode(pm.login(player));
    }
    
    @POST
    @Path("logout")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void logoutPlayer(Player entity) {
        handleStatusCode(pm.logout(entity));
    }
    
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Player entity) {
        handleStatusCode(pm.edit(entity));
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        handleStatusCode(pm.remove(find(id)));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Player find(@PathParam("id") String id) {
        return pm.find(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Player> findAll() {
        return pm.findAll();
    }
}
