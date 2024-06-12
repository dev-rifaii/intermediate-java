package dev.rifaii;

import dev.rifaii.resources.CarDao;
import dev.rifaii.resources.PersonDao;
import dev.rifaii.util.RandomUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static dev.rifaii.util.RandomUtility.log;

public abstract class TestBase {

    public final PersonDao personDao = new PersonDao();
    public final CarDao carDao = new CarDao();

    @BeforeEach
    void before() {
        log("\033[0;32mSTART\033[0m");
    }

    @AfterEach
    void after() {
        log("\033[0;32mEND\033[0m");
    }
}
