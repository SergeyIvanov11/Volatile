package org.example;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static AtomicInteger counterOf3 = new AtomicInteger(0);
    static AtomicInteger counterOf4 = new AtomicInteger(0);
    static AtomicInteger counterOf5 = new AtomicInteger(0);
    static volatile String[] texts = new String[100_000];

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread TextCheckerForPalindromesThread = new Thread(new TextCheckerForPalindromes());
        Thread TextCheckerForSameLettersThread = new Thread(new TextCheckerForSameLetters());
        Thread TextCheckerForIncreaseLettersThread = new Thread(new TextCheckerForIncreaseLetters());

        TextCheckerForPalindromesThread.start();
        TextCheckerForSameLettersThread.start();
        TextCheckerForIncreaseLettersThread.start();

        TextCheckerForPalindromesThread.join();
        TextCheckerForSameLettersThread.join();
        TextCheckerForIncreaseLettersThread.join();

        System.out.println("Красивых слов с длиной 3: " + counterOf3.get() + " шт\n" +
                "Красивых слов с длиной 4: " + counterOf4.get() + " шт\n" +
                "Красивых слов с длиной 5: " + counterOf5.get() + " шт");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static class TextCheckerForPalindromes implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < texts.length; i++) {
                if (checkForPalindromes(texts[i])) {
                    if (texts[i].length() == 3) {
                        counterOf3.addAndGet(1);
                    } else if (texts[i].length() == 4) {
                        counterOf4.addAndGet(1);
                    } else if (texts[i].length() == 5) {
                        counterOf5.addAndGet(1);
                    }
                }
            }
        }
    }

    public static class TextCheckerForSameLetters implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < texts.length; i++) {
                if (checkForSameLetters(texts[i])) {
                    if (texts[i].length() == 3) {
                        counterOf3.getAndIncrement();
                    } else if (texts[i].length() == 4) {
                        counterOf4.getAndIncrement();
                    } else if (texts[i].length() == 5) {
                        counterOf5.getAndIncrement();
                    }
                }
            }
        }
    }

    public static class TextCheckerForIncreaseLetters implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < texts.length; i++) {
                if (checkForIncreaseLetters(texts[i])) {
                    if (texts[i].length() == 3) {
                        counterOf3.getAndIncrement();
                    } else if (texts[i].length() == 4) {
                        counterOf4.getAndIncrement();
                    } else if (texts[i].length() == 5) {
                        counterOf5.getAndIncrement();
                    }
                }
            }
        }
    }

    public static boolean checkForSameLetters(String text) {
        char c = text.charAt(0);
        int count = 1;
        for (int j = 1; j < text.length(); j++) {
            if (c == text.charAt(j)) {
                count++;
            } else return false;
        }
        if (count == text.length()) {
            return true;
        } else return false;
    }

    public static boolean checkForPalindromes(String text) {
        return text.equals(new StringBuilder(text).reverse().toString());
    }


    public static boolean checkForIncreaseLetters(String text) {
        char c = text.charAt(0);
        for (int j = 1; j < text.length(); j++) {
            if (c <= text.charAt(j)) {
                c = text.charAt(j);
            } else return false;
        }
        if (!checkForSameLetters(text)) {
            return true;
        } else return false;
    }
}