package dev.rifaii.util;

import java.time.LocalTime;

public class RandomUtility {

    public static long getRandomLong() {
        return (long) (1 + (Math.random() * 500));
    }


    public static void log(String str) {
        System.out.println("\u001B[31m" + LocalTime.now() + "\u001B[0m" + " " + str);
    }

    public static void log(boolean bool) {
        System.out.println("\u001B[31m" + LocalTime.now() + "\u001B[0m" + " " + bool);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
