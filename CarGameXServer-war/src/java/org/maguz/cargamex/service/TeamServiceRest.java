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
import org.maguz.cargamex.ejb.StatusCode;
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
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Team entity) {
        handleStatusCode(tm.add(entity));
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Team entity) {
        handleStatusCode(tm.edit(entity));
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        handleStatusCode(tm.remove(find(id)));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Team find(@PathParam("id") String id) {
        try {
            Long val = Long.parseLong(id);
            return tm.find(val);
        } catch (NumberFormatException e) {
            handleStatusCode(StatusCode.NotFound);
        }
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Team> findAll() {
        return tm.findAll();
    }
}
