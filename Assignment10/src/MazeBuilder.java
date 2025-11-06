import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import tester.Tester;

// A class that is used to build maze of a given width and height
class MazeBuilder {

  ArrayList<ArrayList<MazeTile>> buildMaze(int width, int height) {

    ArrayList<ArrayList<MazeTile>> grid = this.buildGrid(width, height);

    ArrayList<Edge> sortedEdges = this.buildEdges(grid);

    HashMap<MazeTile, MazeTile> connections = this.buildInitialHash(grid);

    KruskalTreeBuilder treeBuilder = new KruskalTreeBuilder(connections, sortedEdges);

    ArrayList<Edge> tree = treeBuilder.createTree();

    // For each edge in the tree, connects the two MazeTiles the edge represents
    for (Edge edge : tree) {
      edge.connect();
    }

    grid.get(0).get(0).initializeDistanceToStart();
    grid.get(width - 1).get(height - 1).initializeDistanceToEnd();

    return grid;
  }

  // builds a 2d arraylist of Maze tiles with size based on the given width and
  // height
  ArrayList<ArrayList<MazeTile>> buildGrid(int width, int height) {
    ArrayList<ArrayList<MazeTile>> grid = new ArrayList<ArrayList<MazeTile>>();

    for (int x = 0; x < width; x += 1) {
      ArrayList<MazeTile> column = new ArrayList<MazeTile>();

      for (int y = 0; y < height; y += 1) {
        MazeTile tile = new MazeTile(x, y);
        column.add(tile);
      }

      grid.add(column);
    }

    return grid;

  }

  // Creates an edge between all adjacent mazeTiles with a random weight between 0
  // and 100.
  // Returns all edges as a arraylist of edges that is sorted by weight
  // (ascending)
  ArrayList<Edge> buildEdges(ArrayList<ArrayList<MazeTile>> grid) {
    ArrayList<Edge> edges = new ArrayList<Edge>();

    int width = grid.size();

    int height;
    if (width == 0) {
      height = 0;
    }
    else {
      height = grid.get(0).size();
    }

    for (int x = 0; x < width; x += 1) {

      for (int y = 0; y < height; y += 1) {

        if (y != height - 1) {
          edges
              .add(new Edge(grid.get(x).get(y), grid.get(x).get(y + 1), new Random().nextInt(100)));
        }

        if (x != width - 1) {
          edges
              .add(new Edge(grid.get(x).get(y), grid.get(x + 1).get(y), new Random().nextInt(100)));
        }
      }
    }

    edges.sort(new BiggerWeight());

    return edges;
  }

  // builds a hashtable with each tile mapped to itself
  HashMap<MazeTile, MazeTile> buildInitialHash(ArrayList<ArrayList<MazeTile>> tiles) {
    HashMap<MazeTile, MazeTile> hash = new HashMap<MazeTile, MazeTile>();

    for (ArrayList<MazeTile> column : tiles) {

      for (MazeTile tile : column) {
        hash.put(tile, tile);
      }
    }
    return hash;
  }

}

// Represents a tile in a maze
class MazeTile {
  private int x;
  private int y;
  private ArrayList<MazeTile> connections;
  private int distanceToEnd;
  private int distanceToStart;

  MazeTile(int x, int y) {
    this.x = x;
    this.y = y;
    this.connections = new ArrayList<MazeTile>();
  }

  // Connects this mazetile to the given one
  void addConnection(MazeTile tile) {
    this.connections.add(tile);
  }

  // checks if this mazetile is connected to one at the given positon
  boolean isConnected(int x, int y) {

    // checks if a tile in connections is at x, y
    for (MazeTile tile : this.connections) {
      if (tile.isAt(x, y)) {
        return true;
      }
    }
    return false;
  }

  // To be called on end point of the maze, initializes the distances of all
  // points to the end
  void initializeDistanceToEnd() {
    this.distanceToEnd = 0;

    // initialized the distance to end of each tile this is connected to
    for (MazeTile tile : this.connections) {
      tile.initializeDistanceToEnd(0, this);
    }
  }

