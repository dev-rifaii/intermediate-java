package dev.rifaii.cf;

import dev.rifaii.resources.Person;
import dev.rifaii.resources.PersonDao;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static dev.rifaii.util.RandomUtility.log;
import static dev.rifaii.util.RandomUtility.sleep;

/*
 * This class shows the ways of constructing a completable future
 */
class BasicsTest {

    final PersonDao personDao = new PersonDao();

    @Test
    void construction() {
        var future = new CompletableFuture<Person>();
        log("BEFORE STARTING COMPLETION");
        future.completeAsync(() -> personDao.getRandomPerson(2000));
        log("AFTER STARTING COMPLETION");

        log("isDone: " + future.isDone());

        sleep(5000);

        log("isDone: " + future.isDone());
    }

    @Test
    void staticConstruction() {
        log("BEFORE STARTING COMPLETION");
        var future = CompletableFuture.supplyAsync(() -> personDao.getRandomPerson(2000));
        log("AFTER STARTING COMPLETION");

        log("isDone: " + future.isDone());

        sleep(5000);

        log("isDone: " + future.isDone());
    }


}
