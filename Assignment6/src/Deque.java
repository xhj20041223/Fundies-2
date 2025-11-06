import tester.Tester;

import java.util.Iterator;
//import java.util.function.*;
import java.util.function.Predicate;

// represents a node in a generic list
interface INode<T> {
  // inserts a new node with the given value after this node
  void insertAfter(T value);

  // removes this node from the list and returns this value
  T remove();

  // given this node, advance one step, returns the next node in the list,
  // if there is none, return itself
  ANode<T> advance();

  // checks if there is a next node to advance to
  boolean canAdvance();

  // checks whether the given node is the same as this node
  boolean sameAbstractNode(ANode<T> node);

  // checks whether the given node is the same as this node
  boolean sameNode(Node<T> node);

  // checks whether the given sentinel is the same as this node
  boolean sameSentinel(Sentinel<T> sentinel);
}

// the implementations of a node in a generic list
abstract class ANode<T> implements INode<T> {
  // the next item in this list
  ANode<T> next;
  // the previous item of this list
  ANode<T> prev;

  // inserts a new node after this node
  public void insertAfter(T value) {
    // make new node
    Node<T> newNode = new Node<T>(value, this.next, this);
    // update this node's links
    this.next = newNode;
  }

  // given this node, advance one step, returns the next node in the list,
  // if there is none, return itself
  public ANode<T> advance() {
    return this.next;
  }

  // checks if there is a next node to advance to
  public boolean canAdvance() {
    return this.next instanceof Node;
  }

  // checks whether the given node is the same as this node
  public boolean sameNode(Node<T> node) {
    return false;
  }

  // checks whether the given node is the same as this node
  public boolean sameSentinel(Sentinel<T> sentinel) {
    return false;
  }
}

// represents a node that contains a value
class Node<T> extends ANode<T> {
  // the data of this node
  T data;

  // the constructor
  Node(T value) {
    super();
    this.data = value;
  }

  // convenience constructor
  Node(T value, ANode<T> next, ANode<T> previous) {
    super();
    if (next == null || previous == null) {
      throw new IllegalArgumentException("given nodes are null.");
    }
    this.data = value;
    this.next = next;
    this.prev = previous;
    // update other nodes to refer to this node
    this.next.prev = this;
    this.prev.next = this;
  }

  // removes this node from the list and returns this node's value
  public T remove() {
    // change links
    this.prev.next = this.next;
    this.next.prev = this.prev;
    return this.data;
  }

  // checks whether the given node is the same as this node
  public boolean sameAbstractNode(ANode<T> node) {
    return node.sameNode(this);
  }

  // checks whether the given node is the same as this node
  public boolean sameNode(Node<T> node) {
    return this.data.equals(node.data);
    // also check links?
  }
}

// represents a node that does not contain a value
class Sentinel<T> extends ANode<T> {
  // constructor that takes in zero arguments and initializes
  // next and previous to itself
  Sentinel() {
    super();
    this.next = this;
    this.prev = this;
  }

  // throws exception when removal of this node from this empty list is tried
  public T remove() {
    throw new RuntimeException("Can't remove from an empty list.");
  }

  // checks whether the given node is the same as this node
  public boolean sameAbstractNode(ANode<T> node) {
    return node.sameSentinel(this);
  }

  // checks whether the given node is the same as this node
  public boolean sameSentinel(Sentinel<T> sentinel) {
    return true;
  }
}

// represents a list
class Deque<T> implements Iterable<T> {
  // the start and end of the list
  Sentinel<T> header;

  // the constructor
  Deque() {
    this.header = new Sentinel<T>();
  }

  // convenience constructor
  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // returns an iterator that iterates forward
  public Iterator<T> iterator() {
    // forward as default
    return new DequeForwardIterator<T>(this.header.next);
  }

  // returns an iterator that iterates backwards
  Iterator<T> reverseIterator() {
    // forward as default
    return new DequeReverseIterator<T>(this.header.prev);
  }

  // returns the number of nodes in this list, excluding the header
  int size() {
    Iterator<T> iterator = this.iterator();
    int size = 0;
    // adds one to the size while list has remaining elements
    while (iterator.hasNext()) {
      // iterates through this deque
      size += 1;
      // continue to iterate
      iterator.next();
    }
    return size;
  }

  // EFFECT: adds the given node to the front of the list
  void addAtHead(T value) {
    // insert new node after sentinel
    this.header.insertAfter(value);
  }

  // EFFECT: inserts the given node to the tail of the list
  void addAtTail(T value) {
    // insert new node after sentinel's previous
    this.header.prev.insertAfter(value);
  }

  // removes the first node from this list and returns that node value
  T removeFromHead() {
    return this.header.next.remove();
  }

  // removes the last node from this list and returns that node value
  T removeFromTail() {
    return this.header.prev.remove();
  }

