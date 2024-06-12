package dev.rifaii.cf;

import dev.rifaii.TestBase;
import dev.rifaii.resources.Person;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static dev.rifaii.util.RandomUtility.doSomeHeavyWork;
import static dev.rifaii.util.RandomUtility.log;
/*
 * How does completable future handle exceptions?
 */
class ExceptionHandlingTest extends TestBase {


    /*
     * Exceptions in separate thread do not affect the main thread
     */
    @Test
    void exceptionHandling() {
        CompletableFuture.supplyAsync(() -> personDao.throwingGetMock(200))
            .thenAccept(person -> log("Person returned!"));

        doSomeHeavyWork(2000);
    }
}
