package com.zu_min.playground.quarkus.extension.classic.runtime;

import java.io.IOException;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import org.jboss.logging.MDC;

/**
 * レスポンスフィルター。
 */
@Provider
public class ClassicResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext)
            throws IOException {

        MDC.remove("req.id");
    }

}
