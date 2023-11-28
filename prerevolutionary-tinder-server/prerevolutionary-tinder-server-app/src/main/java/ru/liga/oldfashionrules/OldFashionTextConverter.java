package ru.liga.oldfashionrules;

import java.util.HashMap;
import java.util.Map;

public class OldFashionTextConverter implements OldFashionConverter {

    private final Map<String, String> oldWords = new HashMap<>();

    public OldFashionTextConverter() {
        oldWords.put("е", "ѣ");
    }

    public String convertToOldFashion(String string) {
        String[] words = string.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            String convertedWord = processWord(word);
            result.append(convertedWord).append(" ");
        }

        return result.toString().trim();
    }

    private String processWord(String word) {
        // Проверка на наличие слова в таблице oldWords для замены
        if (oldWords.containsKey(word)) {
            return oldWords.get(word);
        } else {
            // Замены согласно указанным правилам
            if (word.endsWith("ъ")) {
                return word;
            } else if (word.endsWith("и")) {
                return word;
            } else if (word.endsWith("й")) {
                return word;
            } else if (word.matches(".*[^аеёиоуыэюя]и$")) {
                return word.substring(0, word.length() - 1) + "i";
            } else {
                return word;
            }
        }
    }
}