  // returns the first node in which the given predicate is true
  ANode<T> find(Predicate<T> pred) {
    // just dispatch it to the nodes, do not need to use iterator

    // while there are still nodes, iterate through list
    // and check each element with the given predicate
    ANode<T> currNode = this.header.next;
    while (currNode != this.header) {
      Node<T> node = (Node<T>) currNode;
      if (pred.test(node.data)) {
        return currNode;
      }
      currNode = currNode.next;
    }
    // else, no node that satisfies predicate is found
    return this.header;
  }

  // removes the given node from this list
  void removeNode(ANode<T> nodeToRemove) {
    // finds the node in the list and removes it
    // just compare values or need to compare links?
    ANode<T> currNode = this.header;
    while (currNode.canAdvance()) {
      if (currNode.sameAbstractNode(nodeToRemove)) {
        currNode.remove();
        return;
      }
      currNode = currNode.advance();
    }
    // else reached the end of list - don't do anything
  }
}

// represents an iterator that goes forward in the list
class DequeForwardIterator<T> implements Iterator<T> {
  // the list to iterate through
  ANode<T> source;

  // the constructor
  DequeForwardIterator(ANode<T> source) {
    this.source = source;
  }

  // checks whether there exists a next node
  public boolean hasNext() {
    // check if next one is a node or goes back to sentinel:
    return this.source instanceof Node;
  }

  // retrieves this node's value and goes on to the next
  public T next() {
    // checks if there are elements
    if (!this.hasNext()) { // next one is sentinel
      throw new RuntimeException("No elements to iterate through.");
    }
    Node<T> abstractNodeAsNode = (Node<T>) source;
    T ans = abstractNodeAsNode.data;
    this.source = abstractNodeAsNode.next;
    return ans;
  }

  // removes this node from the list
  public void remove() {
    this.source.remove();
  }
}

// represents an iterator that goes backwards in a list
class DequeReverseIterator<T> implements Iterator<T> {
  // the list to iterate through
  ANode<T> source;

  // the constructor
  DequeReverseIterator(ANode<T> source) {
    this.source = source;
  }

  // checks whether there exists a next node
  public boolean hasNext() {
    // check if next one is a node or goes back to sentinel:
    return this.source instanceof Node;
  }

  // retrieves this node's value and goes to the previous node
  public T next() {
    // checks if there are elements
    if (!this.hasNext()) { // next one is sentinel
      throw new RuntimeException("No elements to iterate through.");
    }
    Node<T> abstractNodeAsNode = (Node<T>) source;
    T ans = abstractNodeAsNode.data;
    this.source = abstractNodeAsNode.prev;
    return ans;
  }

  // removes this node from the list
  public void remove() {
    this.source.remove();
  }
}

// determine whether an integer is zero or not
class IsZero implements Predicate<Integer> {
  public boolean test(Integer i) {
    return i == 0;
  }
}

//determine whether an integer is negative or not
class IsNega implements Predicate<Integer> {
  public boolean test(Integer i) {
    return i < 0;
  }
}

//determine whether an integer is positive or not
class IsPosi implements Predicate<Integer> {
  public boolean test(Integer i) {
    return i > 0;
  }
}

//determine whether an integer is odd or not
class IsOdd implements Predicate<Integer> {
  public boolean test(Integer i) {
    return i % 2 == 1;
  }
}

//determine whether an integer is even or not
class IsEven implements Predicate<Integer> {
  public boolean test(Integer i) {
    return i % 2 == 0;
  }
}

class ExamplesDeque {
  // empty list
  Deque<String> deque1 = new Deque<String>();

  Sentinel<String> s1 = new Sentinel<String>();
  Node<String> def = new Node<String>("def", s1, s1);
  Node<String> cde = new Node<String>("cde", def, s1);
  Node<String> bcd = new Node<String>("bcd", cde, s1);
  Node<String> abc = new Node<String>("abc", bcd, s1);
  Deque<String> deque2 = new Deque<String>(s1);

  Sentinel<String> s2 = new Sentinel<String>();
  Node<String> v4 = new Node<String>("me.", s2, s2);
  Node<String> v3 = new Node<String>("is", v4, s2);
  Node<String> v2 = new Node<String>("this", v3, s2);
  Node<String> v1 = new Node<String>("Hello", v2, s2);
  Deque<String> deque3 = new Deque<String>(s2);

  Deque<Integer> emptyDeque;
  Deque<Integer> singleElementDeque;
  Deque<Integer> multiElementDeque;
  Deque<Integer> findTestDeque;
  Deque<String> edgeCaseDeque;

