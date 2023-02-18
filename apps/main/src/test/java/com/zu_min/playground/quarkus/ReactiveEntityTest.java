package com.zu_min.playground.quarkus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import javax.inject.Inject;

import io.quarkus.test.TestReactiveTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;

import com.zu_min.playground.quarkus.extension.runtime.Fruit;

import org.hibernate.reactive.mutiny.Mutiny.Session;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ReactiveEntityTest {

    @Inject
    Session session;

    /**
     * 通常の書き方。テスト完了時にロールバックする。
     */
    @Test
    @TestReactiveTransaction
    void testInsertWithTestReactiveTransactionAndUniAsserter(UniAsserter asserter) {
        var apple = new Fruit();
        apple.setName("りんご");

        asserter.assertThat(() -> {

            return apple.<Fruit>persist()

                    // DB へ反映
                    .call(e -> session.flush())

                    // 1 次キャッシュクリア
                    .invoke(e -> session.clear())

                    // 登録した情報を取得 （キャッシュを消したので DB から取得）
                    .chain(e -> Fruit.<Fruit>findById(e.getId()));

        }, actual -> {

            // 結果の検証
            assertEquals("りんご", actual.getName());

            // DB から再取得して新しいオブジェクト ID になっていることを確認
            assertNotSame(System.identityHashCode(apple), System.identityHashCode(actual));
        });
    }

    /**
     * ロールバックしない場合は {@code @RunOnVertxContext} を使う。
     */
    @Test
    @RunOnVertxContext
    void testInsertAndCommit(UniAsserter asserter) {
        var apple = new Fruit();
        apple.setName("みかん");

        asserter.assertThat(() -> {

            return apple.<Fruit>persist()

                    // DB へ反映
                    .call(e -> session.flush())

                    // 1 次キャッシュクリア
                    .invoke(e -> session.clear())

                    // 登録した情報を取得 （キャッシュを消したので DB から取得）
                    .chain(e -> Fruit.<Fruit>findById(e.getId()));

        }, actual -> {

            // 結果の検証
            assertEquals("みかん", actual.getName());

            // DB から再取得して新しいオブジェクト ID になっていることを確認
            assertNotSame(System.identityHashCode(apple), System.identityHashCode(actual));
        });
    }
}
