package org.maguz.cargamex.service;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.maguz.cargamex.ejb.TrackRecordManagementBeanLocal;
import org.maguz.cargamex.entities.TrackRecord;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Path("v1/trackrecords")
public class TrackRecordServiceRest extends ServiceRest {
    
    @EJB 
    private TrackRecordManagementBeanLocal tm;
    
    public TrackRecordServiceRest() {
    }

    @POST
    @Path("{player_id}/{session_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TrackRecord create(TrackRecord entity, @PathParam("player_id") Long playerId,
            @PathParam("session_id") String sessionId) {
        handleStatusCode(tm.add(entity, playerId, sessionId));
        return entity;
    }

    @PUT
    @Path("{player_id}/{session_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(TrackRecord entity, @PathParam("player_id") Long playerId,
            @PathParam("session_id") String sessionId) {
        handleStatusCode(tm.edit(entity, playerId, sessionId));
    }

    @POST
    @Path("remove/{track_record_id}/{player_id}/{session_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void remove(@PathParam("track_record_id") Long trackRecordId,
            @PathParam("player_id") Long playerId, 
            @PathParam("session_id") String sessionId) {
        handleStatusCode(tm.remove(trackRecordId, playerId, sessionId));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TrackRecord find(@PathParam("id") Long id) {
        TrackRecord existing = tm.find(id);
        if (existing != null) {
            return existing;
        } 
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TrackRecord> findAll() {
        List<TrackRecord> all = tm.findAll();
        if (all != null) {
            return all;
        }
        return new ArrayList<>();
    }
    
    @GET
    @Path("findByPlayer/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TrackRecord> findByPlayer(@PathParam("id") Long id) {
        List<TrackRecord> records = tm.findAllByPlayerId(id);
        if (records != null) {
            return records;
        } 
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
    
    @GET
    @Path("findByTeam/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TrackRecord> findByTeam(@PathParam("id") Long id) {
        List<TrackRecord> records = tm.findAllByTrackId(id);
        if (records != null) {
            return records;
        } 
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
}