  void initDeque() {
    emptyDeque = new Deque<>();

    singleElementDeque = new Deque<>();
    singleElementDeque.addAtHead(5);

    multiElementDeque = new Deque<>();
    multiElementDeque.addAtHead(3);
    multiElementDeque.addAtTail(7);
    multiElementDeque.addAtHead(1);

    findTestDeque = new Deque<>();
    findTestDeque.addAtTail(0);
    findTestDeque.addAtTail(-5);
    findTestDeque.addAtTail(10);
    findTestDeque.addAtTail(0); // Duplicate zero

    edgeCaseDeque = new Deque<>(); // Empty deque
  }
  // ----------------------------------------------------------------------------------------------
  // Tests for size()

  void testSize1(Tester t) {
    t.checkExpect(deque2.size(), 4);
  }

  // Test addAtHead()
  void testAddAtHead(Tester t) {
    this.initDeque();
    emptyDeque.addAtHead(10);
    t.checkExpect(emptyDeque.size(), 1);
    t.checkExpect(emptyDeque.removeFromHead(), 10);

    multiElementDeque.addAtHead(0);
    t.checkExpect(multiElementDeque.size(), 4);
    t.checkExpect(multiElementDeque.removeFromHead(), 0);
  }

  // Test addAtTail()
  void testAddAtTail(Tester t) {
    this.initDeque();
    emptyDeque.addAtTail(20);
    t.checkExpect(emptyDeque.size(), 1);
    t.checkExpect(emptyDeque.removeFromTail(), 20);

    multiElementDeque.addAtTail(9);
    t.checkExpect(multiElementDeque.size(), 4);
    t.checkExpect(multiElementDeque.removeFromTail(), 9);
  }

  // Test removeFromHead()
  void testRemoveFromHead(Tester t) {
    this.initDeque();
    t.checkException(new RuntimeException("Can't remove from an empty list."), emptyDeque,
        "removeFromHead");
    t.checkExpect(singleElementDeque.removeFromHead(), 5);
    t.checkExpect(singleElementDeque.size(), 0);

    t.checkExpect(multiElementDeque.removeFromHead(), 1);
    t.checkExpect(multiElementDeque.size(), 2);
  }

  // Test removeFromTail()
  void testRemoveFromTail(Tester t) {
    this.initDeque();
    t.checkException(new RuntimeException("Can't remove from an empty list."), emptyDeque,
        "removeFromTail");
    t.checkExpect(singleElementDeque.removeFromTail(), 5);
    t.checkExpect(singleElementDeque.size(), 0);

    t.checkExpect(multiElementDeque.removeFromTail(), 7);
    t.checkExpect(multiElementDeque.size(), 2);
  }

  // Test iterator()
  void testIterator(Tester t) {
    this.initDeque();
    Iterator<Integer> it = multiElementDeque.iterator();
    t.checkExpect(it.hasNext(), true);
    t.checkExpect(it.next(), 1);
    t.checkExpect(it.next(), 3);
    t.checkExpect(it.next(), 7);
    t.checkExpect(it.hasNext(), false);
  }

  // Test reverseIterator()
  void testReverseIterator(Tester t) {
    this.initDeque();
    Iterator<Integer> rit = multiElementDeque.reverseIterator();
    t.checkExpect(rit.hasNext(), true);
    t.checkExpect(rit.next(), 7);
    t.checkExpect(rit.next(), 3);
    t.checkExpect(rit.next(), 1);
    t.checkExpect(rit.hasNext(), false);
  }

  // Test edge cases for iterator removal
  void testIteratorRemove(Tester t) {
    this.initDeque();
    Iterator<Integer> it = multiElementDeque.iterator();
    it.next();
    it.remove(); // Remove first element
    t.checkExpect(multiElementDeque.size(), 2);
    t.checkExpect(multiElementDeque.iterator().next(), 1);
  }

  // Test find() method with valid Deques
  void testFindValid(Tester t) {
    this.initDeque();
    ANode<Integer> zeroNode = findTestDeque.find(new IsZero());
    t.checkExpect(((Node<Integer>) zeroNode).data, 0);

    ANode<Integer> negNode = findTestDeque.find(new IsNega());
    t.checkExpect(((Node<Integer>) negNode).data, -5);
  }

  // Test find() with no matches Deques
  void testFindNoMatch(Tester t) {
    this.initDeque();
    ANode<Integer> notFound = findTestDeque.find(i -> i > 100);
    t.checkExpect(notFound instanceof Sentinel, true);
  }

  // Test find() on empty Deques
  void testFindEmpty(Tester t) {
    this.initDeque();
    ANode<String> emptyResult = edgeCaseDeque.find(s -> true);
    t.checkExpect(emptyResult instanceof Sentinel, true);
  }

  // Test find() with multiple matches Deques
  void testFindFirstOccurrence(Tester t) {
    this.initDeque();
    ANode<Integer> firstZero = findTestDeque.find(new IsZero());
    t.checkExpect(((Node<Integer>) firstZero).data, 0);
    t.checkExpect(firstZero.sameAbstractNode(findTestDeque.header.next), true);
  }

}