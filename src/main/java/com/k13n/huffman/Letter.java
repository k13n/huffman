package com.k13n.huffman;

public class Letter {
	private final char letter;
	private final double frequency;

	public Letter(char letter, double frequency) {
		this.letter = letter;
		this.frequency = frequency;
	}

	public double getFrequency() {
		return frequency;
	}

	public char getLetter() {
		return letter;
	}

	@Override
	public String toString() {
	  return letter + " " + frequency;
	}

}
