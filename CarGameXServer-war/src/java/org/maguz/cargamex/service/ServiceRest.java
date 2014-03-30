/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.maguz.cargamex.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.maguz.cargamex.ejb.StatusCode;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
public abstract class ServiceRest {
    
    protected Logger logger;
    
    protected ServiceRest() {
        logger = Logger.getLogger(getClass().getName());
    }
    protected void handleStatusCode(StatusCode status) {
        logger.log(Level.INFO, "handleStatusCode {0}", status);
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
    
    protected Long parseId(String id) {
        try {
            Long playerId = Long.parseLong(id);
            return playerId;
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Error parsing id: {0}", e.getMessage());
        }
        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
}
