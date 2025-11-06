import java.awt.Color;
import java.util.*;
import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;

// represents a maze
class Maze extends World {
  final int width;
  final int height;
  private int blockSize;
  private ArrayList<ArrayList<MazeTile>> grid;
  private Collection<MazeTile> path;
  private ArrayList<MazeTile> seen;
  private MazeTile currentPosition;
  private boolean searchInProgress;
  private IWorkList workList;
  private HashMap<MazeTile, MazeTile> cameFrom;
  private boolean searchComplete;
  private boolean showSeen;

  // either "none" "to start" or "to end"
  private String gradientMode;

  Maze(int width, int height) {
    this.blockSize = 20;
    this.width = width;
    this.height = height;
    this.grid = new MazeBuilder().buildMaze(width, height);
    this.currentPosition = this.grid.get(0).get(0);
    this.path = new ArrayList<MazeTile>();
    this.seen = new ArrayList<MazeTile>();
    this.searchInProgress = false;
    this.searchComplete = false;
    this.showSeen = true;
    this.gradientMode = "normal";
    this.path.add(this.currentPosition);
    this.seen.add(this.currentPosition);

  }

  Maze(int width, int height, int blockSize) {
    this.blockSize = blockSize;
    this.width = width;
    this.height = height;
    this.grid = new MazeBuilder().buildMaze(width, height);
    this.currentPosition = this.grid.get(0).get(0);
    this.path = new ArrayList<MazeTile>();
    this.seen = new ArrayList<MazeTile>();
    this.searchInProgress = false;
    this.searchComplete = false;
    this.showSeen = true;
    this.gradientMode = "normal";
    this.path.add(this.currentPosition);
    this.seen.add(this.currentPosition);
  }

  // Resets maze to state before search
  void clear() {
    this.path = new ArrayList<MazeTile>();
    this.seen = new ArrayList<MazeTile>();
    this.currentPosition = this.grid.get(0).get(0);
    this.searchInProgress = false;
    this.workList = null;
    this.cameFrom = null;
    this.searchComplete = false;
    this.gradientMode = "normal";
    this.path.add(this.currentPosition);
    this.seen.add(this.currentPosition);
  }

  // Generates a new maze
  void newMaze() {
    this.grid = new MazeBuilder().buildMaze(width, height);
    this.clear();
  }

  // draws the maze and paths based on the gradient mode
  public WorldScene makeScene() {

    WorldScene scene = new WorldScene(this.width * this.blockSize, this.height * this.blockSize);
    int maxToStart = this.maxDistanceFromStart();
    int maxToEnd = this.maxDistanceFromEnd();

    for (ArrayList<MazeTile> column : grid) {
      for (MazeTile tile : column) {
        Color color;

        // Priority: Start/End > Current Location > Path > Tries > Gradient Mode >
        // Default
        if (tile.isAt(0, 0)) {
          color = Color.GREEN;
        }
        else if (tile.isAt(width - 1, height - 1)) {
          color = new Color(150, 0, 255);
        }
        else if (tile.equals(this.currentPosition)) {
          color = Color.RED;
        }
        else if (this.path.contains(tile)) {
          color = Color.BLUE;
        }
        else if (this.seen.contains(tile) && this.showSeen) {
          color = Color.CYAN;
        }
        else if (this.gradientMode.equals("to start")) {
          color = tile.toStartColor(maxToStart);
        }
        else if (this.gradientMode.equals("to end")) {
          color = tile.toEndColor(maxToEnd);
        }
        else {
          color = Color.WHITE;
        }
        tile.draw(scene, this.blockSize, color);
      }
    }

    scene.placeImageXY(
        new TextImage("Wrong moves: " + (this.seen.size() - this.path.size()), Color.BLACK),
        width * this.blockSize - 60, this.blockSize / 2);

    return scene;
  }

