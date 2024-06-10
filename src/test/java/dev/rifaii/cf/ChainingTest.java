package dev.rifaii.cf;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

class ChainingTest {

    @Test
    void thenApplyVsThenCompose() {


//

        CompletableFuture.supplyAsync(() -> 1)
            .thenApply(v -> {
                log("Starting THEN-APPLY sleep");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log("Finished THEN-APPLY sleep");
                return v + 1;
            })
            .thenRun(() -> log("From After running then APPLY"));

        CompletableFuture.supplyAsync(() -> 1)
            .thenCompose(val -> CompletableFuture.supplyAsync(() -> {
                log("Starting THEN-COMPOSE sleep");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log("Finished THEN-COMPOSE sleep");
                return val + 1;
            }))
            .thenRun(() -> log("From After running then COMPOSE"));

        CompletableFuture.supplyAsync(() -> 1)
            .thenApply(v -> {
                log("Starting THEN-APPLY2 sleep");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log("Finished THEN-APPLY2 sleep");
                return v + 1;
            })
            .thenRun(() -> log("From After running then APPLY2"));
    }

    private static void log(String s) {
        System.out.println(LocalTime.now() + " " + s);
    }
}