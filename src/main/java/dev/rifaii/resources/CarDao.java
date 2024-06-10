package dev.rifaii.resources;

import dev.rifaii.util.RandomUtility;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class CarDao {

    public Car getCar(Long personId) {
        return new Car(RandomUtility.getRandomLong(), personId);
    }

    public CompletableFuture<Car> getCarAsync(Long personId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return CompletableFuture.supplyAsync(
            () -> new Car(RandomUtility.getRandomLong(), personId)
        );
    }
}
