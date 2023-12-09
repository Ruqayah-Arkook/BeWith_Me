package com.app.cardfeature7;

import java.util.Random;
public class KeyGenerator {
    public static String generateKey() {
        // Generate a key with 4 characters and 4 numbers
        String letters = generateRandomString(4, true);
        String numbers = generateRandomString(4, false);
        return letters + numbers;
    }
    private static String generateRandomString(int length, boolean useLetters) {
        String characters;
        if (useLetters) {
            characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        } else {
            characters = "0123456789";
        }
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }
        return stringBuilder.toString();
    }
}
