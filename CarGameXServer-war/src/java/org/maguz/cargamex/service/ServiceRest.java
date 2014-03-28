/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.maguz.cargamex.service;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.maguz.cargamex.ejb.StatusCode;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
public abstract class ServiceRest {
    
    protected void handleStatusCode(StatusCode status) {
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
