/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.maguz.cargamex.service;

import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
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
public class PlayerFacadeREST extends AbstractFacade<Player> {
    @PersistenceContext(unitName = "CarGameXServer-warPU")
    private EntityManager em;

    
    @EJB 
    private PlayerManagementBeanLocal pm;
    
    public PlayerFacadeREST() {
        super(Player.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public void create(Player entity) {
        handleStatusCode(pm.addPlayer(entity));
    }

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void loginPlayer(Player entity) {
        handleStatusCode(pm.loginPlayer(entity));
    }
    
    @POST
    @Path("logout")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void logoutPlayer(Player entity) {
        handleStatusCode(pm.logoutPlayer(entity));
    }
    
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Player entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Player find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Player> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Player> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    private void handleStatusCode(StatusCode status) {
        if (status == StatusCode.DuplicateEntry) {
            //  Player with similar login exists, return CONFLICT error code
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
        if (status == StatusCode.Error) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (status == StatusCode.NotFound) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        if (status == StatusCode.AuthenticationFailed) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
}
