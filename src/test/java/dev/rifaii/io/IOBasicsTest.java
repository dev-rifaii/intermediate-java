package dev.rifaii.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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

    @Test
    void fileInputStream() throws IOException {
        File file = new File(RAND_FILE_NAME);
        Assertions.assertTrue(file.exists());

        try (InputStream inputStream = new FileInputStream(file)) {
            int byt = inputStream.read();
            Assertions.assertEquals(76, byt);
            Assertions.assertEquals("L", Character.toString(byt));
        }
    }

    @Test
    void fileReader() throws IOException {
        File file = new File(RAND_FILE_NAME);
        Assertions.assertTrue(file.exists());

        try (FileReader fileReader = new FileReader(file)) {
            int byt = fileReader.read();
            Assertions.assertEquals(76, byt);
            Assertions.assertEquals("L", Character.toString(byt));
        }
    }

    @Test
    void fileReaderVsFileInputStream1() throws IOException {
        File file = new File(CAR_FILE_NAME);
        Assertions.assertTrue(file.exists());

        try (InputStream inputStream = new FileInputStream(file)) {
            int byt = inputStream.read();

            int secondByt = inputStream.read();
            System.out.println(secondByt);
            Assertions.assertEquals("Õ", Character.toString(byt));
        }
    }

    @Test
    void fileReaderVsFileInputStream2() throws IOException {
        File file = new File(CAR_FILE_NAME);
        Assertions.assertTrue(file.exists());

        try (FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8)) {
            int byt = fileReader.read();
            System.out.println(byt);
            System.out.println(fileReader.read());
        }
    }

}