  // returns the maximum distance from the end tile
  int maxDistanceFromEnd() {
    int max = 0;

    // iterates through 2d arraylist and
    // set max as the maximum of max and the tiles distance to end
    for (ArrayList<MazeTile> column : this.grid) {
      for (MazeTile tile : column) {
        max = tile.biggerFromEnd(max);
      }

    }
    return max;
  }

  // returns the maximum distance from the start tile
  int maxDistanceFromStart() {
    int max = 0;

    // iterates through 2d arraylist and
    // set max as the maximum of max and the tiles distance to start
    for (ArrayList<MazeTile> column : this.grid) {
      for (MazeTile tile : column) {
        max = tile.biggerFromStart(max);
      }

    }
    return max;
  }

  // responds to user input according to UserGuide.txt
  public void onKeyEvent(String key) {

    if (key.equals("d")) {
      this.clear();
      this.startSearch(new MazeStack());
    }
    else if (key.equals("b")) {
      this.clear();
      this.startSearch(new MazeQueue());
    }
    else if (key.equals("c")) {
      this.clear();
    }
    else if (key.equals("n")) {
      this.newMaze();
    }
    else if (key.equals("s")) {
      this.showSeen = !this.showSeen;
    }
    else if (key.equals("1")) {
      this.gradientMode = "to start";
    }
    else if (key.equals("9")) {
      this.gradientMode = "to end";
    }
    else if (key.equals("k")) {
      this.gradientMode = "normal";
    }
    else if (!this.searchComplete && !this.searchInProgress
        && (key.equals("right") || key.equals("left") || key.equals("down") || key.equals("up"))) {

      MazeTile nextPosition = this.currentPosition;

      int currentX = -1;
      int currentY = -1;

      // goes through the 2d arraylist and finds the x y coordinates of this tile
      for (int x = 0; x < this.grid.size(); x++) {
        for (int y = 0; y < this.grid.get(x).size(); y++) {
          if (this.grid.get(x).get(y) == this.currentPosition) {
            currentX = x;
            currentY = y;
            break;
          }
        }
        if (currentX != -1) {
          break;
        }
      }

      if (key.equals("up") && currentY > 0) {
        if (this.currentPosition.isConnected(currentX, currentY - 1)) {
          nextPosition = this.grid.get(currentX).get(currentY - 1);
        }
      }
      else if (key.equals("down") && currentY < this.height - 1) {
        if (this.currentPosition.isConnected(currentX, currentY + 1)) {
          nextPosition = this.grid.get(currentX).get(currentY + 1);
        }
      }
      else if (key.equals("left") && currentX > 0) {
        if (this.currentPosition.isConnected(currentX - 1, currentY)) {
          nextPosition = this.grid.get(currentX - 1).get(currentY);
        }
      }
      else if (key.equals("right") && currentX < this.width - 1) {
        if (this.currentPosition.isConnected(currentX + 1, currentY)) {
          nextPosition = this.grid.get(currentX + 1).get(currentY);
        }
      }

      if (!nextPosition.equals(this.currentPosition)) {
        if (path.contains(nextPosition)) {
          this.path.remove(this.currentPosition);
        }
        else {
          this.path.add(nextPosition);
        }
        if (!this.seen.contains(nextPosition)) {
          this.seen.add(nextPosition);
        }
        this.currentPosition = nextPosition;
      }
      if (this.currentPosition.isAt(this.width - 1, this.height - 1)) {
        this.searchComplete = true;
      }
    }

  }

  // performs a search step if needed
  public void onTick() {
    if (this.searchInProgress && !this.searchComplete) {
      this.performSearchStep();
    }
  }

  // given the current tile and a hash map representing where tiles came from,
  // returns a list of tiles in the path
  ArrayList<MazeTile> findPath(MazeTile tile, HashMap<MazeTile, MazeTile> cameFrom) {
    ArrayList<MazeTile> path = new ArrayList<MazeTile>();
    MazeTile current = tile;

    while (!current.isAt(0, 0)) {
      path.add(current);

      if (!cameFrom.containsKey(current)) {
        throw new IllegalArgumentException("Could not trace maze path");
      }

      current = cameFrom.get(current);
    }

    path.add(current);
    return path;
  }

