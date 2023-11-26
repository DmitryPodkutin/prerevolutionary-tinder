package ru.liga.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64EncoderDecoder {

    public static String encode(String text) {
        final byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decode(String encodedText) {
        final byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