  // Given the distance of the mazetile that called this method from the end
  // and that maze tile, initialized the distanceToEnd field for all maze
  // tiles on this path
  void initializeDistanceToEnd(int distanceSoFar, MazeTile from) {
    this.distanceToEnd = distanceSoFar + 1;

    // initialized the distance to end of each tile this is connected to
    for (MazeTile tile : this.connections) {
      if (tile != from) {
        tile.initializeDistanceToEnd(this.distanceToEnd, this);
      }
    }
  }

  // To be called on start point of the maze, initializes the distances of all
  // points to the start
  void initializeDistanceToStart() {
    this.distanceToStart = 0;

    // initialized the distance to start of each tile this is connected to
    for (MazeTile tile : this.connections) {
      tile.initializeDistanceToStart(0, this);
    }
  }

  // Given the distance of the mazetile that called this method from the start
  // and that maze tile, initialized the distanceToStart field for all maze
  // tiles on this path
  void initializeDistanceToStart(int distanceSoFar, MazeTile from) {
    this.distanceToStart = distanceSoFar + 1;

    // initialized the distance to start of each tile this is connected to
    // except the tile that called this one
    for (MazeTile tile : this.connections) {
      if (tile != from) {
        tile.initializeDistanceToStart(this.distanceToStart, this);
      }
    }
  }

  // returns whether this tile is at the given position
  boolean isAt(int x, int y) {
    return this.x == x && this.y == y;
  }

  // given a worldscene, a blocksize, and a color, draws this tile on the scene
  void draw(WorldScene scene, int blockSize, Color color) {
    int centerX = x * blockSize + blockSize / 2;
    int centerY = y * blockSize + blockSize / 2;

    // Draw the tile
    scene.placeImageXY(new RectangleImage(blockSize, blockSize, OutlineMode.SOLID, color), centerX,
        centerY);

    // Draw left wall
    if (!this.isConnected(this.x - 1, this.y) && this.x != 0) {
      scene.placeImageXY(new LineImage(new Posn(0, blockSize), Color.BLACK), x * blockSize,
          centerY);
    }

    // Draw top wall
    if (!this.isConnected(this.x, this.y - 1) && this.y != 0) {
      scene.placeImageXY(new LineImage(new Posn(blockSize, 0), Color.BLACK), centerX,
          y * blockSize);
    }

    // Draw right wall
    if (!this.isConnected(this.x + 1, this.y)) {
      scene.placeImageXY(new LineImage(new Posn(0, blockSize), Color.BLACK), (x + 1) * blockSize,
          centerY);
    }

    // Draw bottom wall
    if (!this.isConnected(this.x, this.y + 1)) {
      scene.placeImageXY(new LineImage(new Posn(blockSize, 0), Color.BLACK), centerX,
          (y + 1) * blockSize);
    }
  }

  // adds unseen neighbor tiles to the worklist and updates the hashmap
  void addUnseenConnectionsTo(IWorkList workList, ArrayList<MazeTile> seen,
      HashMap<MazeTile, MazeTile> cameFrom) {

    // adds unseen neighbor tiles to the worklist and updates the hashmap
    for (MazeTile tile : this.connections) {

      if (!seen.contains(tile)) {
        workList.add(tile);
        cameFrom.put(tile, this);
      }
    }
  }

  // returns the maximum of the given integer and the distance from the tile to
  // the end
  int biggerFromEnd(int max) {
    return Math.max(this.distanceToEnd, max);
  }

  // returns the maximum of the given integer and the distance from the tile to
  // the start
  int biggerFromStart(int max) {
    return Math.max(this.distanceToStart, max);
  }

  // given the maximum distance to the start tile,
  // returns the gradient color that represents the distance of this tile to the
  // start
  Color toStartColor(int maxToStart) {
    double ratio = (double) this.distanceToStart / maxToStart;
    int red = (int) (255 * ratio);
    int green = (int) (255 * (1 - ratio));
    int blue = 0;
    return new Color(red, green, blue);
  }

  // given the maximum distance to the end tile,
  // returns the gradient color that represents the distance of this tile to the
  // end
  Color toEndColor(int maxToEnd) {
    double ratio = (double) this.distanceToEnd / maxToEnd;
    int red = (int) (255 * ratio);
    int green = (int) (255 * (1 - ratio));
    int blue = 0;
    return new Color(red, green, blue);
  }

}

