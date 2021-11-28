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

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;

import com.zu_min.playground.quarkus.dto.FruitDto;
import com.zu_min.playground.quarkus.extension.runtime.Fruit;
import com.zu_min.playground.quarkus.mapper.FruitMapper;

import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.resteasy.reactive.RestQuery;

/**
 * Panache の例。
 */
@Path("fruit")
public class FruitResource {
    @Inject
    Mutiny.Session session;

    @Inject
    FruitMapper mapper;

    /**
     * 指定した id に紐づく情報を返却します。
     */
    @GET
    public Uni<FruitDto> get(@RestQuery Long id) {
        return Fruit.<Fruit>findById(id)
                .onItem().ifNull().failWith(() -> new WebApplicationException(404))
                .map(e -> mapper.createFrom(e));
    }

    /**
     * 追加します。
     */
    @POST
    @ReactiveTransactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<FruitDto> add(@Valid FruitDto fruit) {
        var entity = mapper.createFrom(fruit);
        return entity.<Fruit>persist()
                .map(e -> mapper.createFrom(e));
    }

    /**
     * 更新します。
     */
    @PUT
    @ReactiveTransactional
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<FruitDto> update(@PathParam("id") Long id, @Valid FruitDto dto) {
        return Fruit.<Fruit>findById(id)
                .onItem().ifNull().failWith(() -> new WebApplicationException(404))
                .invoke(e -> mapper.copyTo(e, dto))
                .call(() -> session.flush())
                .map(e -> mapper.createFrom(e));
    }

    /**
     * 削除します。
     */
    @DELETE
    @ReactiveTransactional
    @Path("{id}")
    public Uni<Void> delete(@PathParam("id") Long id) {
        return Fruit.findById(id)
                .onItem().ifNull().failWith(() -> new WebApplicationException(404))
                .chain(f -> f.delete());
    }

    /**
     * query を使用して取得します。
     */
    @GET
    @Path("from-em")
    public Uni<FruitDto> getByEntityManager(@RestQuery Long id) {
        return session.createQuery("from Fruit where id = :id", Fruit.class)
                .setParameter("id", id).getSingleResultOrNull()
                .onItem().ifNull().failWith(() -> new WebApplicationException(404))
                .map(e -> mapper.createFrom(e));
    }
}
