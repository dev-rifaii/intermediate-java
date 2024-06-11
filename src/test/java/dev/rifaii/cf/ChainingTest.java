package dev.rifaii.cf;

import dev.rifaii.TestBase;
import dev.rifaii.resources.CarDao;
import dev.rifaii.resources.Person;
import dev.rifaii.resources.PersonDao;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static dev.rifaii.util.RandomUtility.*;

/**
 * This class shows ways of chaining completable future
 */
class ChainingTest extends TestBase {

    private final PersonDao personDao = new PersonDao();
    private final CarDao carDao = new CarDao();

    /*
     * How to use result of CF for another operation using chaining?
     * Does chaining methods block main thread?
     */
    @Test
    void simpleChain() {
        CompletableFuture.supplyAsync(() -> personDao.getRandomPerson(2000))
            .thenApply(person -> carDao.getCar(person.id()))
            .thenAccept(car -> log("Yay car id %d returned".formatted(car.id())));

        doSomeHeavyWork(3000);
    }

    @Test
    void syncVersionsOfChainingMethods() {
        log("CURRENT THREAD: %s".formatted(Thread.currentThread().getName()));
        CompletableFuture.completedFuture(new Person(51L, "THREAD TEST"))
            .thenApply(person -> {
                log("Getting CAR on thread: %s".formatted(Thread.currentThread().getName()));
                return carDao.getCar(person.id(), 5000);
            })
            .thenAccept(car -> {
                log("Accepting CAR on thread: %s".formatted(Thread.currentThread().getName()));
                log("Yay car id %d returned".formatted(car.id()));
            });

        doSomeHeavyWork(3000);
    }

    @Test
    void asyncVersionsOfChainingMethods() {
        log("CURRENT THREAD: %s".formatted(Thread.currentThread().getName()));
        CompletableFuture.completedFuture(new Person(51L, "THREAD TEST"))
            .thenApplyAsync(person -> {
                log("Getting CAR on thread: %s".formatted(Thread.currentThread().getName()));
                return carDao.getCar(person.id(), 5000);
            })
            .thenAcceptAsync(car -> {
                log("Accepting CAR on thread: %s".formatted(Thread.currentThread().getName()));
                log("Yay car id %d returned".formatted(car.id()));
            });

        doSomeHeavyWork(3000);
    }

    /*
     * TLDR, thenApply   = Optional.map()
     *       thenCompose = Optional.flatMap()
     */
    @Test
    void thenApplyVsThenCompose() {
        CompletableFuture.supplyAsync(() -> 1)
            .thenApply(v -> {
                log("Starting THEN-APPLY sleep");
                sleep(5000);
                log("Finished THEN-APPLY sleep");
                return v + 1;
            })
            .thenRun(() -> log("From After running then APPLY"));

        CompletableFuture.supplyAsync(() -> 1)
            .thenCompose(val -> CompletableFuture.supplyAsync(() -> {
                log("Starting THEN-COMPOSE sleep");
                sleep(5000);
                log("Finished THEN-COMPOSE sleep");
                return val + 1;
            }))
            .thenRun(() -> log("From After running then COMPOSE"));

        CompletableFuture.supplyAsync(() -> 1)
            .thenApply(v -> {
                log("Starting THEN-APPLY2 sleep");
                sleep(5000);
                log("Finished THEN-APPLY2 sleep");
                return v + 1;
            })
            .thenRun(() -> log("From After running then APPLY2"));
    }
}