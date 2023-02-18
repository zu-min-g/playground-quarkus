package com.zu_min.playground.quarkus;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;

import com.zu_min.playground.quarkus.dto.FruitDto;
import com.zu_min.playground.quarkus.extension.runtime.Fruit;
import com.zu_min.playground.quarkus.mapper.FruitMapper;

import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestQuery;

/**
 * Panache の例。
 */
@Path("fruit")
public class FruitResource {

    @Inject
    FruitMapper mapper;

    /**
     * 指定した id に紐づく情報を返却します。
     */
    @GET
    @WithTransaction
    public Uni<FruitDto> get(@RestQuery Long id) {
        return Fruit.<Fruit>findById(id)
                .onItem().ifNull().failWith(() -> new WebApplicationException(404))
                .map(e -> mapper.createFrom(e));
    }

    /**
     * 追加します。
     */
    @POST
    @WithTransaction
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
    @WithTransaction
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<FruitDto> update(@PathParam("id") Long id, @Valid FruitDto dto) {
        return Fruit.<Fruit>findById(id)
                .onItem().ifNull().failWith(() -> new WebApplicationException(404))
                .invoke(e -> mapper.copyTo(e, dto))
                .call(Panache::flush)
                .map(e -> mapper.createFrom(e));
    }

    /**
     * 削除します。
     */
    @DELETE
    @WithTransaction
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
        return Panache.getSession()
                .map(s -> s.createQuery("from Fruit where id = :id", Fruit.class))
                .invoke(query -> query.setParameter("id", id))
                .chain(query -> query.getSingleResultOrNull())
                .onItem().ifNull().failWith(() -> new WebApplicationException(404))
                .map(e -> mapper.createFrom(e));
    }
}
