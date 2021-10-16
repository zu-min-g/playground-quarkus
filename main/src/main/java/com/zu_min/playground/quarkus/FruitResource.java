package com.zu_min.playground.quarkus;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.zu_min.playground.quarkus.extension.runtime.Fruit;

import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.resteasy.reactive.RestQuery;

import io.smallrye.mutiny.Uni;

/**
 * Panache の例。
 */
@Path("fruit")
public class FruitResource {
    @Inject
    Mutiny.Session session;

    @GET
    public Uni<Fruit> get(@RestQuery Long id) {
        return Fruit.findById(id);
    }
    
    @GET
    @Path("from-em")
    public Uni<Fruit> getByEntityManager(@RestQuery Long id) {
        return session.createQuery("from Fruit where id = :id", Fruit.class).setParameter("id", id).getSingleResultOrNull();
    }
}
