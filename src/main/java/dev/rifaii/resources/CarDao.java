package dev.rifaii.resources;

import dev.rifaii.util.RandomUtility;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static dev.rifaii.util.RandomUtility.sleep;

public class CarDao {

    public Car getCar(Long personId) {
        return new Car(RandomUtility.getRandomLong(), personId);
    }

    public Car getCar(Long personId, long sleepMillis) {
        sleep(sleepMillis);
        return new Car(RandomUtility.getRandomLong(), personId);
    }

    public CompletableFuture<Car> getCarAsync(Long personId) {
       sleep(500);
        return CompletableFuture.supplyAsync(
            () -> new Car(RandomUtility.getRandomLong(), personId)
        );
    }
}
