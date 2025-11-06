import java.util.ArrayList;
import java.util.HashMap;

import tester.Tester;

// A Class that determines which blocks are connected in the maze using Kruskal's algorithm
class KruskalTreeBuilder {
  HashMap<MazeTile, MazeTile> hash;
  ArrayList<Edge> edges;

  // takes in a hashmap of tiles, and a Arraylist of edges, sorted by weight
  // ascending
  KruskalTreeBuilder(HashMap<MazeTile, MazeTile> hash, ArrayList<Edge> edges) {
    this.hash = hash;
    this.edges = edges;
  }

  // Creates the tree, or list of edges
  ArrayList<Edge> createTree() {

    ArrayList<Edge> tree = new ArrayList<Edge>();
    int numTiles = hash.size();

    // iterates through the edges and adds a edge to the tree if
    // it's tiles are not already connected
    // when adding, updates the hashmap
    for (Edge edge : this.edges) {
      if (!edge.isConnected(hash)) {
        edge.join(hash);
        tree.add(edge);
      }

      if (tree.size() >= numTiles - 1) {
        return tree;
      }
    }
    return tree;
  }

}

class ExamplesKruskalTreeBuilder {

  KruskalTreeBuilder testBuilder;
  HashMap<MazeTile, MazeTile> testHash;
  ArrayList<Edge> testEdges;

  MazeTile testTile1;
  MazeTile testTile2;
  MazeTile testTile3;
  MazeTile testTile4;
  MazeTile testTile5;
  MazeTile testTile6;

  Edge testEdge12;
  Edge testEdge13;
  Edge testEdge23;
  Edge testEdge24;
  Edge testEdge35;
  Edge testEdge45;
  Edge testEdge46;
  Edge testEdge56;

  void init() {
    this.testTile1 = new MazeTile(0, 0);
    this.testTile2 = new MazeTile(0, 1);
    this.testTile3 = new MazeTile(1, 0);
    this.testTile4 = new MazeTile(1, 1);
    this.testTile5 = new MazeTile(2, 0);
    this.testTile6 = new MazeTile(2, 1);

    this.testEdge12 = new Edge(this.testTile1, this.testTile2, 10);
    this.testEdge13 = new Edge(this.testTile1, this.testTile3, 5);
    this.testEdge23 = new Edge(this.testTile2, this.testTile3, 15);
    this.testEdge24 = new Edge(this.testTile2, this.testTile4, 8);
    this.testEdge35 = new Edge(this.testTile3, this.testTile5, 7);
    this.testEdge45 = new Edge(this.testTile4, this.testTile5, 12);
    this.testEdge46 = new Edge(this.testTile4, this.testTile6, 6);
    this.testEdge56 = new Edge(this.testTile5, this.testTile6, 9);

    this.testHash = new HashMap<MazeTile, MazeTile>();
    this.testHash.put(this.testTile1, this.testTile1);
    this.testHash.put(this.testTile2, this.testTile2);
    this.testHash.put(this.testTile3, this.testTile3);
    this.testHash.put(this.testTile4, this.testTile4);
    this.testHash.put(this.testTile5, this.testTile5);
    this.testHash.put(this.testTile6, this.testTile6);

    this.testEdges = new ArrayList<Edge>();
    this.testEdges.add(this.testEdge13);
    this.testEdges.add(this.testEdge46);
    this.testEdges.add(this.testEdge35);
    this.testEdges.add(this.testEdge24);
    this.testEdges.add(this.testEdge56);
    this.testEdges.add(this.testEdge12);
    this.testEdges.add(this.testEdge45);
    this.testEdges.add(this.testEdge23);

    this.testEdges.sort(new BiggerWeight());

    this.testBuilder = new KruskalTreeBuilder(this.testHash, this.testEdges);
  }

  void testCreateTreeBasic(Tester t) {
    this.init();

    ArrayList<Edge> tree = this.testBuilder.createTree();

    t.checkExpect(tree.size(), 5);

    t.checkExpect(tree.contains(this.testEdge13), true);
    t.checkExpect(tree.contains(this.testEdge46), true);
    t.checkExpect(tree.contains(this.testEdge35), true);
    t.checkExpect(tree.contains(this.testEdge24), true);
    t.checkExpect(tree.contains(this.testEdge56), true);
  }

  void testCreateTreeWithCycle(Tester t) {
    this.init();

    ArrayList<Edge> edges = new ArrayList<Edge>();
    edges.add(this.testEdge13);
    edges.add(this.testEdge35);

    KruskalTreeBuilder builder = new KruskalTreeBuilder(this.testHash, edges);
    ArrayList<Edge> tree = builder.createTree();

    t.checkExpect(tree.size(), 2);
  }

  void testCreateTreeEmpty(Tester t) {
    this.init();

    ArrayList<Edge> edges = new ArrayList<Edge>();
    KruskalTreeBuilder builder = new KruskalTreeBuilder(this.testHash, edges);
    ArrayList<Edge> tree = builder.createTree();

    t.checkExpect(tree.size(), 0);
  }

