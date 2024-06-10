package dev.rifaii.resources;

import dev.rifaii.util.RandomUtility;

import java.util.Random;

import static dev.rifaii.util.RandomUtility.getRandomLong;

public class PersonDao {

    public Person getRandomPerson() {
        return new Person(getRandomLong(), "Test");
    }

    public Person getRandomPerson(int sleepMillis) {
        try {
            Thread.sleep(sleepMillis);
            return new Person(getRandomLong(), "Test");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
