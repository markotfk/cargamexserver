package org.maguz.cargamex.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 * Java REST API application class implementation.
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
@javax.ws.rs.ApplicationPath("rest")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        // following code can be used to customize Jersey 1.x JSON provider:
        try {
            Class jacksonProvider = Class.forName("org.codehaus.jackson.jaxrs.JacksonJsonProvider");
            resources.add(jacksonProvider);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(org.maguz.cargamex.service.PlayerServiceRest.class);
        resources.add(org.maguz.cargamex.service.SessionServiceRest.class);
        resources.add(org.maguz.cargamex.service.TeamServiceRest.class);
        resources.add(org.maguz.cargamex.service.TrackRecordServiceRest.class);
        resources.add(org.maguz.cargamex.service.TrackServiceRest.class);
    }
    
}
