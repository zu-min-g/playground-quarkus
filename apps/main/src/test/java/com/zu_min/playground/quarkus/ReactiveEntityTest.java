package com.zu_min.playground.quarkus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import javax.inject.Inject;

import io.quarkus.test.TestReactiveTransaction;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.vertx.UniAsserter;

import com.zu_min.playground.quarkus.extension.runtime.Fruit;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.hibernate.reactive.mutiny.Mutiny.Session;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ReactiveEntityTest {

    @Inject
    Session session;

    /**
     * 通常の書き方（ロールバックしない）。
     */
    @Test
    void testInsertAndCommit() {
        var apple = new Fruit();
        apple.setName("りんご");

        var subscriber = apple.<Fruit>persist()

                // DB へ反映
                .call(e -> session.flush())

                // 1 次キャッシュクリア
                .invoke(e -> session.clear())

                // 登録した情報を取得 （キャッシュを消したので DB から取得）
                .chain(e -> Fruit.<Fruit>findById(e.getId()))

                // 購読者を指定して処理を実施
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        var actual = subscriber
                // ブロックして結果を待つ
                .awaitItem()
                // 完了すること（例外が発生しないこと）
                .assertCompleted()
                // 結果を取得
                .getItem();

        // 結果の検証
        assertEquals("りんご", actual.getName());

        // DB から再取得して新しいオブジェクト ID になっていることを確認
        assertNotSame(System.identityHashCode(apple), System.identityHashCode(actual));
    }

    /**
     * TestTransaction ではロールバックされない。
     */
    @Test
    @TestTransaction
    void testInsertWithTestTransaction() {
        var apple = new Fruit();
        apple.setName("りんご");

        var subscriber = apple.<Fruit>persist()

                // DB へ反映
                .call(e -> session.flush())

                // 1 次キャッシュクリア
                .invoke(e -> session.clear())

                // 登録した情報を取得 （キャッシュを消したので DB から取得）
                .chain(e -> Fruit.<Fruit>findById(e.getId()))

                // 購読者を指定して処理を実施
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        var actual = subscriber
                // ブロックして結果を待つ
                .awaitItem()
                // 完了すること（例外が発生しないこと）
                .assertCompleted()
                // 結果を取得
                .getItem();

        // 結果の検証
        assertEquals("りんご", actual.getName());

        // DB から再取得して新しいオブジェクト ID になっていることを確認
        assertNotSame(System.identityHashCode(apple), System.identityHashCode(actual));
    }

    /**
     * TestReactiveTransaction の失敗例。例外が発生する。
     */
    @Test
    @Disabled("失敗例")
    @TestReactiveTransaction
    void testFailureCaseOfInsertWithTestReactiveTransaction() {
        var apple = new Fruit();
        apple.setName("りんご");

        var subscriber = apple.<Fruit>persist()

                // DB へ反映
                .call(e -> session.flush())

                // 1 次キャッシュクリア
                .invoke(e -> session.clear())

                // 登録した情報を取得 （キャッシュを消したので DB から取得）
                .chain(e -> Fruit.<Fruit>findById(e.getId()))

                // 購読者を指定して処理を実施
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        var actual = subscriber
                // ブロックして結果を待つ
                .awaitItem()
                // 完了すること（例外が発生しないこと）
                .assertCompleted()
                // 結果を取得
                .getItem();

        // 結果の検証
        assertEquals("りんご", actual.getName());

        // DB から再取得して新しいオブジェクト ID になっていることを確認
        assertNotSame(System.identityHashCode(apple), System.identityHashCode(actual));
    }

    /**
     * TestReactiveTransaction 書き換え例。
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
}
