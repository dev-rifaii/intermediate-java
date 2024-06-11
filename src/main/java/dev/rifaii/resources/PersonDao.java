package dev.rifaii.resources;

import dev.rifaii.util.RandomUtility;

import java.util.Random;

import static dev.rifaii.util.RandomUtility.*;

public class PersonDao {

    public Person getRandomPerson() {
        return new Person(getRandomLong(), "FOO BAR");
    }

    public Person getRandomPerson(int sleepMillis) {
        sleep(sleepMillis);
        return new Person(getRandomLong(), "FOO BAR");
    }

}