  // iterates the next step of the serach
  void performSearchStep() {
    if (this.workList.isEmpty()) {
      this.searchComplete = true;
      return;
    }

    MazeTile tile = this.workList.remove();
    this.seen.add(tile);

    if (tile.isAt(this.width - 1, this.height - 1)) {
      this.path = this.findPath(tile, this.cameFrom);
      this.searchComplete = true;
      return;
    }
    tile.addUnseenConnectionsTo(this.workList, this.seen, this.cameFrom);
  }

  // begins the serach
  void startSearch(IWorkList workList) {
    if (this.grid.size() == 0 || this.grid.get(0).size() == 0) {
      throw new IllegalArgumentException("This maze has no tiles");
    }

    this.seen.clear();
    this.path.clear();
    this.searchInProgress = true;
    this.workList = workList;
    this.cameFrom = new HashMap<MazeTile, MazeTile>();
    this.searchComplete = false;
    this.workList.add(this.grid.get(0).get(0));
  }

  // starts the animation at the given frequency
  void start(double frequency) {
    this.bigBang(width * this.blockSize, height * this.blockSize, frequency);
  }
}

// Test class
class ExamplesMaze {

  Maze testMaze1;
  Maze testMaze2;
  Maze testMaze3;

  MazeBuilder testBuilder;
  ArrayList<ArrayList<MazeTile>> testGrid;

  MazeTile testTile1;
  MazeTile testTile2;
  MazeTile testTile3;

  Edge testEdge1;
  Edge testEdge2;
  Edge testEdge3;

  MazeStack testStack;
  MazeQueue testQueue;

  HashMap<MazeTile, MazeTile> testHash;
  ArrayList<Edge> testEdges;
  KruskalTreeBuilder testTreeBuilder;

  ArrayList<Edge> unsortedEdges;

  Maze maze = new Maze(3, 3);

  void init() {
    this.unsortedEdges = new ArrayList<Edge>();
    this.unsortedEdges.add(new Edge(null, null, 10));
    this.unsortedEdges.add(new Edge(null, null, 0));
    this.unsortedEdges.add(new Edge(null, null, 100));
    this.unsortedEdges.add(new Edge(null, null, 7));
    this.testMaze1 = new Maze(3, 3);
    this.testMaze2 = new Maze(5, 5);
    this.testMaze3 = new Maze(10, 8, 15);
    this.testBuilder = new MazeBuilder();

    this.testTile1 = new MazeTile(0, 0);
    this.testTile2 = new MazeTile(1, 1);
    this.testTile3 = new MazeTile(2, 2);

    this.testTile1.addConnection(this.testTile2);
    this.testTile2.addConnection(this.testTile1);
    this.testTile2.addConnection(this.testTile3);
    this.testTile3.addConnection(this.testTile2);

    this.testEdge1 = new Edge(this.testTile1, this.testTile2, 10);
    this.testEdge2 = new Edge(this.testTile2, this.testTile3, 5);
    this.testEdge3 = new Edge(this.testTile1, this.testTile3, 15);

    this.testStack = new MazeStack();
    this.testQueue = new MazeQueue();

    this.testGrid = this.testBuilder.buildGrid(3, 3);

    this.testHash = new HashMap<MazeTile, MazeTile>();
    this.testEdges = new ArrayList<Edge>();

    this.testHash.put(this.testTile1, this.testTile1);
    this.testHash.put(this.testTile2, this.testTile2);
    this.testHash.put(this.testTile3, this.testTile3);

    this.testEdges.add(this.testEdge1);
    this.testEdges.add(this.testEdge2);
    this.testEdges.add(this.testEdge3);

    this.testTreeBuilder = new KruskalTreeBuilder(this.testHash, this.testEdges);
  }

