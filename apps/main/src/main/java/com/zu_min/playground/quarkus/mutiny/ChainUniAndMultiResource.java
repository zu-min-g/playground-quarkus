package com.zu_min.playground.quarkus.mutiny;

import java.util.LongSummaryStatistics;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

/**
 * Uni や Multi を繋ぐ。
 */
@Path("mutiny/chain")
public class ChainUniAndMultiResource {
    
    /**
     * Uni と Multi を繋ぐ例。
     */
    @GET
    @Path("uni-and-multi")
    public Uni<Void> standard() {
        return Uni.createFrom().nullItem()
            .chain(() -> {
                // 最後の要素を返す
                return Multi.createFrom().items(1, 2, 3, 5, 7)
                    .collect().last();
            })
            .chain(l -> {
                // 件数を返す
                return Multi.createFrom().items(1, 2, 3, 5, 7)
                    .collect().with(Collectors.counting());
            })
            .chain(l -> {
                // 件数を返す
                return Multi.createFrom().items(1, 2, 3, 5, 7)
                    .collect().with(Collectors.summarizingLong(a -> 1L))
                    .map(LongSummaryStatistics::getCount);
            })
            .chain(l -> {
                // 件数を返す (Collector 独自実装)
                return Multi.createFrom().items(1, 2, 3, 5, 7)
                    .collect().with(Collector.of(
                        () -> new long[1],
                        (count, item) -> {
                            count[0]++;
                        },
                        (a, b) -> a,
                        arr -> arr[0]));
            })
            .chain(l -> {
                // 件数を返す (in を使う)
                return Multi.createFrom().items(1, 2, 3, 5, 7)
                    .collect()
                    .in(() -> new long[1], (count, item) -> {
                        count[0]++;
                    })
                    .map(count -> count[0]);
            })
            .chain(l -> {
                // 総和を返す
                return Multi.createFrom().items(1, 2, 3, 5, 7)
                    .collect().with(Collectors.reducing((a, b) -> {
                        return a + b;
                    }));
            })
            .chain(l -> {
                // 総和を返す
                return Multi.createFrom().items(1, 2, 3, 5, 7)
                    .collect()
                    .with(Collectors.summingLong(a -> a));
            })
            .chain(l -> Uni.createFrom().nullItem());
    }

}
