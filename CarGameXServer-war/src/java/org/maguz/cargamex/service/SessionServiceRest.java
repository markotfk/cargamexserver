package org.maguz.cargamex.service;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.maguz.cargamex.ejb.SessionManagementBeanLocal;
import org.maguz.cargamex.entities.Player;

/**
 * Player Session REST API.
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@Path("v1/session")
public class SessionServiceRest extends ServiceRest {
    
    @EJB 
    private SessionManagementBeanLocal sm;
    
    public SessionServiceRest() {
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void keepAlive(Player player) {
        handleStatusCode(sm.keepAlive(player));
    }

}
