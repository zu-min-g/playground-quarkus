package com.zu_min.playground.quarkus;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.zu_min.playground.quarkus.extension.runtime.Fruit;

import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.resteasy.reactive.RestQuery;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
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

    @POST
    @ReactiveTransactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Fruit> add(@Valid Fruit fruit) {
        fruit.setId(null);
        return fruit.<Fruit>persist();
    }

    @PUT
    @ReactiveTransactional
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Fruit> update(@PathParam("id") Long id, @Valid Fruit fruit) {
        fruit.setId(id);
        return session.merge(fruit).call(() -> session.flush());
    }

    @DELETE
    @ReactiveTransactional
    @Path("{id}")
    public Uni<Void> delete(@PathParam("id") Long id) {
        return Fruit.findById(id)
            .onItem().ifNull().failWith(() -> new WebApplicationException(404))
            .chain(f -> f.delete());
    }
    
    @GET
    @Path("from-em")
    public Uni<Fruit> getByEntityManager(@RestQuery Long id) {
        return session.createQuery("from Fruit where id = :id", Fruit.class)
            .setParameter("id", id).getSingleResultOrNull();
    }
}