  void testCreateTreeSingleEdge(Tester t) {
    this.init();

    ArrayList<Edge> edges = new ArrayList<Edge>();
    edges.add(this.testEdge13);

    HashMap<MazeTile, MazeTile> hash = new HashMap<MazeTile, MazeTile>();
    hash.put(this.testTile1, this.testTile1);
    hash.put(this.testTile3, this.testTile3);

    KruskalTreeBuilder builder = new KruskalTreeBuilder(hash, edges);
    ArrayList<Edge> tree = builder.createTree();

    t.checkExpect(tree.size(), 1);
    t.checkExpect(tree.get(0), this.testEdge13);
  }

  void testCreateTreePreConnected(Tester t) {
    this.init();

    HashMap<MazeTile, MazeTile> hash = new HashMap<MazeTile, MazeTile>();
    hash.put(this.testTile1, this.testTile1);
    hash.put(this.testTile2, this.testTile1);
    hash.put(this.testTile3, this.testTile3);

    ArrayList<Edge> edges = new ArrayList<Edge>();
    edges.add(this.testEdge12);
    edges.add(this.testEdge13);

    KruskalTreeBuilder builder = new KruskalTreeBuilder(hash, edges);
    ArrayList<Edge> tree = builder.createTree();

    t.checkExpect(tree.size(), 1);
    t.checkExpect(tree.contains(this.testEdge13), true);
  }

  void testCreateTreeLargerMaze(Tester t) {
    this.init();

    MazeTile a = new MazeTile(0, 2);
    MazeTile b = new MazeTile(1, 2);
    MazeTile c = new MazeTile(2, 2);

    this.testHash.put(a, a);
    this.testHash.put(b, b);
    this.testHash.put(c, c);

    Edge edgeAB = new Edge(a, b, 3);
    Edge edgeBC = new Edge(b, c, 4);
    Edge edgeA1 = new Edge(a, this.testTile1, 11);
    Edge edgeB2 = new Edge(b, this.testTile2, 14);
    Edge edgeC6 = new Edge(c, this.testTile6, 2);

    ArrayList<Edge> edges = new ArrayList<Edge>();
    edges.add(this.testEdge13);
    edges.add(this.testEdge46);
    edges.add(this.testEdge35);
    edges.add(this.testEdge24);
    edges.add(this.testEdge56);
    edges.add(edgeC6);
    edges.add(edgeAB);
    edges.add(edgeBC);
    edges.add(edgeA1);
    edges.add(edgeB2);

    edges.sort(new BiggerWeight());

    KruskalTreeBuilder builder = new KruskalTreeBuilder(this.testHash, edges);
    ArrayList<Edge> tree = builder.createTree();

    t.checkExpect(tree.size(), 8);
  }

  void testCreateTreeVariousConnections(Tester t) {
    this.init();

    HashMap<MazeTile, MazeTile> hash = new HashMap<MazeTile, MazeTile>();
    hash.put(this.testTile1, this.testTile1);
    hash.put(this.testTile2, this.testTile2);
    hash.put(this.testTile3, this.testTile3);

    Edge edge12 = new Edge(this.testTile1, this.testTile2, 5);
    Edge edge13 = new Edge(this.testTile1, this.testTile3, 10);
    Edge edge23 = new Edge(this.testTile2, this.testTile3, 15);

    ArrayList<Edge> edges = new ArrayList<Edge>();
    edges.add(edge12);
    edges.add(edge13);
    edges.add(edge23);

    KruskalTreeBuilder builder = new KruskalTreeBuilder(hash, edges);
    ArrayList<Edge> tree = builder.createTree();

    t.checkExpect(tree.size(), 2);
    t.checkExpect(tree.contains(edge12), true);
    t.checkExpect(tree.contains(edge13), true);
    t.checkExpect(tree.contains(edge23), false);
  }

  void testCreateTreeEarlyTermination(Tester t) {
    this.init();

    HashMap<MazeTile, MazeTile> hash = new HashMap<MazeTile, MazeTile>();
    hash.put(this.testTile1, this.testTile1);
    hash.put(this.testTile2, this.testTile2);
    hash.put(this.testTile3, this.testTile3);

    ArrayList<Edge> manyEdges = new ArrayList<Edge>();
    for (int i = 0; i < 100; i++) {
      manyEdges.add(new Edge(this.testTile1, this.testTile2, i));
      manyEdges.add(new Edge(this.testTile2, this.testTile3, i + 100));
    }

    KruskalTreeBuilder builder = new KruskalTreeBuilder(hash, manyEdges);
    ArrayList<Edge> tree = builder.createTree();

    t.checkExpect(tree.size(), 2);
  }

  void testCreateTreeWithDuplicateEdges(Tester t) {
    this.init();

    HashMap<MazeTile, MazeTile> hash = new HashMap<MazeTile, MazeTile>();
    hash.put(this.testTile1, this.testTile1);
    hash.put(this.testTile2, this.testTile2);

    Edge edge1 = new Edge(this.testTile1, this.testTile2, 5);
    Edge edge2 = new Edge(this.testTile1, this.testTile2, 10);

    ArrayList<Edge> edges = new ArrayList<Edge>();
    edges.add(edge1);
    edges.add(edge2);

    KruskalTreeBuilder builder = new KruskalTreeBuilder(hash, edges);
    ArrayList<Edge> tree = builder.createTree();

    t.checkExpect(tree.size(), 1);
    t.checkExpect(tree.contains(edge1), true);
    t.checkExpect(tree.contains(edge2), false);
  }
}
