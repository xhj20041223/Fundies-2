import java.util.ArrayList;
import tester.Tester;

// represent a node exist in the Huffman code alphabet tree
interface INode {

  // count the total frequency exist in this node
  int totalFrequency();

  // flatten the node to a list of node
  ArrayList<INode> flatten();

  // encode the giving string to the a list of boolean by the Huffman code
  // will throw exception if a character is not in the alphabet
  ArrayList<Boolean> encode(String target);

  // determine whether the target character exist in this node
  boolean contains(String target);

  // try to decode the first character from the giving list of booleans and delete
  // those bits.
  // if no character can be reached, return "?"
  String decode(ArrayList<Boolean> target);
}

// represent a node in huffman code that combine two other nodes
class Node implements INode {
  // represent the node in the left (code with 0 and frequency is less or equal to
  // the right one)
  INode left;
  // represent the node in the right (code with 1 and frequency is more or equal
  // to the left one)
  INode right;

  Node(INode left, INode right) {
    this.left = left;
    this.right = right;

  }

  // count the total frequency exist in this node
  public int totalFrequency() {
    return left.totalFrequency() + right.totalFrequency();
  }

  // flatten the node to a list of node
  public ArrayList<INode> flatten() {
    ArrayList<INode> result = new ArrayList<INode>();
    for (INode n : this.left.flatten()) {
      result.add(n);
    }
    result.add(this);
    for (INode n : this.right.flatten()) {
      result.add(n);
    }
    return result;
  }

  // encode the giving string to the a list of boolean by the Huffman code
  // will throw exception if a character is not in the alphabet
  public ArrayList<Boolean> encode(String target) {
    ArrayList<Boolean> result = new ArrayList<Boolean>();
    if (this.left.contains(target)) {
      result.add(false);
      for (Boolean b : left.encode(target)) {
        result.add(b);
      }
    }
    else if (this.right.contains(target)) {
      result.add(true);
      for (Boolean b : right.encode(target)) {
        result.add(b);
      }
    }
    else {
      throw new IllegalArgumentException(
          "Tried to encode " + target + " but that is not part of the language.");
    }
    return result;
  }

  // determine whether the target character exist in this node
  public boolean contains(String target) {
    return this.left.contains(target) || this.right.contains(target);
  }

  // try to decode the first character from the giving list of booleans and delete
  // those bits.
  // if no character can be reached, return "?"
  public String decode(ArrayList<Boolean> target) {
    if (target.size() < 1) {
      return "?";
    }
    else {
      if (target.get(0)) {
        target.remove(0);
        return this.right.decode(target);
      }
      else {
        target.remove(0);
        return this.left.decode(target);
      }
    }
  }
}

// represent a character and its frequency count
class Character implements INode {
  // represent the character in this node
  String character;
  // represent the frequency of the character appear
  int frequency;

  Character(String character, int frequency) {
    this.character = character;
    this.frequency = frequency;
  }

  // count the total frequency exist in this node
  public int totalFrequency() {
    return frequency;
  }

  // flatten the node to a list of node
  public ArrayList<INode> flatten() {
    ArrayList<INode> result = new ArrayList<INode>();
    result.add(this);
    return result;
  }

  // encode the giving string to the a list of boolean by the Huffman code
  // will throw exception if a character is not in the alphabet
  public ArrayList<Boolean> encode(String target) {
    return new ArrayList<Boolean>();
  }

  // determine whether the target character exist in this node
  public boolean contains(String target) {
    return this.character.equals(target);
  }

  // try to decode the first character from the giving list of booleans and delete
  // those bits.
  // if no character can be reached, return "?"
  public String decode(ArrayList<Boolean> target) {
    return this.character;
  }

}

// represent a Huffman code system
class Huffman {
  // represent a tree of the Huffman code system
  INode tree;
  // an ArrayList represent the Huffman code system tree being flatten
  ArrayList<INode> flattenTree;