  void testEdgeSort(Tester t) {
    this.init();
    this.unsortedEdges.sort(new BiggerWeight());

    t.checkExpect(this.unsortedEdges, new ArrayList<Edge>(Arrays.asList(new Edge(null, null, 0),
        new Edge(null, null, 7), new Edge(null, null, 10), new Edge(null, null, 100))));
  }

  void testBigbang(Tester t) {
    Maze world = new Maze(60, 40);
    world.start(0.01);
  }

  void testMazeScene(Tester t) {
    this.init();
    WorldScene scene = this.testMaze1.makeScene();
    t.checkExpect(scene != null, true);
    t.checkExpect(scene.width, 3 * 20);
    t.checkExpect(scene.height, 3 * 20);
  }

  void testBuildGrid(Tester t) {
    this.init();

    ArrayList<ArrayList<MazeTile>> grid = this.testBuilder.buildGrid(4, 5);

    t.checkExpect(grid.size(), 4);
    t.checkExpect(grid.get(0).size(), 5);
    t.checkExpect(grid.get(1).size(), 5);
    t.checkExpect(grid.get(2).size(), 5);
    t.checkExpect(grid.get(3).size(), 5);
  }

  void testBuildEdges(Tester t) {
    this.init();

    ArrayList<ArrayList<MazeTile>> grid = this.testBuilder.buildGrid(2, 2);

    ArrayList<Edge> edges = this.testBuilder.buildEdges(grid);

    t.checkExpect(edges.size(), 4);

    grid = this.testBuilder.buildGrid(3, 4);
    edges = this.testBuilder.buildEdges(grid);

    t.checkExpect(edges.size(), 17);
  }

  void testBuildMaze(Tester t) {
    this.init();

    ArrayList<ArrayList<MazeTile>> maze = this.testBuilder.buildMaze(3, 3);

    t.checkExpect(maze.size(), 3);
    for (ArrayList<MazeTile> column : maze) {
      t.checkExpect(column.size(), 3);
    }

    MazeTile start = maze.get(0).get(0);
    MazeTile end = maze.get(2).get(2);

    t.checkExpect(start.isConnected(0, 0), false);
    t.checkExpect(end.isConnected(2, 2), false);
  }

  void testMazeTileIsAt(Tester t) {
    this.init();

    t.checkExpect(this.testTile1.isAt(0, 0), true);
    t.checkExpect(this.testTile1.isAt(0, 1), false);
    t.checkExpect(this.testTile1.isAt(1, 0), false);

    t.checkExpect(this.testTile2.isAt(1, 1), true);
    t.checkExpect(this.testTile2.isAt(0, 1), false);
    t.checkExpect(this.testTile2.isAt(1, 0), false);
  }

  void testMazeTileIsConnected(Tester t) {
    this.init();

    t.checkExpect(this.testTile1.isConnected(1, 1), true);

    t.checkExpect(this.testTile1.isConnected(2, 2), false);

    t.checkExpect(this.testTile2.isConnected(0, 0), true);
    t.checkExpect(this.testTile2.isConnected(2, 2), true);
  }

  void testEdgeConnect(Tester t) {
    this.init();

    MazeTile tileA = new MazeTile(3, 3);
    MazeTile tileB = new MazeTile(3, 4);

    t.checkExpect(tileA.isConnected(3, 4), false);
    t.checkExpect(tileB.isConnected(3, 3), false);

    Edge edge = new Edge(tileA, tileB, 5);
    edge.connect();

    t.checkExpect(tileA.isConnected(3, 4), true);
    t.checkExpect(tileB.isConnected(3, 3), true);
  }

