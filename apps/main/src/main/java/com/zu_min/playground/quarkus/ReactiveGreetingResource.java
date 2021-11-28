package com.zu_min.playground.quarkus;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.zu_min.playground.quarkus.extension.runtime.MyService;

/**
 * サンプル REST API。
 */
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