  Huffman(ArrayList<String> strs, ArrayList<Integer> frequency) {
    if (strs.size() != frequency.size()) {
      throw new IllegalArgumentException("the sizes of two array list do not match");
    }
    else if (strs.size() < 2) {
      throw new IllegalArgumentException("the sizes of two array list should be greater than 1");
    }
    this.flattenTree = new ArrayList<INode>();
    for (int i = 0; i < strs.size(); i += 1) {
      this.flattenTree.add(new Character(strs.get(i), frequency.get(i)));
    }
    this.toTree();
  }

  // Determine whether the flattenTree is sorted from less to more in frequency
  boolean isSorted() {
    for (int i = 0; i < this.flattenTree.size() - 1; i += 1) {
      if (this.flattenTree.get(i).totalFrequency() > this.flattenTree.get(i + 1).totalFrequency()) {
        return false;
      }
    }
    return true;
  }

  // sort the flattenTree from less to more by using bubble sort
  void sortAlphabet() {
    if (this.isSorted()) {
      return;
    }
    else {
      for (int i = 0; i < this.flattenTree.size() - 1; i += 1) {
        if (this.flattenTree.get(i).totalFrequency() > this.flattenTree.get(i + 1)
            .totalFrequency()) {
          INode helper = this.flattenTree.get(i);
          this.flattenTree.set(i, this.flattenTree.get(i + 1));
          this.flattenTree.set(i + 1, helper);
        }
      }
      this.sortAlphabet();
    }
  }

  // determine which index should the new node being inserted to satisfy the sort
  // of from less to more on frequency
  int insertIndex(int frequency) {
    for (int i = 0; i < this.flattenTree.size(); i += 1) {
      if (frequency < this.flattenTree.get(i).totalFrequency()) {
        return i;
      }
    }
    return this.flattenTree.size();
  }

  // generate the tree of the given Huffman code system and the flatten tree
  void toTree() {
    this.sortAlphabet();
    if (this.flattenTree.size() > 1) {
      INode newNode = new Node(this.flattenTree.get(0), this.flattenTree.get(1));
      this.flattenTree.add(this.insertIndex(newNode.totalFrequency()), newNode);

      this.flattenTree.remove(0);
      this.flattenTree.remove(0);
      this.toTree();
    }
    else {
      this.tree = this.flattenTree.get(0);
    }
    this.flattenTree();
  }

  // flatten the tree from left to right
  void flattenTree() {
    this.flattenTree = this.tree.flatten();
  }

  // encode the giving string to a list of boolean by using this Huffman code
  // system, will throw an
  // exception if any character is not included in the system
  ArrayList<Boolean> encode(String target) {
    ArrayList<Boolean> result = new ArrayList<Boolean>();
    for (int i = 0; i < target.length(); i += 1) {
      for (Boolean b : tree.encode(target.substring(i, i + 1))) {
        result.add(b);
      }
    }
    return result;
  }

  // decode the given list of boolean to a string by using the Huffman code
  // system. will return ? if
  // the remaining booleans cannot reach to a certain character
  String decode(ArrayList<Boolean> target) {
    ArrayList<Boolean> newTarget = new ArrayList<Boolean>();
    for (Boolean b : target) {
      newTarget.add(b);
    }
    String result = "";
    while (newTarget.size() >= 1) {
      result = result + tree.decode(newTarget);
    }
    return result;
  }
}

// test cases for the Huffman
class ExamplesHuffman {

  ArrayList<String> MtLoStr1;
  ArrayList<Integer> MtLoInt1;

  ArrayList<String> LoStr1;
  ArrayList<Integer> LoInt1;

  ArrayList<String> LoStr2;
  ArrayList<Integer> LoInt2;

  ArrayList<String> LoStr3;
  ArrayList<Integer> LoInt3;

  Huffman h1;
  Huffman h2;

  ArrayList<String> LoStr4;
  ArrayList<Integer> LoInt4;
  Huffman h3;

  ArrayList<String> LoStr5;
  ArrayList<Integer> LoInt5;
  Huffman h4;