// represents a weighted edge between two tiles
// (is used temporarily to construct a MST)
class Edge {

  private MazeTile tile1;
  private MazeTile tile2;
  private int weight;

  Edge(MazeTile tile1, MazeTile tile2, int weight) {
    this.tile1 = tile1;
    this.tile2 = tile2;
    this.weight = weight;
  }

  // connects the two tiles of this edge
  void connect() {
    this.tile1.addConnection(this.tile2);
    this.tile2.addConnection(this.tile1);
  }

  // given a hashmap of tile connections,
  // checks if the two tiles on this edge are already connected
  boolean isConnected(HashMap<MazeTile, MazeTile> hash) {
    MazeTile ref1 = this.findRef(this.tile1, hash);
    MazeTile ref2 = this.findRef(this.tile2, hash);
    return ref1 == ref2;
  }

  // given a tile and a hashmap of tile connections,
  // returns the parent tile of the group the tile is in
  MazeTile findRef(MazeTile tile, HashMap<MazeTile, MazeTile> hash) {
    if (!hash.containsKey(tile)) {
      throw new IllegalArgumentException("Hash does not contain given MazeTile");
    }

    if (tile == hash.get(tile)) {
      return tile;
    }

    return this.findRef(hash.get(tile), hash);
  }

  // joins the two tiles this edge is connected to in the given hash map of tile
  // connections
  void join(HashMap<MazeTile, MazeTile> hash) {
    MazeTile rep1 = this.findRef(this.tile1, hash);
    MazeTile rep2 = this.findRef(this.tile2, hash);
    hash.put(rep1, rep2);
  }

  // compares the weight of this edge to another
  // if this has a heigher weight, returns a positive number
  // if this has a lower weight, returns a negative number
  // otherwise returns 0
  int compareWeight(Edge other) {
    return this.weight - other.weight;
  }

}

// Represents a comparator that compares two edges based on weight
class BiggerWeight implements Comparator<Edge> {

  // compares the two given edges based on weight
  // if edge1 has a heigher weight, returns a positive number
  // if edge1 has a lower weight, returns a negative number
  // otherwise returns 0
  public int compare(Edge edge1, Edge edge2) {
    return edge1.compareWeight(edge2);
  }

}

class ExamplesMazeBuilder {

  MazeBuilder testBuilder;
  ArrayList<ArrayList<MazeTile>> testGrid;
  ArrayList<Edge> testEdges;
  HashMap<MazeTile, MazeTile> testHash;

  MazeTile testTile1;
  MazeTile testTile2;
  MazeTile testTile3;
  MazeTile testTile4;

  void init() {
    this.testBuilder = new MazeBuilder();

    this.testTile1 = new MazeTile(0, 0);
    this.testTile2 = new MazeTile(0, 1);
    this.testTile3 = new MazeTile(1, 0);
    this.testTile4 = new MazeTile(1, 1);

    this.testGrid = new ArrayList<ArrayList<MazeTile>>();

    ArrayList<MazeTile> column1 = new ArrayList<MazeTile>();
    column1.add(this.testTile1);
    column1.add(this.testTile2);

    ArrayList<MazeTile> column2 = new ArrayList<MazeTile>();
    column2.add(this.testTile3);
    column2.add(this.testTile4);

    this.testGrid.add(column1);
    this.testGrid.add(column2);

    this.testEdges = new ArrayList<Edge>();

    this.testHash = new HashMap<MazeTile, MazeTile>();
  }

  void testBuildGrid(Tester t) {
    this.init();

    ArrayList<ArrayList<MazeTile>> grid1 = this.testBuilder.buildGrid(1, 1);
    t.checkExpect(grid1.size(), 1);
    t.checkExpect(grid1.get(0).size(), 1);

    ArrayList<ArrayList<MazeTile>> grid2 = this.testBuilder.buildGrid(3, 5);
    t.checkExpect(grid2.size(), 3);
    t.checkExpect(grid2.get(0).size(), 5);
    t.checkExpect(grid2.get(1).size(), 5);
    t.checkExpect(grid2.get(2).size(), 5);

    ArrayList<ArrayList<MazeTile>> grid3 = this.testBuilder.buildGrid(5, 3);
    t.checkExpect(grid3.size(), 5);
    for (int i = 0; i < 5; i++) {
      t.checkExpect(grid3.get(i).size(), 3);
    }
  }

