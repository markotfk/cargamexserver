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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.maguz.cargamex.ejb.TrackManagementBeanLocal;
import org.maguz.cargamex.entities.Track;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Path("v1/tracks")
public class TrackServiceRest extends ServiceRest {
    
    @EJB 
    private TrackManagementBeanLocal tm;
    
    public TrackServiceRest() {
    }

    @POST
    @Path("{player_id}/{session_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Track create(@PathParam("player_id") Long playerId, 
                    @PathParam("session_id") String sessionId,
                    Track entity) {
        handleStatusCode(tm.add(playerId, sessionId, entity));
        return entity;
    }

    @PUT
    @Path("{player_id}/{session_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("player_id") Long playerId, 
            @PathParam("session_id") String sessionId, 
            Track entity) {
        handleStatusCode(tm.edit(playerId, sessionId, entity));
    }

    @DELETE
    @Path("{player_id}/{session_id}/{track_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void remove(@PathParam("player_id") Long playerId, 
            @PathParam("session_id") String sessionId,
            @PathParam("track_id") Long trackId) {
        handleStatusCode(tm.remove(playerId, sessionId, trackId));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Track find(@PathParam("id") Long id) {
        Track track = tm.find(id);
        if (track != null) {
            return track;
        } 
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Track> findAll() {
        List<Track> tracks = tm.findAll();
        if (tracks != null) {
            return tracks;
        }
        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
}
