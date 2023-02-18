package com.zu_min.playground.quarkus.extension.classic.runtime;

import java.io.IOException;
import java.util.UUID;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

/**
 * リクエストフィルター。
 */
@Provider
public class ClassicRequestFilter implements ContainerRequestFilter {
    private static final Logger log = Logger.getLogger(ClassicRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        var requestId = UUID.randomUUID();
        MDC.put("req.id", requestId);

        log.info(requestContext.getUriInfo().getRequestUri().toString());
    }

}
