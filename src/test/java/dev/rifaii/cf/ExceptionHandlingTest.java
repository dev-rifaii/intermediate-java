package dev.rifaii.cf;

import dev.rifaii.TestBase;
import dev.rifaii.resources.Person;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static dev.rifaii.util.RandomUtility.doSomeHeavyWork;
import static dev.rifaii.util.RandomUtility.log;

/*
 * How does completable future handle exceptions?
 */
class ExceptionHandlingTest extends TestBase {

    /*
     * Exceptions in separate thread do not affect the main thread unless .get() is called
     */
    @Test
    void get() throws ExecutionException, InterruptedException {
        CompletableFuture<Person> future = CompletableFuture.supplyAsync(() -> personDao.throwingGetMock(200));

        doSomeHeavyWork(2000);

        log(future.get().toString());
    }

    /*
     * How to handle an exception?
     */
    @Test
    void handle() throws ExecutionException, InterruptedException {
        CompletableFuture<Person> future = CompletableFuture.supplyAsync(() -> personDao.throwingGetMock(200))
            .handle((res, ex) -> {
                log("RES: %s".formatted(res));
                log("EX: %s".formatted(ex));
                return new Person(1L, "BEEP");
            });

        doSomeHeavyWork(2000);

        log(future.get().toString());
    }

    /*
     * Handle is always called, even if an exception is not thrown
     */
    @Test
    void handle2() throws ExecutionException, InterruptedException {
        CompletableFuture<Person> future = CompletableFuture.supplyAsync(() -> personDao.getRandomPerson(200))
            .handle((res, ex) -> {
                log("RES: %s".formatted(res));
                log("EX: %s".formatted(ex));
                if (ex != null)
                    return new Person(1L, "BEEP");

                return res;
            });

        doSomeHeavyWork(2000);

        log(future.get().toString());
    }

    /*
     * To provide a callback for ONLY when exception is thrown, use .exceptionally():
     */
    @Test
    void exceptionally() throws ExecutionException, InterruptedException {
        CompletableFuture<Person> future = CompletableFuture.supplyAsync(() -> personDao.throwingGetMock(200))
            .exceptionally(ex -> {
                log("EX: %s".formatted(ex));
                return new Person(1L, "POOP");
            });

        doSomeHeavyWork(2000);

        log(future.get().toString());
    }

    @Test
    void exceptionallyWhenNoExceptionIsThrown() throws ExecutionException, InterruptedException {
        CompletableFuture<Person> future = CompletableFuture.supplyAsync(() -> personDao.getRandomPerson(200))
            .exceptionally(ex -> {
                log("EX: %s".formatted(ex));
                return new Person(1L, "POOP");
            });

        doSomeHeavyWork(2000);

        log(future.get().toString());
    }

    /*
     * Handle is used to actually handle an exception and return
     * another result if an exception occurs.
     * If we want to simply run some code that executes regardless of completion result
     * then whenComplete is the best option.
     */
    @Test
    void whenComplete() {
        CompletableFuture.supplyAsync(() -> personDao.throwingGetMock(200)) //Switch to non throwing for demo
            .exceptionally(ex -> {
                log("EX: %s".formatted(ex));
                return new Person(1L, "POOP");
            })
            .whenComplete((res, ex) -> log("RES: %s. EX: %s.".formatted(res, ex)));

        doSomeHeavyWork(2000);
    }
}