  void initiHuffman() {
    MtLoStr1 = new ArrayList<String>();
    MtLoInt1 = new ArrayList<Integer>();

    LoStr1 = new ArrayList<String>();
    LoInt1 = new ArrayList<Integer>();

    LoStr2 = new ArrayList<String>();
    LoInt2 = new ArrayList<Integer>();

    LoStr3 = new ArrayList<String>();
    LoInt3 = new ArrayList<Integer>();

    LoStr1.add("a");
    LoInt1.add(0);

    LoStr2.add("a");
    LoInt2.add(5);
    LoStr2.add("b");
    LoInt2.add(6);
    LoStr2.add("c");
    LoInt2.add(11);

    LoStr3.add("a");
    LoInt3.add(30);
    LoStr3.add("b");
    LoInt3.add(6);
    LoStr3.add("c");
    LoInt3.add(6);
    LoStr3.add("d");
    LoInt3.add(20);
    LoStr3.add("e");
    LoInt3.add(20);

    h1 = new Huffman(LoStr2, LoInt2);
    h2 = new Huffman(LoStr3, LoInt3);

    LoStr4 = new ArrayList<String>();
    LoInt4 = new ArrayList<Integer>();
    LoStr4.add("x");
    LoInt4.add(3);
    LoStr4.add("y");
    LoInt4.add(7);
    LoStr4.add("z");
    LoInt4.add(1);
    h3 = new Huffman(LoStr4, LoInt4);

    LoStr5 = new ArrayList<String>();
    LoInt5 = new ArrayList<Integer>();
    LoStr5.add("m");
    LoInt5.add(10);
    LoStr5.add("n");
    LoInt5.add(10);
    h4 = new Huffman(LoStr5, LoInt5);
  }

  void testConstructor(Tester t) {
    this.initiHuffman();
    t.checkConstructorException(
        new IllegalArgumentException("the sizes of two array list should be greater than 1"),
        "Huffman", MtLoStr1, MtLoInt1);
    t.checkConstructorException(
        new IllegalArgumentException("the sizes of two array list should be greater than 1"),
        "Huffman", LoStr1, LoInt1);
    t.checkConstructorException(
        new IllegalArgumentException("the sizes of two array list do not match"), "Huffman", LoStr1,
        LoInt2);
    t.checkConstructorException(
        new IllegalArgumentException("the sizes of two array list do not match"), "Huffman", LoStr2,
        LoInt3);
    t.checkConstructorException(
        new IllegalArgumentException("the sizes of two array list do not match"), "Huffman", LoStr3,
        LoInt2);
    t.checkConstructorException(
        new IllegalArgumentException("the sizes of two array list do not match"), "Huffman", LoStr3,
        MtLoInt1);
  }

  void testEncodeException(Tester t) {
    this.initiHuffman();
    t.checkException(
        new IllegalArgumentException("Tried to encode f but that is not part of the language."), h2,
        "encode", "fabc!");
    t.checkException(
        new IllegalArgumentException("Tried to encode @ but that is not part of the language."), h2,
        "encode", "@afc!");
  }

  void testEncodeSame(Tester t) {
    this.initiHuffman();
    ArrayList<Boolean> encode2 = new ArrayList<Boolean>();

    encode2.add(true);
    encode2.add(true);
    encode2.add(true);
    encode2.add(true);
    encode2.add(true);
    encode2.add(true);
    t.checkExpect(h2.encode("aaa"), encode2);
  }

  void testEncodeSimple(Tester t) {
    this.initiHuffman();
    ArrayList<Boolean> encode1 = new ArrayList<Boolean>();
    encode1.add(true);
    encode1.add(false);
    encode1.add(true);
    encode1.add(true);
    t.checkExpect(h1.encode("ab"), encode1);
  }

  void testEncode(Tester t) {
    this.initiHuffman();
    ArrayList<Boolean> encode1 = new ArrayList<Boolean>();
    encode1.add(true);
    encode1.add(true);
    encode1.add(false);
    encode1.add(false);
    encode1.add(false);
    encode1.add(false);
    encode1.add(false);
    encode1.add(true);
    encode1.add(false);
    encode1.add(true);

    t.checkExpect(h2.encode("abcd"), encode1);
  }

