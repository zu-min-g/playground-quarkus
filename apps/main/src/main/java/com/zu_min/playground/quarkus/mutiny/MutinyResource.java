package com.zu_min.playground.quarkus.mutiny;

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;

/**
 * Mutiny の例。
 */
@Path("mutiny")
@Produces(MediaType.TEXT_PLAIN)
public class MutinyResource {

    /**
     * Uni を繋ぐ例。
     */
    @GET
    @Path("uni")
    public Uni<Integer> standard() {
        Uni<Integer> uni = Uni.createFrom().item(1);

        return uni
            .onItem().invoke(i -> {})
            .onItem().call(i -> Uni.createFrom().voidItem())
            .onItem().transform(i -> i)
            .onItem().transformToUni(i -> Uni.createFrom().item(i))
            .onFailure().invoke(exception -> {});
    }

    /**
     * Uni を開始する例。
     */
    @GET
    @Path("run-uni")
    public void run() {
        Uni<Void> uni = Uni.createFrom().voidItem();

        Uni.createFrom().item("A")
            .chain(i -> uni.map(e -> i))
            .subscribe().with(i -> Log.info(i));

        Log.info("B");

        var result = Uni.createFrom().item("C")
            .chain(i -> uni.map(e -> i))
            .await().indefinitely();
        Log.info(result);

        Log.info("D");
    }

    /**
     * Uni の実行がうまくいかない例。
     */
    @GET
    @Path("invoke-and-call")
    public Uni<Integer> invokeAndCall() {
        Uni.createFrom().voidItem().await().indefinitely();
        return 最初の操作()
            .invoke(this::次の操作)
            .call(this::次の操作)
            .map(this::最後の操作);
    }

    Uni<Void> 次の操作(AtomicInteger atomicInt) {
        Log.info("次の操作");
        return Uni.createFrom().item(() -> {
            atomicInt.incrementAndGet();
            return null;
        });
    }

    Uni<AtomicInteger> 最初の操作() {
        Log.info("最初の操作");
        return Uni.createFrom().item(new AtomicInteger());
    }

    int 最後の操作(AtomicInteger atomicInt) {
        Log.info("最後の操作");
        return atomicInt.intValue();
    }

    /**
     * 例外の伝播の例。
     */
    @GET
    @Path("failure")
    public Uni<Integer> failure() {
        // A, C, D が実行される
        return Uni.createFrom().item(1)
            .onItem().failWith(i -> new RuntimeException())
            .onFailure().invoke(() -> Log.info("A"))
            .onItem().invoke(() -> Log.info("B"))
            .onFailure().invoke(() -> Log.info("C"))
            .onFailure().recoverWithItem(2)
            .onItem().invoke(() -> Log.info("D"))
            .onFailure().invoke(() -> Log.info("E"));
    }

    /**
     * 特定の処理が投げた例外をキャッチする例。
     */
    @GET
    @Path("catch-specific-process-failure")
    public Uni<Integer> catchSpecificProcessFailure() {
        // 前の処理の例外はキャッチしなくていい場合
        // B のみ実行される
        return Uni.createFrom().item(1)
            .onItem().failWith(i -> new RuntimeException())
            .chain(i -> Uni.createFrom().item(i)
                .onItem().failWith(item -> new RuntimeException())
                .onFailure().invoke(() -> Log.info("A")))
            .onFailure().invoke(() -> Log.info("B"))
            .onFailure().recoverWithItem(2);
    }

    /**
     * 例外を投げる例。
     */
    @GET
    @Path("throw-exception")
    public void throwException() {
        Uni.createFrom().item(1)
            .invoke(i -> {
                throw new RuntimeException();
            })
            .onFailure().invoke(i -> Log.info("A"))
            .subscribe().with(i -> Log.info("A-1"), i -> Log.info("A-2"));

        Uni.createFrom().item(1)
            .call(i -> {
                throw new RuntimeException();
            })
            .onFailure().invoke(i -> Log.info("B"))
            .subscribe().with(i -> Log.info("B-1"), i -> Log.info("B-2"));

        Uni.createFrom().item(1)
            .call(i -> {
                return Uni.createFrom().failure(() -> new RuntimeException());
            })
            .onFailure().invoke(i -> Log.info("C"))
            .subscribe().with(i -> Log.info("C-1"), e -> Log.info("C-2"));

        Uni.createFrom().item(1)
            .map(i -> {
                throw new RuntimeException();
            })
            .onFailure().invoke(i -> Log.info("D"))
            .subscribe().with(i -> Log.info("D-1"), i -> Log.info("D-2"));

        Uni.createFrom().item(1)
            .chain(i -> {
                throw new RuntimeException();
            })
            .onFailure().invoke(i -> Log.info("E"))
            .subscribe().with(i -> Log.info("E-1"), e -> Log.info("E-2"));

        Uni.createFrom().item(1)
            .chain(i -> {
                return Uni.createFrom().failure(() -> new RuntimeException());
            })
            .onFailure().invoke(i -> Log.info("F"))
            .subscribe().with(i -> Log.info("F-1"), e -> Log.info("F-2"));
    }
}
