package com.k13n.huffman;

import java.util.HashMap;
import java.util.Map;

public class AlphabetGenerator {

  public static Letter[] fromText(String text) {
    Map<Character, Integer> alphabetMap = buildFrequencyTable(text);
    Letter[] alphabet = new Letter[alphabetMap.size()];
    int index = 0;
    for (Map.Entry<Character, Integer> entry : alphabetMap.entrySet())
      alphabet[index++] = new Letter(entry.getKey(), entry.getValue());
    return alphabet;
  }

  private static Map<Character, Integer> buildFrequencyTable(String text) {
    Map<Character, Integer> alphabetMap = new HashMap<>();
    for (int i = 0; i < text.length(); i++) {
      char letter = text.charAt(i);
      incLetterCount(alphabetMap, letter);
    }
    return alphabetMap;
  }

  private static void incLetterCount(Map<Character, Integer> map, char letter) {
    Integer count = map.get(letter);
    count = (count == null) ? 1 : count + 1;
    map.put(letter, count);
  }

}
