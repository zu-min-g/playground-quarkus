package com.zu_min.playground.quarkus.extension.runtime;

import javax.ws.rs.container.ContainerRequestContext;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

public class ReactiveRequestFilter {
    private static final Logger log = Logger.getLogger(ReactiveRequestFilter.class);
    
    @ServerRequestFilter
    public void getFilter(ContainerRequestContext ctx) {
        log.info(ctx.getUriInfo().getRequestUri().toString());
    }
}