  void testDecodeIncomplete(Tester t) {
    this.initiHuffman();
    ArrayList<Boolean> encode1 = new ArrayList<Boolean>();
    encode1.add(true);
    encode1.add(true);
    encode1.add(false);
    encode1.add(false);
    encode1.add(false);
    encode1.add(false);
    encode1.add(false);
    encode1.add(true);
    encode1.add(false);

    t.checkExpect(h2.decode(encode1), "abc?");
  }

  void testOnlyQuestion(Tester t) {
    this.initiHuffman();
    ArrayList<Boolean> encode1 = new ArrayList<Boolean>();
    encode1.add(false);
    encode1.add(true);
    encode1.add(true);
    t.checkExpect(h1.decode(encode1), "cb");
  }

  void testDecodeSimple(Tester t) {
    this.initiHuffman();
    ArrayList<Boolean> encode1 = new ArrayList<Boolean>();
    encode1.add(true);
    t.checkExpect(h1.decode(encode1), "?");
  }

  void testDecode(Tester t) {
    this.initiHuffman();
    ArrayList<Boolean> encode1 = new ArrayList<Boolean>();
    encode1.add(true);
    encode1.add(true);
    encode1.add(false);
    encode1.add(false);
    encode1.add(false);
    encode1.add(false);
    encode1.add(false);
    encode1.add(true);
    encode1.add(false);
    encode1.add(true);

    t.checkExpect(h2.decode(encode1), "abcd");
  }

  void testTotalFrequency(Tester t) {
    this.initiHuffman();
    t.checkExpect(new Character("a", 5).totalFrequency(), 5);
    t.checkExpect(new Node(new Character("a", 2), new Character("b", 3)).totalFrequency(), 5);
    t.checkExpect(h3.tree.totalFrequency(), 11); // 3+7+1=11
    t.checkExpect(h4.tree.totalFrequency(), 20); // 10+10=20
  }

  void testFlatten(Tester t) {
    this.initiHuffman();
    ArrayList<INode> flatChar = new Character("a", 1).flatten();
    t.checkExpect(flatChar.size(), 1);
    t.checkExpect(flatChar.get(0) instanceof Character, true);

    INode node = new Node(new Character("a", 1), new Character("b", 2));
    ArrayList<INode> flatNode = node.flatten();
    t.checkExpect(flatNode.size(), 3); // [a, Node, b]
    t.checkExpect(flatNode.get(1) instanceof Node, true);
  }

  void testContains(Tester t) {
    this.initiHuffman();
    t.checkExpect(h3.tree.contains("x"), true);
    t.checkExpect(h3.tree.contains("w"), false);
    t.checkExpect(h4.tree.contains("m"), true);
    t.checkExpect(new Character("a", 1).contains("b"), false);
  }

  void testInsertIndex(Tester t) {
    this.initiHuffman();
    h3.flattenTree = new ArrayList<INode>();
    h3.flattenTree.add(new Character("a", 1));
    h3.flattenTree.add(new Character("b", 3));
    t.checkExpect(h3.insertIndex(2), 1);
    t.checkExpect(h3.insertIndex(0), 0);
    t.checkExpect(h3.insertIndex(5), 2);
  }

  void testIsSorted(Tester t) {
    this.initiHuffman();
    h3.flattenTree.clear();
    h3.flattenTree.add(new Character("a", 1));
    h3.flattenTree.add(new Character("b", 3));
    t.checkExpect(h3.isSorted(), true);

    h3.flattenTree.add(0, new Character("c", 5));
    t.checkExpect(h3.isSorted(), false);
  }

  void testSortAlphabet(Tester t) {
    this.initiHuffman();
    h3.flattenTree.clear();
    h3.flattenTree.add(new Character("z", 5));
    h3.flattenTree.add(new Character("y", 3));
    h3.sortAlphabet();
    t.checkExpect(h3.flattenTree.get(0).totalFrequency(), 3);
    t.checkExpect(h3.flattenTree.get(1).totalFrequency(), 5);
  }
}