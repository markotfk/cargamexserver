/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.maguz.cargamex.ejb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
public abstract class ManagementBean {
    
    protected Logger logger;
    
    protected ManagementBean() {
        logger = Logger.getLogger(this.getClass().toString());
    }
    @PersistenceContext(unitName = "CarGameXServer-ejbPU")
    protected EntityManager em;
    
    
    protected StatusCode merge(Object entity) {
        if (entity == null) {
            return StatusCode.NotFound;
        }
        try {
            em.merge(entity);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error merge: {0}", e.getMessage());
            return StatusCode.AuthenticationFailed;
        }
        return StatusCode.OK;
    }
    
    protected StatusCode remove(Object entity) {
        if (entity == null) {
            return StatusCode.NotFound;
        }
        try {
            em.remove(em.merge(entity));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error remove: {0}", e.getMessage());
            return StatusCode.AuthenticationFailed;
        }
        return StatusCode.OK;
    }
}
