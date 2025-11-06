import tester.Tester;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.HashMap;

// represents a worklist containing mazetiles
interface IWorkList {

  // Adds a tile to the worklist
  void add(MazeTile tile);

  // Removes a tile from the worklist and return it
  MazeTile remove();

  // Determines whether this WorkList is empty
  boolean isEmpty();

  // Returns the number of elements in this worklist
  int size();
}

// Represents a stack of MazeTiles (LIFO)
class MazeStack implements IWorkList {

  ArrayDeque<MazeTile> deque;

  MazeStack() {
    this.deque = new ArrayDeque<MazeTile>();
  }

  // Adds the given tile to the front of the deque
  public void add(MazeTile tile) {
    this.deque.addFirst(tile);
  }

  // Removes and returns the first element of the deque
  public MazeTile remove() {
    return this.deque.removeFirst();
  }

  // returns whether this stack is empty
  public boolean isEmpty() {
    return this.deque.isEmpty();
  }

  // returns the size of this stack
  public int size() {
    return this.deque.size();
  }

}

//Represents a Queue of MazeTiles (FIFO)
class MazeQueue implements IWorkList {

  ArrayDeque<MazeTile> deque;

  MazeQueue() {
    this.deque = new ArrayDeque<MazeTile>();
  }

  // Adds the given tile to the front of the deque
  public void add(MazeTile tile) {
    this.deque.addFirst(tile);
  }

  // Removes and returns the last element of the deque
  public MazeTile remove() {
    return this.deque.removeLast();
  }

  // returns whether this queue is empty
  public boolean isEmpty() {
    return this.deque.isEmpty();
  }

  // returns the size of this queue
  public int size() {
    return this.deque.size();
  }
}

class ExamplesWorkList {
  MazeTile tile1 = new MazeTile(0, 0);
  MazeTile tile2 = new MazeTile(1, 0);
  MazeTile tile3 = new MazeTile(0, 1);
  MazeTile tile4 = new MazeTile(1, 1);

  void testCreateTree(Tester t) {
    HashMap<MazeTile, MazeTile> hash = new HashMap<MazeTile, MazeTile>();
    hash.put(tile1, tile1);
    hash.put(tile2, tile2);
    hash.put(tile3, tile3);
    hash.put(tile4, tile4);

    ArrayList<Edge> edges = new ArrayList<Edge>();
    Edge edge1 = new Edge(tile1, tile2, 10);
    Edge edge2 = new Edge(tile1, tile3, 20);
    Edge edge3 = new Edge(tile2, tile4, 30);
    Edge edge4 = new Edge(tile3, tile4, 40);
    edges.add(edge1);
    edges.add(edge2);
    edges.add(edge3);
    edges.add(edge4);

    KruskalTreeBuilder builder = new KruskalTreeBuilder(hash, edges);
    ArrayList<Edge> tree = builder.createTree();

    t.checkExpect(tree.size(), 3);

    t.checkExpect(tree.contains(edge1), true);
    t.checkExpect(tree.contains(edge2), true);
    t.checkExpect(tree.contains(edge3), true);

    HashMap<MazeTile, MazeTile> hash2 = new HashMap<MazeTile, MazeTile>();
    hash2.put(tile1, tile1);
    hash2.put(tile2, tile2);
    hash2.put(tile3, tile3);
    hash2.put(tile4, tile4);

    hash2.put(tile1, tile2);

    KruskalTreeBuilder builder2 = new KruskalTreeBuilder(hash2, edges);
    ArrayList<Edge> tree2 = builder2.createTree();

    t.checkExpect(tree2.size(), 2);

    HashMap<MazeTile, MazeTile> hash3 = new HashMap<MazeTile, MazeTile>();
    hash3.put(tile1, tile1);
    hash3.put(tile2, tile2);
    hash3.put(tile3, tile3);
    hash3.put(tile4, tile4);

    ArrayList<Edge> edges3 = new ArrayList<Edge>();
    Edge edgeA = new Edge(tile1, tile2, 40);
    Edge edgeB = new Edge(tile1, tile3, 30);
    Edge edgeC = new Edge(tile2, tile4, 20);
    Edge edgeD = new Edge(tile3, tile4, 10);
    edges3.add(edgeA);
    edges3.add(edgeB);
    edges3.add(edgeC);
    edges3.add(edgeD);

    KruskalTreeBuilder builder3 = new KruskalTreeBuilder(hash3, edges3);
    ArrayList<Edge> tree3 = builder3.createTree();

    t.checkExpect(tree3.size(), 3);
    t.checkExpect(tree3.contains(edgeD), false);
    t.checkExpect(tree3.contains(edgeC), true);
  }
}
