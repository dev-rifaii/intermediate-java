package dev.rifaii.cf;

import dev.rifaii.TestBase;
import dev.rifaii.resources.Person;
import dev.rifaii.resources.PersonDao;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static dev.rifaii.util.RandomUtility.*;

/**
 * This class shows the basics of using {@link CompletableFuture}
 */
class BasicsTest extends TestBase {

    final PersonDao personDao = new PersonDao();

    @Test
    void withoutCompletableFuture() {
        personDao.getRandomPerson(4000);
        sleep(5000);
    }

    @Test
    void construction() {
        var future = new CompletableFuture<Person>();
        future.completeAsync(() -> personDao.getRandomPerson(4000));
        sleep(5000, true);
    }

    @Test
    void staticConstruction() {
        CompletableFuture.supplyAsync(() -> personDao.getRandomPerson(4000));
        sleep(5000, true);
    }

    @Test
    void runningInBackground() {
        CompletableFuture.runAsync(() -> doSomeHeavyWork(4000));
    }

    @Test
    void gettingValue() throws ExecutionException, InterruptedException {
        var future = CompletableFuture.supplyAsync(() -> personDao.getRandomPerson(4000));
        future.get();
    }

    @Test
    void gettingDefaultValueIfNotDone() {
        var future = CompletableFuture.supplyAsync(() -> personDao.getRandomPerson(4000));
        Person person = future.getNow(new Person(51L, "John Doe"));
        log(person.name());
    }

    @Test
    void basicUsage() throws ExecutionException, InterruptedException {
        var future = CompletableFuture.supplyAsync(() -> personDao.getRandomPerson(4000));

        //Some other blocking operations
        doSomeHeavyWork(4000);
        doSomeHeavyWork(2000);

        log("Is future done: " + future.isDone());
        Person person = future.get();
        log(person.name());
    }

}
