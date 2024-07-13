package dev.rifaii.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.Duration;
import java.time.Instant;

/*
 * There are many ways to read a file in Java:
 * InputStream (FileInputStream)
 * FileReader
 * Scanner
 * ....
 */
public class IOBasicsTest {

    private static final String RAND_FILE_NAME = "src/test/resources/rand.txt";
    private static final String CAR_FILE_NAME = "src/test/resources/car.txt";
    private static final String LARGE_FILE_NAME = "src/test/resources/large.txt";

    private static final int L_DECIMAL_REP = 76;
    private static final int O_TILDE_DECIMAL_REP = 213;

    @BeforeAll
    static void setup() {
        Assertions.assertEquals("UTF-8", System.getProperty("file.encoding"));
    }

    @Test
    void fileInputStream() throws IOException {
        File file = new File(RAND_FILE_NAME);
        Assertions.assertTrue(file.exists());

        try (InputStream inputStream = new FileInputStream(file)) {
            int byt = inputStream.read();
            Assertions.assertEquals(L_DECIMAL_REP, byt);
            Assertions.assertEquals("L", Character.toString(byt));
        }
    }

    @Test
    void fileReader() throws IOException {
        File file = new File(RAND_FILE_NAME);
        Assertions.assertTrue(file.exists());

        try (FileReader fileReader = new FileReader(file)) {
            int byt = fileReader.read();
            Assertions.assertEquals(L_DECIMAL_REP, byt);
            Assertions.assertEquals("L", Character.toString(byt));
        }
    }

    @Test //FAILS
    void fileReaderVsFileInputStream1() throws IOException {
        File file = new File(CAR_FILE_NAME);
        Assertions.assertTrue(file.exists());

        try (InputStream inputStream = new FileInputStream(file)) {
            int byt = inputStream.read();
            Assertions.assertAll(() -> {
                Assertions.assertEquals(O_TILDE_DECIMAL_REP, byt);
                Assertions.assertEquals("Õ", Character.toString(byt));
            });
        }
    }

    @Test
    void fileReaderVsFileInputStream2() throws IOException {
        File file = new File(CAR_FILE_NAME);
        Assertions.assertTrue(file.exists());

        try (FileReader fileReader = new FileReader(file)) {
            int byt = fileReader.read();
            Assertions.assertAll(() -> {
                Assertions.assertEquals(O_TILDE_DECIMAL_REP, byt);
                Assertions.assertEquals("Õ", Character.toString(byt));
            });
        }
    }

    @Test
    void fileReaderForLargeFiles() throws IOException {
        File file = new File(LARGE_FILE_NAME);
        Assertions.assertTrue(file.exists());

        var start = Instant.now();
        try (FileReader fileReader = new FileReader(file)) {
            int byt = fileReader.read();
            while (byt != -1) {
                System.out.print((char) byt);
                byt = fileReader.read();
            }
        }
        var end = Instant.now();

        Assertions.assertTrue(Duration.between(start, end).toMillis() < 1000);
    }

    /*
     * Reads chunks of data into a buffer that is stored in memory
     * which makes it faster than FileReader and FileInputStream
     */
    @Test
    void bufferedReaderForLargeFiles() throws IOException {
        File file = new File(LARGE_FILE_NAME);
        Assertions.assertTrue(file.exists());

        var start = Instant.now();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            int byt = bufferedReader.read();
            while (byt != -1) {
                System.out.print((char) byt);
                byt = bufferedReader.read();
            }
        }

        var end = Instant.now();

        Assertions.assertTrue(Duration.between(start, end).toMillis() < 1000);
    }

    /*
     * Summary:
     * InputStream reads 1 byte at a time
     * Reader reads 1 character at a time (uses InputStream)
     * BufferedReader loads a chunk of chars into memory and streams then one at a time (uses InputStream)
     * Scanner uses Regex to read specific data types (int, double etc...) instead of just plain bytes or chars
     */

}