  void testBuildGridPositions(Tester t) {
    this.init();

    ArrayList<ArrayList<MazeTile>> grid = this.testBuilder.buildGrid(2, 2);

    t.checkExpect(grid.get(0).get(0).isAt(0, 0), true);
    t.checkExpect(grid.get(0).get(1).isAt(0, 1), true);
    t.checkExpect(grid.get(1).get(0).isAt(1, 0), true);
    t.checkExpect(grid.get(1).get(1).isAt(1, 1), true);

    t.checkExpect(grid.get(0).get(0).isAt(1, 1), false);
    t.checkExpect(grid.get(1).get(1).isAt(0, 0), false);
  }

  void testBuildGridZero(Tester t) {
    this.init();

    ArrayList<ArrayList<MazeTile>> grid1 = this.testBuilder.buildGrid(0, 5);
    t.checkExpect(grid1.size(), 0);

    ArrayList<ArrayList<MazeTile>> grid2 = this.testBuilder.buildGrid(5, 0);
    t.checkExpect(grid2.size(), 5);
    for (ArrayList<MazeTile> column : grid2) {
      t.checkExpect(column.size(), 0);
    }

    ArrayList<ArrayList<MazeTile>> grid3 = this.testBuilder.buildGrid(0, 0);
    t.checkExpect(grid3.size(), 0);
  }

  void testBuildEdges(Tester t) {
    this.init();

    ArrayList<Edge> edges1 = this.testBuilder.buildEdges(this.testGrid);
    t.checkExpect(edges1.size(), 4);

    ArrayList<ArrayList<MazeTile>> grid2 = this.testBuilder.buildGrid(3, 3);
    ArrayList<Edge> edges2 = this.testBuilder.buildEdges(grid2);
    t.checkExpect(edges2.size(), 12);

    ArrayList<ArrayList<MazeTile>> grid3 = this.testBuilder.buildGrid(1, 3);
    ArrayList<Edge> edges3 = this.testBuilder.buildEdges(grid3);
    t.checkExpect(edges3.size(), 2);

    ArrayList<ArrayList<MazeTile>> grid4 = this.testBuilder.buildGrid(3, 1);
    ArrayList<Edge> edges4 = this.testBuilder.buildEdges(grid4);
    t.checkExpect(edges4.size(), 2);
  }

  void testBuildEdgesEmpty(Tester t) {
    this.init();

    ArrayList<ArrayList<MazeTile>> emptyGrid = new ArrayList<ArrayList<MazeTile>>();
    ArrayList<Edge> edges = this.testBuilder.buildEdges(emptyGrid);
    t.checkExpect(edges.size(), 0);

    ArrayList<ArrayList<MazeTile>> emptyColumnGrid = new ArrayList<ArrayList<MazeTile>>();
    emptyColumnGrid.add(new ArrayList<MazeTile>());
    emptyColumnGrid.add(new ArrayList<MazeTile>());
    ArrayList<Edge> edges2 = this.testBuilder.buildEdges(emptyColumnGrid);
    t.checkExpect(edges2.size(), 0);
  }

  void testBuildEdgesSorted(Tester t) {
    this.init();

    ArrayList<ArrayList<MazeTile>> grid = this.testBuilder.buildGrid(2, 2);
    ArrayList<Edge> edges = this.testBuilder.buildEdges(grid);

    for (int i = 0; i < edges.size() - 1; i++) {
      Edge current = edges.get(i);
      Edge next = edges.get(i + 1);

      BiggerWeight comparator = new BiggerWeight();
      t.checkExpect(comparator.compare(current, next) <= 0, true);
    }
  }

