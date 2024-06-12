package dev.rifaii.cf;

import dev.rifaii.TestBase;
import dev.rifaii.resources.Car;
import dev.rifaii.resources.CarDao;
import dev.rifaii.resources.Person;
import dev.rifaii.resources.PersonDao;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static dev.rifaii.util.RandomUtility.*;

/**
 * This class shows ways of chaining completable future
 */
class ChainingTest extends TestBase {

    /*
     * How to use result of CF for another operation using chaining?
     * Demonstrating thenApply, thenAccept and thenRun methods
     *
     * FAQ:
     * Does chaining methods block main thread?
     */
    @Test
    void simpleChain() {
        CompletableFuture.supplyAsync(() -> personDao.getRandomPerson(2000))
            .thenApply(person -> carDao.getCar(person.id()))
            .thenAccept(car -> log("Yay car id %d returned".formatted(car.id())))
            .thenRun(() -> log("From run callback"));

        doSomeHeavyWork(3000);
    }

    /*
     * thenApply method is used to map returned value of CF
     * to another, thenCompose does the same thing so what's
     * the difference?
     *
     * TLDR, thenApply   = Optional.map()
     *       thenCompose = Optional.flatMap()
     */
    @Test
    @Disabled
    void thenApplyVsThenCompose() {
        CompletableFuture<CompletableFuture<Car>> thenApplyExample = CompletableFuture.supplyAsync(
                () -> personDao.getRandomPerson(2000)
            )
            .thenApply(person -> carDao.getCarAsync(person.id()));

        CompletableFuture<Car> thenComposeExample = CompletableFuture.supplyAsync(
                () -> personDao.getRandomPerson(2000)
            )
            .thenCompose(person -> carDao.getCarAsync(person.id()));
    }

    /*
     * FAQ:
     * If sync version of chaining methods isn't blocking
     * then why is there async versions of them?
     */
    @Test
    void syncVersionsOfChainingMethods() {
        log("CURRENT THREAD: %s".formatted(Thread.currentThread().getName()));

        CompletableFuture.completedFuture(new Person(151L, "TEST"))
            .thenApply(person -> {
                log("Getting CAR on thread: %s".formatted(Thread.currentThread().getName()));
                return carDao.getCar(person.id(), 5000);
            })
            .thenAccept(car -> {
                log("Accepting CAR on thread: %s".formatted(Thread.currentThread().getName()));
                log("Yay car id %d returned".formatted(car.id()));
            });
    }

    @Test
    void asyncVersionsOfChainingMethods() {
        log("CURRENT THREAD: %s".formatted(Thread.currentThread().getName()));

        CompletableFuture.completedFuture(new Person(151L, "TEST"))
            .thenApplyAsync(person -> {
                log("Getting CAR on thread: %s".formatted(Thread.currentThread().getName()));
                return carDao.getCar(person.id(), 5000);
            })
            .thenAcceptAsync(car -> {
                log("Accepting CAR on thread: %s".formatted(Thread.currentThread().getName()));
                log("Yay car id %d returned".formatted(car.id()));
            });
    }

    @Test
    void thenCombine() {
        CompletableFuture<Person> personFuture = CompletableFuture.supplyAsync(() -> personDao.getRandomPerson(3000));
        CompletableFuture.supplyAsync(() -> carDao.getCar(5L))
            .thenCombine(personFuture, (car, person) -> person.id() + car.id())
            .thenAccept(sum -> log("Sum is %d".formatted(sum)));

        doSomeHeavyWork(5000);
    }

}