package com.k13n.huffman;

import java.util.HashSet;
import java.util.NoSuchElementException;
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

  private Node buildHuffmanTable() {
    Set<Node> nodes = new HashSet<Node>(leaves);
    while (nodes.size() != 1) {
      Node rightChild = extractMinFrequencyNode(nodes);
      Node leftChild = extractMinFrequencyNode(nodes);
      Node parent = Node.merge(leftChild, rightChild);
      nodes.add(parent);
    }
    Node root = nodes.iterator().next();
    return root;
  }

  private Set<Node> createLeavesFromAlphabet() {
    Set<Node> leaves = new HashSet<>();
    for (Letter letter : alphabet)
      leaves.add(new Node(letter));
    return leaves;
  }

  private Node extractMinFrequencyNode(Set<Node> nodes) {
    double minFrequency = Double.MAX_VALUE;
    Node smallestNode = null;
    for (Node node : nodes) {
      if (node.getFrequency() < minFrequency) {
        minFrequency = node.getFrequency();
        smallestNode = node;
      }
    }
    nodes.remove(smallestNode);
    return smallestNode;
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
      char digit = mapSideToDigit(leaf.getSide());
      builder.insert(offset, digit);
      leaf = leaf.getParent();
    }
  }

  public String decode(String text) {
    StringBuilder builder = new StringBuilder();
    Node currentNode = root;
    for (int i = 0; i < text.length(); i++) {
      currentNode = traverseTreeToNextDeeperNode(currentNode, text.charAt(i));
      if (currentNode.isLeave()) {
        char letter = currentNode.getLetter().getLetter();
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
      currentNode = currentNode.getLeftChild();
    else
      currentNode = currentNode.getRightChild();
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
      if (leaf.getLetter().getLetter() == letter)
        return leaf;
    throw new NoSuchElementException("letter '" + letter
        + "' does not belong to the alphabet");
  }

  public void dump() {
    for (Node leaf : leaves) {
      char letter = leaf.getLetter().getLetter();
      String encoding = encode(Character.toString(letter));
      System.out.println(letter + ": " + encoding);
    }
  }

  private enum Side {
    LEFT, RIGHT;
  }

  private static final class Node {
    private final double frequency;
    private final Letter letter;
    private final Node leftChild, rightChild;
    private Node parent;
    private Side side;

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

    public double getFrequency() {
      return frequency;
    }

    public Node getLeftChild() {
      return leftChild;
    }

    public Letter getLetter() {
      return letter;
    }

    public Node getParent() {
      return parent;
    }

    private void setParent(Node parent) {
      this.parent = parent;
    }

    public Node getRightChild() {
      return rightChild;
    }

    public void setSide(Side side) {
      this.side = side;
    }

    public Side getSide() {
      return side;
    }

    public boolean isLeave() {
      return letter != null;
    }

    public static Node merge(Node leftChild, Node rightChild) {
      double frequency = leftChild.getFrequency() + rightChild.getFrequency();
      Node parent = new Node(frequency, leftChild, rightChild);
      leftChild.setParent(parent);
      leftChild.setSide(Side.LEFT);
      rightChild.setParent(parent);
      rightChild.setSide(Side.RIGHT);
      return parent;
    }

  }

}