  void testBuildInitialHash(Tester t) {
    this.init();

    HashMap<MazeTile, MazeTile> hash = this.testBuilder.buildInitialHash(this.testGrid);

    t.checkExpect(hash.containsKey(this.testTile1), true);
    t.checkExpect(hash.containsKey(this.testTile2), true);
    t.checkExpect(hash.containsKey(this.testTile3), true);
    t.checkExpect(hash.containsKey(this.testTile4), true);

    t.checkExpect(hash.get(this.testTile1), this.testTile1);
    t.checkExpect(hash.get(this.testTile2), this.testTile2);
    t.checkExpect(hash.get(this.testTile3), this.testTile3);
    t.checkExpect(hash.get(this.testTile4), this.testTile4);
  }

  void testBuildInitialHashLarge(Tester t) {
    this.init();

    ArrayList<ArrayList<MazeTile>> grid = this.testBuilder.buildGrid(3, 3);
    HashMap<MazeTile, MazeTile> hash = this.testBuilder.buildInitialHash(grid);

    t.checkExpect(hash.size(), 9);

    MazeTile topLeft = grid.get(0).get(0);
    MazeTile center = grid.get(1).get(1);
    MazeTile bottomRight = grid.get(2).get(2);

    t.checkExpect(hash.get(topLeft), topLeft);
    t.checkExpect(hash.get(center), center);
    t.checkExpect(hash.get(bottomRight), bottomRight);
  }

  void testBuildInitialHashEmpty(Tester t) {
    this.init();

    ArrayList<ArrayList<MazeTile>> emptyGrid = new ArrayList<ArrayList<MazeTile>>();
    HashMap<MazeTile, MazeTile> hash1 = this.testBuilder.buildInitialHash(emptyGrid);
    t.checkExpect(hash1.size(), 0);

    ArrayList<ArrayList<MazeTile>> emptyColumnGrid = new ArrayList<ArrayList<MazeTile>>();
    emptyColumnGrid.add(new ArrayList<MazeTile>());
    emptyColumnGrid.add(new ArrayList<MazeTile>());
    HashMap<MazeTile, MazeTile> hash2 = this.testBuilder.buildInitialHash(emptyColumnGrid);
    t.checkExpect(hash2.size(), 0);
  }

  void testBuildMaze(Tester t) {
    this.init();
    ArrayList<ArrayList<MazeTile>> maze1 = this.testBuilder.buildMaze(2, 3);
    t.checkExpect(maze1.size(), 2);
    t.checkExpect(maze1.get(0).size(), 3);
    t.checkExpect(maze1.get(1).size(), 3);

    ArrayList<ArrayList<MazeTile>> maze2 = this.testBuilder.buildMaze(4, 2);
    t.checkExpect(maze2.size(), 4);
    for (int i = 0; i < 4; i++) {
      t.checkExpect(maze2.get(i).size(), 2);
    }
  }

  void testBuildMazeConnectivity(Tester t) {
    this.init();

    ArrayList<ArrayList<MazeTile>> maze = this.testBuilder.buildMaze(2, 2);

    MazeTile topLeft = maze.get(0).get(0);
    MazeTile topRight = maze.get(1).get(0);
    MazeTile bottomLeft = maze.get(0).get(1);
    MazeTile bottomRight = maze.get(1).get(1);

    int connectionCount = 0;

    if (topLeft.isConnected(1, 0)) {
      connectionCount++;
    }
    if (topLeft.isConnected(0, 1)) {
      connectionCount++;
    }
    if (topRight.isConnected(1, 1)) {
      connectionCount++;
    }
    if (bottomLeft.isConnected(1, 1)) {
      connectionCount++;
    }
    t.checkExpect(connectionCount >= 3, true);

    boolean topLeftHasConnection = topLeft.isConnected(1, 0) || topLeft.isConnected(0, 1);
    boolean topRightHasConnection = topRight.isConnected(0, 0) || topRight.isConnected(1, 1);
    boolean bottomLeftHasConnection = bottomLeft.isConnected(0, 0) || bottomLeft.isConnected(1, 1);
    boolean bottomRightHasConnection = bottomRight.isConnected(1, 0)
        || bottomRight.isConnected(0, 1);

    t.checkExpect(topLeftHasConnection, true);
    t.checkExpect(topRightHasConnection, true);
    t.checkExpect(bottomLeftHasConnection, true);
    t.checkExpect(bottomRightHasConnection, true);
  }

}