  void testEdgeIsConnected(Tester t) {
    this.init();

    HashMap<MazeTile, MazeTile> testMap = new HashMap<>();
    testMap.put(this.testTile1, this.testTile1);
    testMap.put(this.testTile2, this.testTile2);
    testMap.put(this.testTile3, this.testTile3);

    t.checkExpect(this.testEdge1.isConnected(testMap), false);
    t.checkExpect(this.testEdge2.isConnected(testMap), false);

    testMap.put(this.testTile1, this.testTile2);

    t.checkExpect(this.testEdge1.isConnected(testMap), true);
    t.checkExpect(this.testEdge2.isConnected(testMap), false);
  }

  void testEdgeJoin(Tester t) {
    this.init();

    HashMap<MazeTile, MazeTile> testMap = new HashMap<>();
    testMap.put(this.testTile1, this.testTile1);
    testMap.put(this.testTile2, this.testTile2);
    testMap.put(this.testTile3, this.testTile3);

    t.checkExpect(this.testEdge1.isConnected(testMap), false);

    this.testEdge1.join(testMap);

    t.checkExpect(this.testEdge1.isConnected(testMap), true);
  }

  void testMazeStackAddRemove(Tester t) {
    this.init();

    MazeStack stack = new MazeStack();

    t.checkExpect(stack.isEmpty(), true);
    t.checkExpect(stack.size(), 0);

    stack.add(this.testTile1);
    t.checkExpect(stack.isEmpty(), false);
    t.checkExpect(stack.size(), 1);

    stack.add(this.testTile2);
    t.checkExpect(stack.size(), 2);

    t.checkExpect(stack.remove(), this.testTile2);
    t.checkExpect(stack.size(), 1);

    t.checkExpect(stack.remove(), this.testTile1);
    t.checkExpect(stack.isEmpty(), true);
  }

  void testMazeStackIsEmpty(Tester t) {
    this.init();

    MazeStack stack = new MazeStack();

    t.checkExpect(stack.isEmpty(), true);

    stack.add(this.testTile1);
    t.checkExpect(stack.isEmpty(), false);

    stack.remove();
    t.checkExpect(stack.isEmpty(), true);
  }

  void testMazeStackSize(Tester t) {
    this.init();

    MazeStack stack = new MazeStack();

    t.checkExpect(stack.size(), 0);

    stack.add(this.testTile1);
    t.checkExpect(stack.size(), 1);

    stack.add(this.testTile2);
    t.checkExpect(stack.size(), 2);

    stack.add(this.testTile3);
    t.checkExpect(stack.size(), 3);

    stack.remove();
    t.checkExpect(stack.size(), 2);

    stack.remove();
    t.checkExpect(stack.size(), 1);

    stack.remove();
    t.checkExpect(stack.size(), 0);
  }

  void testMazeQueueAddRemove(Tester t) {
    this.init();

    MazeQueue queue = new MazeQueue();

    t.checkExpect(queue.isEmpty(), true);
    t.checkExpect(queue.size(), 0);

    queue.add(this.testTile1);
    t.checkExpect(queue.isEmpty(), false);
    t.checkExpect(queue.size(), 1);

    queue.add(this.testTile2);
    t.checkExpect(queue.size(), 2);

    t.checkExpect(queue.remove(), this.testTile1);
    t.checkExpect(queue.size(), 1);

    t.checkExpect(queue.remove(), this.testTile2);
    t.checkExpect(queue.isEmpty(), true);
  }

  void testMazeQueueIsEmpty(Tester t) {
    this.init();

    MazeQueue queue = new MazeQueue();

    t.checkExpect(queue.isEmpty(), true);

    queue.add(this.testTile1);
    t.checkExpect(queue.isEmpty(), false);

    queue.remove();
    t.checkExpect(queue.isEmpty(), true);
  }

  void testMazeQueueSize(Tester t) {
    this.init();

    // Create a new queue
    MazeQueue queue = new MazeQueue();

    t.checkExpect(queue.size(), 0);

    queue.add(this.testTile1);
    t.checkExpect(queue.size(), 1);

    queue.add(this.testTile2);
    t.checkExpect(queue.size(), 2);

    queue.add(this.testTile3);
    t.checkExpect(queue.size(), 3);

    queue.remove();
    t.checkExpect(queue.size(), 2);

    queue.remove();
    t.checkExpect(queue.size(), 1);

    queue.remove();
    t.checkExpect(queue.size(), 0);
  }

