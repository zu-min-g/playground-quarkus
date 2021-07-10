package com.zu_min.playground.quarkus.extension.classic.runtime;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

@Provider
public class ClassicRequestFilter  implements ContainerRequestFilter {
    private static final Logger log = Logger.getLogger(ClassicRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        log.info(requestContext.getUriInfo().getRequestUri().toString());
    }
    
}
