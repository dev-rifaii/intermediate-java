package dev.rifaii.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

/*
 * There are many ways to read a file in Java:
 * InputStream (FileInputStream)
 * FileReader
 * BufferedReader/BufferedInputStream
 * Scanner
 */
public class IOBasicsTest {

    private static final String RAND_FILE_NAME = "src/test/resources/rand.txt";
    private static final String SPECIAL_FILE_NAME = "src/test/resources/special.txt";
    private static final String LARGE_FILE_NAME = "src/test/resources/large.txt";

    private static final int L_DECIMAL_REP = 76;
    private static final int O_TILDE_DECIMAL_REP = 213;

    @BeforeAll
    static void setup() {
        if (!"UTF-8".equals(Charset.defaultCharset().displayName())) {
            System.setProperty("file.encoding", "UTF-8");
        }
        Assertions.assertEquals("UTF-8", System.getProperty("file.encoding"));
    }

    /**
     * @see InputStream only reads bytes.
     */
    @Test
    void fileInputStream() throws IOException {
        File file = new File(RAND_FILE_NAME);
        Assertions.assertTrue(file.exists());

        try (InputStream inputStream = new FileInputStream(file)) {
            int byt = inputStream.read(); //It's also possible to read N bytes using #readNBytes
            Assertions.assertEquals(L_DECIMAL_REP, byt);
            Assertions.assertEquals("L", Character.toString(byt));
        }
    }

    /**
     * @see FileReader reads one CHARACTER at a time
     */
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

    /**
     * How InputStream can fail:
     */
    @Test //SHOULD FAIL
    void fileReaderVsFileInputStream1() throws IOException {
        File file = new File(SPECIAL_FILE_NAME);
        Assertions.assertTrue(file.exists());

        try (InputStream inputStream = new FileInputStream(file)) {
            int byt = inputStream.read();
            Assertions.assertEquals(O_TILDE_DECIMAL_REP, byt);
            Assertions.assertEquals("Õ", Character.toString(byt));
        }
    }

    /**
     * @see FileReader reads one CHARACTER at a time
     * Regardless of how many bytes the character is.
     * Unlike InputStream which only reads one byte at a time.
     *
     * @see FileReader uses the default system charset for character
     * encoding unless another charset is specified through the constructor:
     * @see FileReader#FileReader(File, Charset)
     *
     * So in summary, it's InputStream wrapper with character encoding
     */
    @Test
    void fileReaderVsFileInputStream2() throws IOException {
        File file = new File(SPECIAL_FILE_NAME);
        Assertions.assertTrue(file.exists());

        try (FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8)) {
            int byt = fileReader.read();
            Assertions.assertEquals(O_TILDE_DECIMAL_REP, byt);
            Assertions.assertEquals("Õ", Character.toString(byt));
        }
    }

    /*
     * What about BufferedReader?
     * BufferedReader Reads chunks of data into a buffer which
     * makes it faster than FileReader and FileInputStream
     */
    @Test
    void fileReaderForLargeFiles() throws IOException {
        File file = new File(LARGE_FILE_NAME);
        Assertions.assertTrue(file.exists());

        //Reading large file using FileReader
        var startFileReader = Instant.now();
        try (FileReader fileReader = new FileReader(file)) {
            int byt = fileReader.read();
            while (byt != -1) {
                System.out.print((char) byt);
                byt = fileReader.read();
            }
        }
        var endFileReader = Instant.now();

        //Reading large file using BufferedReader
        var startBufferedReader = Instant.now();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            int byt = bufferedReader.read();
            while (byt != -1) {
                System.out.print((char) byt);
                byt = bufferedReader.read();
            }
        }
        var endBufferedReader = Instant.now();

        Duration bufferedReaderDuration = Duration.between(startBufferedReader, endBufferedReader);
        Duration fileReaderDuration = Duration.between(startFileReader, endFileReader);
        Assertions.assertTrue(bufferedReaderDuration.compareTo(fileReaderDuration) < 0);

        System.out.printf("%nBufferedReader duration: %s", bufferedReaderDuration.toMillis());
        System.out.printf("%nFileReader duration: %s%n", fileReaderDuration.toMillis());
    }

    /**
     * The Scanner class provides a convenient API
     * that uses regex under the hood to read meaningful data
     * and avoid manual parsing. Like everything else it uses
     * @see InputStream and uses system default charset unless
     * another charset is specified.
     *
     * @see Scanner#nextInt()
     * @see Scanner#nextBoolean()
     * @see Scanner#nextBigDecimal()
     * @see Scanner#nextLine()
     */
    @Test
    void scanner() throws IOException {
        File file = new File(SPECIAL_FILE_NAME);
        Assertions.assertTrue(file.exists());

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNext()) {
                System.out.print(scanner.next());
            }
            System.out.println("\n");
        }
    }

    /*
     * Summary:
     * InputStream reads 1 byte at a time. Useful when dealing with binary data.
     * Reader reads 1 character at a time and allows to specify charset. Easier to use than InputStream when dealing with characters.
     * BufferedReader loads a chunk of chars into memory and streams them one at a time. Better for larger files.
     * Scanner makes reading different types of data easier by providing convenient apis that use regex under the hood to parse read data.
     */


    /**
     * Fun fact
     * char in java is 2 bytes so every character in a string is 2 bytes. (UTF-16)
     * But it's not possible to directly check the amount of bytes for a character
     * so demonstrating this is not easy.
     *
     * Creating a string with a single character and calling .getBytes() on it and then
     * checking length won't really prove this either, because if a charset is not specified
     * in .getBytes() then it uses the System default charset which defeats the point.
     *
     */
    @Test
    void stringBytesLength() {
        String str = "s";
        int bytesCount = str.getBytes().length;
        Assertions.assertEquals(1, bytesCount);
        System.out.println(bytesCount);
    }

    /*
     * Considering the explanation above, then one would think that
     * specifying charset to UTF-16 would give a bytes array with length '2'
     */
    @Test
    void utf16StringBytesLength() {
        String str = "s";
        int bytesCount = str.getBytes(StandardCharsets.UTF_16).length;
        Assertions.assertEquals(2, bytesCount);
    }

    /**
     *
     * If UTF-16 charset is specified when calling .getBytes() on a single character string,
     * then the length of the bytes array will be actually 4, that's because the character
     * itself will be 2 bytes in addition to a BYTE ORDER MARK appended to the start of the String
     * which indicates if the String was stored with Big-endian or little-endian.
     */
}
