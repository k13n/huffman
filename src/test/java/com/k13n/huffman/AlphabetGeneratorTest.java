package com.k13n.huffman;

import static org.junit.Assert.*;

import org.junit.Test;

public class AlphabetGeneratorTest {
  private Letter[] alphabet;
  private Letter letter;

  @Test
  public void generateFromSingleLetter() {
    alphabet = AlphabetGenerator.fromText("A");
    assertEquals('A', alphabet[0].getLetter());
    assertEquals(1, alphabet[0].getFrequency(), 0);
  }

  @Test
  public void generateFromEmptyString() {
    alphabet = AlphabetGenerator.fromText("");
    assertEquals(0, alphabet.length);
  }

  @Test
  public void generateFromSpecialCharacter() {
    alphabet = AlphabetGenerator.fromText("€");
    assertEquals(1, alphabet.length);
    assertEquals('€', alphabet[0].getLetter());
  }

  @Test
  public void generateFromText() {
    alphabet = AlphabetGenerator.fromText("ACDC");

    letter = findLetter(alphabet, 'A');
    assertEquals('A', letter.getLetter());
    assertEquals(1, letter.getFrequency(), 0);

    letter = findLetter(alphabet, 'C');
    assertEquals('C', letter.getLetter());
    assertEquals(2, letter.getFrequency(), 0);

    letter = findLetter(alphabet, 'D');
    assertEquals('D', letter.getLetter());
    assertEquals(1, letter.getFrequency(), 0);
  }

  private static Letter findLetter(Letter[] alphabet, char letter) {
    for (Letter element : alphabet)
      if (element.getLetter() == letter)
        return element;
    return null;
  }

}