  void testCreateTree(Tester t) {
    this.init();

    ArrayList<Edge> result = this.testTreeBuilder.createTree();

    t.checkExpect(result.size(), 2);
  }

  void testCreateTreeComplex(Tester t) {
    this.init();

    MazeTile tileA = new MazeTile(3, 3);
    MazeTile tileB = new MazeTile(3, 4);
    MazeTile tileC = new MazeTile(4, 3);
    MazeTile tileD = new MazeTile(4, 4);

    Edge edgeAB = new Edge(tileA, tileB, 10);
    Edge edgeAC = new Edge(tileA, tileC, 5);
    Edge edgeBD = new Edge(tileB, tileD, 8);
    Edge edgeCD = new Edge(tileC, tileD, 12);
    Edge edgeBC = new Edge(tileB, tileC, 15);

    HashMap<MazeTile, MazeTile> map = new HashMap<>();
    map.put(tileA, tileA);
    map.put(tileB, tileB);
    map.put(tileC, tileC);
    map.put(tileD, tileD);

    ArrayList<Edge> edges = new ArrayList<>();
    edges.add(edgeAC);
    edges.add(edgeBD);
    edges.add(edgeAB);
    edges.add(edgeCD);
    edges.add(edgeBC);

    KruskalTreeBuilder builder = new KruskalTreeBuilder(map, edges);

    ArrayList<Edge> result = builder.createTree();

    t.checkExpect(result.size(), 3);

    t.checkExpect(result.contains(edgeAC), true);
    t.checkExpect(result.contains(edgeBD), true);
    t.checkExpect(result.contains(edgeAB) || result.contains(edgeCD), true);
  }

  void testCreateTreeEmpty(Tester t) {
    this.init();

    ArrayList<Edge> edges = new ArrayList<>();

    HashMap<MazeTile, MazeTile> map = new HashMap<>();
    map.put(this.testTile1, this.testTile1);

    KruskalTreeBuilder builder = new KruskalTreeBuilder(map, edges);

    ArrayList<Edge> result = builder.createTree();

    t.checkExpect(result.size(), 0);
  }

  void testBiggerWeightCompare(Tester t) {
    this.init();

    BiggerWeight comparator = new BiggerWeight();

    Edge edge1 = new Edge(this.testTile1, this.testTile2, 5);
    Edge edge2 = new Edge(this.testTile2, this.testTile3, 10);

    t.checkExpect(comparator.compare(edge1, edge2) < 0, true);
    t.checkExpect(comparator.compare(edge2, edge1) > 0, true);
  }

  void testBiggerWeightCompareSame(Tester t) {
    this.init();

    BiggerWeight comparator = new BiggerWeight();

    Edge edge1 = new Edge(this.testTile1, this.testTile2, 7);
    Edge edge2 = new Edge(this.testTile2, this.testTile3, 7);

    t.checkExpect(comparator.compare(edge1, edge2), 0);
    t.checkExpect(comparator.compare(edge2, edge1), 0);
  }

  void testBiggerWeightSort(Tester t) {
    this.init();

    ArrayList<Edge> edges = new ArrayList<>();
    edges.add(new Edge(this.testTile1, this.testTile2, 15));
    edges.add(new Edge(this.testTile2, this.testTile3, 5));
    edges.add(new Edge(this.testTile1, this.testTile3, 10));

    edges.sort(new BiggerWeight());

    t.checkExpect(edges.get(0).compareWeight(new Edge(null, null, 5)), 0);
    t.checkExpect(edges.get(1).compareWeight(new Edge(null, null, 10)), 0);
    t.checkExpect(edges.get(2).compareWeight(new Edge(null, null, 15)), 0);
  }
}