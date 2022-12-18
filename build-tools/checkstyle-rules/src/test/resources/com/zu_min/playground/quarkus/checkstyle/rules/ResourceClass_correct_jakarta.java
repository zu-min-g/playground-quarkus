package com.zu_min.playground.quarkus.checkstyle.rules;

import jakarta.ws.rs.Path;

@ApplicationScoped
@Path("rootPath")
public class TestResource {
    @Path("path")
    public void test() {

    }
}
