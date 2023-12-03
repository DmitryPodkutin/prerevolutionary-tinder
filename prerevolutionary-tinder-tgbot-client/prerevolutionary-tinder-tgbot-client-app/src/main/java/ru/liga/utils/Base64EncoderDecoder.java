package ru.liga.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utility class for encoding and decoding strings in Base64 format.
 */
public class Base64EncoderDecoder {

    /**
     * Encodes text into Base64 format.
     *
     * @param text The text to be encoded.
     * @return The string encoded in Base64 format.
     */
    public static String encode(String text) {
        final byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Decodes a string from Base64 format.
     *
     * @param encodedText The encoded string in Base64 format.
     * @return The original string after decoding.
     */
    public static String decode(String encodedText) {
        final byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
