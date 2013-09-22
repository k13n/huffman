package com.k13n.huffman;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class HuffmanCoderTest {
  private Letter[] alphabet;
  private HuffmanCoder huffman;

  @Before
  public void setUp() throws Exception {
	  alphabet = new Letter[] {
	      new Letter('A', 0.60),
	      new Letter('B', 0.25),
	      new Letter('C', 0.10),
	      new Letter('D', 0.05),
	  };
	  huffman = new HuffmanCoder(alphabet);
  }

  @Test
  public void encodeSingleLetter() {
    assertEquals("0", huffman.encode("A"));
    assertEquals("10", huffman.encode("B"));
    assertEquals("110", huffman.encode("C"));
    assertEquals("111", huffman.encode("D"));
  }

  @Test
  public void encodeEmptyString() {
    assertEquals("", huffman.encode(""));
  }

  @Test(expected = NoSuchElementException.class)
  public void encodeLetterThatIsNotInAlphabet() {
    huffman.encode("E");
  }

  @Test
  public void encodeString() {
    assertEquals("010100", huffman.encode("ABBA"));
    assertEquals("0110111110", huffman.encode("ACDC"));
  }

  @Test
  public void decodeSingleLetter() {
    assertEquals("A", huffman.decode("0"));
    assertEquals("B", huffman.decode("10"));
    assertEquals("C", huffman.decode("110"));
    assertEquals("D", huffman.decode("111"));
  }

  @Test
  public void decodeEmptyString() {
    assertEquals("", huffman.decode(""));
  }

  @Test(expected = IllegalArgumentException.class)
  public void decodeInvalidBitSequence() {
    huffman.decode("1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void decodePartiallyInvalidBitSequence() {
    huffman.decode("011");
  }

  @Test
  public void decodeString() {
    assertEquals("ABBA", huffman.decode("010100"));
    assertEquals("ACDC", huffman.decode("0110111110"));
  }

}
