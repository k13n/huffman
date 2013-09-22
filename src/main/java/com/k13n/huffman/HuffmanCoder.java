package com.k13n.huffman;

import java.util.Comparator;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class HuffmanCoder {
  private final Letter[] alphabet;
  private final Set<Node> leaves;
  private final Node root;

  public HuffmanCoder(Letter[] alphabet) {
    this.alphabet = alphabet;
    leaves = createLeavesFromAlphabet();
    root = buildHuffmanTable();
  }

  private Set<Node> createLeavesFromAlphabet() {
    Set<Node> leaves = new HashSet<>();
    for (Letter letter : alphabet)
      leaves.add(new Node(letter));
    return leaves;
  }

  private Node buildHuffmanTable() {
    Queue<Node> heap = newMinFrequencyHeapFromLeaves();
    while (heap.size() != 1) {
      Node rightChild = heap.poll();
      Node leftChild = heap.poll();
      Node parent = Node.merge(leftChild, rightChild);
      heap.add(parent);
    }
    Node root = heap.poll();
    return root;
  }

  private Queue<Node> newMinFrequencyHeapFromLeaves() {
    int size = leaves.size();
    PriorityQueue<Node> heap = new PriorityQueue<>(size, new Comparator<Node>() {
      @Override public int compare(Node node1, Node node2) {
        return Double.compare(node1.frequency, node2.frequency);
      }
    });
    heap.addAll(leaves);
    return heap;
  }

  public String encode(String text) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < text.length(); i++)
      encodeLetter(text.charAt(i), builder);
    return builder.toString();
  }

  private void encodeLetter(char letter, StringBuilder builder) {
    int offset = builder.length();
    Node leaf = getLeafOfLetter(letter);
    while (leaf != root) {
      char digit = mapSideToDigit(leaf.side);
      builder.insert(offset, digit);
      leaf = leaf.parent;
    }
  }

  public String decode(String text) {
    StringBuilder builder = new StringBuilder();
    Node currentNode = root;
    for (int i = 0; i < text.length(); i++) {
      currentNode = traverseTreeToNextDeeperNode(currentNode, text.charAt(i));
      if (currentNode.isLeave()) {
        char letter = currentNode.letter.getLetter();
        builder.append(letter);
        currentNode = root;
      }
    }
    if (currentNode != root)
      throw new IllegalArgumentException("text is malformed and can be "
          + "decoded only partially");
    return builder.toString();
  }

  private Node traverseTreeToNextDeeperNode(Node currentNode, char letter) {
    Side side = mapDigitToSide(letter);
    if (side == Side.LEFT)
      currentNode = currentNode.leftChild;
    else
      currentNode = currentNode.rightChild;
    return currentNode;
  }

  private char mapSideToDigit(Side side) {
    return side == Side.LEFT ? '0' : '1';
  }

  private Side mapDigitToSide(char digit) {
    return digit == '0' ? Side.LEFT : Side.RIGHT;
  }

  private Node getLeafOfLetter(char letter) {
    for (Node leaf : leaves)
      if (leaf.letter.getLetter() == letter)
        return leaf;
    throw new NoSuchElementException("letter '" + letter
        + "' does not belong to the alphabet");
  }

  private static enum Side {
    LEFT, RIGHT;
  }

  private static final class Node {
    final double frequency;
    final Letter letter;
    final Node leftChild, rightChild;
    Node parent;
    Side side;

    private Node(double frequency, Node leftChild, Node rightChild) {
      this.frequency = frequency;
      this.leftChild = leftChild;
      this.rightChild = rightChild;
      this.letter = null;
    }

    public Node(Letter letter) {
      this.letter = letter;
      this.leftChild = null;
      this.rightChild = null;
      this.frequency = letter.getFrequency();
    }

    public boolean isLeave() {
      return letter != null;
    }

    public static Node merge(Node leftChild, Node rightChild) {
      double frequency = leftChild.frequency + rightChild.frequency;
      Node parent = new Node(frequency, leftChild, rightChild);
      leftChild.parent = parent;
      leftChild.side = Side.LEFT;
      rightChild.parent = parent;
      rightChild.side = Side.RIGHT;
      return parent;
    }

  }

}
