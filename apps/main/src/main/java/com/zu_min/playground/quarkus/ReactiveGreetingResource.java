package com.zu_min.playground.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.zu_min.playground.quarkus.extension.runtime.MyService;

/**
 * サンプル REST API。
 */
@ApplicationScoped
@Path("/hello")
public class ReactiveGreetingResource {

    @Inject
    MyService myService;

    @GET
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public String hello() {
        return myService.getMessage();
    }
}
