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
    
    protected static final Logger logger = Logger.getLogger(ServiceRest.class.getSimpleName());
    
    protected ServiceRest() {
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
        if (status == StatusCode.Forbidden) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
