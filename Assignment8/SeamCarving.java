import java.util.ArrayList;
import java.util.HashMap;
import tester.*;
import javalib.impworld.*;
import javalib.worldimages.*;
import java.awt.Color;

// Represents operations that can be performed on a pixel in an image
interface IPixel {
  // Calculates the energy value of this pixel
  double getEnergy();

  // Returns the brightness value of this pixel
  int getBrightness();

  // Returns the color of this pixel
  Color getColor();

  // Returns the pixel at the left
  IPixel getLeft();

  // Returns the pixel at the right
  IPixel getRight();

  // Returns the pixel above this one
  IPixel getTop();

  // Returns the pixel below this one
  IPixel getBottom();

  // Returns the pixel at the top-left
  IPixel getTopLeft();

  // Returns the pixel at the top-right
  IPixel getTopRight();

  // Returns the pixel at the bottom-left
  IPixel getBottomLeft();

  // Returns the pixel at the bottom-right
  IPixel getBottomRight();

  // Sets the pixel at the left
  void setLeft(IPixel p);

  // Sets the pixel at the right
  void setRight(IPixel p);

  // Sets the pixel above
  void setTop(IPixel p);

  // Sets the pixel below
  void setBottom(IPixel p);

  // Sets the pixel at the top-left
  void setTopLeft(IPixel p);

  // Sets the pixel at the top-right
  void setTopRight(IPixel p);

  // Sets the pixel at the bottom-left
  void setBottomLeft(IPixel p);

  // Sets the pixel at the bottom-right
  void setBottomRight(IPixel p);
}

// Represents a standard pixel in an image with connections to surrounding pixels
class Pixel implements IPixel {
  // The color of this pixel
  Color color;
  // The pixel at the left
  IPixel left;
  // The pixel at the right
  IPixel right;
  // The pixel above this one
  IPixel top;
  // The pixel below this one
  IPixel bottom;
  // The pixel at the top-left
  IPixel topLeft;
  // The pixel at the top-right
  IPixel topRight;
  // The pixel at the bottom-left
  IPixel bottomLeft;
  // The pixel at the bottom-right
  IPixel bottomRight;

  // Constructor: creates a new pixel with the given color
  Pixel(Color color) {
    this.color = color;
  }

  // Calculates the energy of this pixel
  public double getEnergy() {
    return Math.sqrt(Math.pow(getHorizEnergy(), 2) + Math.pow(getVertEnergy(), 2));
  }

  // Calculates the horizontal energy gradient
  double getHorizEnergy() {
    return (getTopLeft().getBrightness() + 2 * getLeft().getBrightness()
        + getBottomLeft().getBrightness())
        - (getTopRight().getBrightness() + 2 * getRight().getBrightness()
            + getBottomRight().getBrightness());
  }

  // Calculates the vertical energy gradient
  double getVertEnergy() {
    return (getTopLeft().getBrightness() + 2 * getTop().getBrightness()
        + getTopRight().getBrightness())
        - (getBottomLeft().getBrightness() + 2 * getBottom().getBrightness()
            + getBottomRight().getBrightness());
  }

  // Returns the brightness of this pixel (average of RGB values)
  public int getBrightness() {
    return (this.color.getRed() + this.color.getGreen() + this.color.getBlue()) / 3;
  }

  // Returns the color of this pixel
  public Color getColor() {
    return this.color;
  }

  // Returns the pixel at the left
  public IPixel getLeft() {
    return this.left;
  }

  // Returns the pixel at the right
  public IPixel getRight() {
    return this.right;
  }

  // Returns the pixel above this one
  public IPixel getTop() {
    return this.top;
  }

  // Returns the pixel below this one
  public IPixel getBottom() {
    return this.bottom;
  }

  // Returns the pixel at the top-left
  public IPixel getTopLeft() {
    return this.topLeft;
  }

  // Returns the pixel at the top-right
  public IPixel getTopRight() {
    return this.topRight;
  }

  // Returns the pixel at the bottom-left
  public IPixel getBottomLeft() {
    return this.bottomLeft;
  }

  // Returns the pixel at the bottom-right
  public IPixel getBottomRight() {
    return this.bottomRight;
  }

  // Sets the pixel at the left
  public void setLeft(IPixel p) {
    this.left = p;
  }

  // Sets the pixel at the right
  public void setRight(IPixel p) {
    this.right = p;
  }

  // Sets the pixel above this one
  public void setTop(IPixel p) {
    this.top = p;
  }

  // Sets the pixel below this one
  public void setBottom(IPixel p) {
    this.bottom = p;
  }

  // Sets the pixel at the top-left
  public void setTopLeft(IPixel p) {
    this.topLeft = p;
  }

  // Sets the pixel at the top-right
  public void setTopRight(IPixel p) {
    this.topRight = p;
  }

  // Sets the pixel at the bottom-left
  public void setBottomLeft(IPixel p) {
    this.bottomLeft = p;
  }

  // Sets the pixel at the bottom-right
  public void setBottomRight(IPixel p) {
    this.bottomRight = p;
  }
}

// Represents a border pixel that surrounds the image
class BorderPixel implements IPixel {
  Color color;

  // creates a new border pixel (black by default)
  BorderPixel() {
    this.color = Color.BLACK;
  }

  // Border pixels have maximum energy to avoid being selected for seams
  public double getEnergy() {
    return Double.MAX_VALUE;
  }

  // Border pixels have zero brightness
  public int getBrightness() {
    return 0;
  }

  // Returns the color of this border pixel
  public Color getColor() {
    return this.color;
  }

  // Border pixels reference themselves for all neighbor queries
  public IPixel getLeft() {
    return this;
  }

  // Border pixels reference themselves for all neighbor queries
  public IPixel getRight() {
    return this;
  }

  // Border pixels reference themselves for all neighbor queries
  public IPixel getTop() {
    return this;
  }

  // Border pixels reference themselves for all neighbor queries
  public IPixel getBottom() {
    return this;
  }

  // Border pixels reference themselves for all neighbor queries
  public IPixel getTopLeft() {
    return this;
  }

  // Border pixels reference themselves for all neighbor queries
  public IPixel getTopRight() {
    return this;
  }

  // Border pixels reference themselves for all neighbor queries
  public IPixel getBottomLeft() {
    return this;
  }

  // Border pixels reference themselves for all neighbor queries
  public IPixel getBottomRight() {
    return this;
  }

  // Border pixels ignore all setter methods
  public void setLeft(IPixel p) {
    // Do nothing for border pixels
  }

  // Border pixels ignore all setter methods
  public void setRight(IPixel p) {
    // Do nothing for border pixels
  }

  // Border pixels ignore all setter methods
  public void setTop(IPixel p) {
    // Do nothing for border pixels
  }

  // Border pixels ignore all setter methods
  public void setBottom(IPixel p) {
    // Do nothing for border pixels
  }

  // Border pixels ignore all setter methods
  public void setTopLeft(IPixel p) {
    // Do nothing for border pixels
  }

  // Border pixels ignore all setter methods
  public void setTopRight(IPixel p) {
    // Do nothing for border pixels
  }

  // Border pixels ignore all setter methods
  public void setBottomLeft(IPixel p) {
    // Do nothing for border pixels
  }

  // Border pixels ignore all setter methods
  public void setBottomRight(IPixel p) {
    // Do nothing for border pixels
  }
}

// Represents information about a pixel in a seam
class SeamInfo {
  // Total energy of the seam up to this pixel
  double totalWeight;
  // Previous pixel in the seam
  SeamInfo cameFrom;
  // X-coordinate of this pixel
  int x;
  // Y-coordinate of this pixel
  int y;

  // Constructor: creates a new seam info with the given parameters
  SeamInfo(double totalWeight, SeamInfo cameFrom, int x, int y) {
    this.totalWeight = totalWeight;
    this.cameFrom = cameFrom;
    this.x = x;
    this.y = y;
  }
}

// Represents the seam carving algorithm and display
class SeamCarver extends World {
  // grid of the pixels in the image
  ArrayList<ArrayList<IPixel>> pixelGrid;
  // Current width of the image
  int width;
  // Height of the image
  int height;
  // memoization of energies
  HashMap<String, Double> energies;
  // memoization of seamCosts
  HashMap<String, Double> seamCosts;

  // Constructor: creates a new seam carver from an image file
  SeamCarver(String fileName) {
    FromFileImage image = new FromFileImage(fileName);
    this.width = (int) image.getWidth();
    this.height = (int) image.getHeight();
    this.pixelGrid = new ArrayList<ArrayList<IPixel>>();
    this.energies = new HashMap<String, Double>();
    this.seamCosts = new HashMap<String, Double>();

    initRows(0, image);

    BorderPixel border = new BorderPixel();
    connectAllRows(0, border);
  }

  // Recursively initialize all rows of pixels
  void initRows(int y, FromFileImage image) {
    if (y >= height) {
      return;
    }

    ArrayList<IPixel> row = new ArrayList<IPixel>();
    initRow(0, y, row, image);
    pixelGrid.add(row);

    initRows(y + 1, image);
  }

  // Initialize pixels in a single row
  void initRow(int x, int y, ArrayList<IPixel> row, FromFileImage image) {
    if (x >= width) {
      return;
    }

    row.add(new Pixel(image.getColorAt(x, y)));
    initRow(x + 1, y, row, image);
  }

  // Connect all rows of pixels
  void connectAllRows(int y, BorderPixel border) {
    if (y >= height) {
      return;
    }

    connectRowPixels(0, y, border);
    connectAllRows(y + 1, border);
  }

  // Connect pixels in a single row
  void connectRowPixels(int x, int y, BorderPixel border) {
    if (x >= width) {
      return;
    }

    connectPixel(x, y, border);
    connectRowPixels(x + 1, y, border);
  }

  // Connect a pixel to its surrounding pixels
  void connectPixel(int x, int y, BorderPixel border) {
    IPixel pixel = getPixelAt(y, x);

    if (x > 0) {
      pixel.setLeft(getPixelAt(y, x - 1));
    }
    else {
      pixel.setLeft(border);
    }

    if (x < width - 1) {
      pixel.setRight(getPixelAt(y, x + 1));
    }
    else {
      pixel.setRight(border);
    }

    if (y > 0) {
      pixel.setTop(getPixelAt(y - 1, x));
    }
    else {
      pixel.setTop(border);
    }

    if (y < height - 1) {
      pixel.setBottom(getPixelAt(y + 1, x));
    }
    else {
      pixel.setBottom(border);
    }

    if (x > 0 && y > 0) {
      pixel.setTopLeft(getPixelAt(y - 1, x - 1));
    }
    else {
      pixel.setTopLeft(border);
    }

    if (x < width - 1 && y > 0) {
      pixel.setTopRight(getPixelAt(y - 1, x + 1));
    }
    else {
      pixel.setTopRight(border);
    }

    if (x > 0 && y < height - 1) {
      pixel.setBottomLeft(getPixelAt(y + 1, x - 1));
    }
    else {
      pixel.setBottomLeft(border);
    }

    if (x < width - 1 && y < height - 1) {
      pixel.setBottomRight(getPixelAt(y + 1, x + 1));
    }
    else {
      pixel.setBottomRight(border);
    }
  }

  // Get a pixel at the given coordinates
  IPixel getPixelAt(int y, int x) {
    if (y < 0 || y >= pixelGrid.size() || x < 0 || pixelGrid.get(y).size() <= x) {
      return new BorderPixel();
    }
    return pixelGrid.get(y).get(x);
  }

  // Get pixel energy
  double getEnergy(int y, int x) {
    String key = y + "," + x;
    if (!energies.containsKey(key)) {
      energies.put(key, getPixelAt(y, x).getEnergy());
    }
    return energies.get(key);
  }

  // Get minimum cost to reach a pixel from the top row
  double getSeamCost(int y, int x) {
    // Check bounds
    if (y < 0 || x < 0 || y >= height || x >= width) {
      return Double.MAX_VALUE;
    }

    String key = y + "," + x;
    if (seamCosts.containsKey(key)) {
      return seamCosts.get(key);
    }

    double cost;
    if (y == 0) {
      cost = getEnergy(y, x);
    }
    else {
      double leftCost = getSeamCost(y - 1, x - 1);
      double centerCost = getSeamCost(y - 1, x);
      double rightCost = getSeamCost(y - 1, x + 1);

      double minCost = centerCost;
      if (leftCost < minCost) {
        minCost = leftCost;
      }
      if (rightCost < minCost) {
        minCost = rightCost;
      }

      cost = minCost + getEnergy(y, x);
    }

    seamCosts.put(key, cost);
    return cost;
  }

  // Find the column in the bottom row with minimum energy
  int findMinBottomCol() {
    return findMinBottomColHelper(0, 1, Double.MAX_VALUE, 0);
  }

  // Find the column in the bottom row with minimum energy
  int findMinBottomColHelper(int x, int nextX, double minCost, int minX) {
    if (nextX > width) {
      return minX;
    }

    double cost = getSeamCost(height - 1, x);

    if (cost < minCost) {
      return findMinBottomColHelper(nextX, nextX + 1, cost, x);
    }
    else {
      return findMinBottomColHelper(nextX, nextX + 1, minCost, minX);
    }
  }

  // Trace the path from bottom to top
  ArrayList<Integer> traceSeam(int bottomX) {
    ArrayList<Integer> seam = new ArrayList<Integer>();
    traceSeamHelper(height - 1, bottomX, seam);
    return seam;
  }

  // Trace the path from bottom to top
  void traceSeamHelper(int y, int x, ArrayList<Integer> seam) {
    seam.add(0, x);

    if (y <= 0) {
      return;
    }

    double leftCost = x > 0 ? getSeamCost(y - 1, x - 1) : Double.MAX_VALUE;
    double centerCost = getSeamCost(y - 1, x);
    double rightCost = x < width - 1 ? getSeamCost(y - 1, x + 1) : Double.MAX_VALUE;

    int nextX = x;
    if (leftCost < centerCost && leftCost < rightCost) {
      nextX = x - 1;
    }
    else if (rightCost < centerCost && rightCost < leftCost) {
      nextX = x + 1;
    }

    traceSeamHelper(y - 1, nextX, seam);
  }

  // Remove the given seam from the image
  void removeSeam(ArrayList<Integer> seam) {
    ArrayList<ArrayList<IPixel>> newPixelRows = new ArrayList<ArrayList<IPixel>>();
    removeSeamRows(0, seam, newPixelRows);
    pixelGrid = newPixelRows;
    width--;

    BorderPixel border = new BorderPixel();
    reconnectAffectedRows(0, seam, border);

    energies.clear();
    seamCosts.clear();
  }

  // remove seam from all rows
  void removeSeamRows(int y, ArrayList<Integer> seam, ArrayList<ArrayList<IPixel>> newRows) {
    if (y >= height) {
      return;
    }

    ArrayList<IPixel> newRow = new ArrayList<IPixel>();
    removeSeamFromRow(0, y, seam.get(y), pixelGrid.get(y), newRow);
    newRows.add(newRow);

    removeSeamRows(y + 1, seam, newRows);
  }

  // build a new row without the pixel at seamX
  void removeSeamFromRow(int x, int y, int seamX, ArrayList<IPixel> oldRow,
      ArrayList<IPixel> newRow) {
    if (x >= oldRow.size()) {
      return;
    }

    if (x != seamX) {
      newRow.add(oldRow.get(x));
    }

    removeSeamFromRow(x + 1, y, seamX, oldRow, newRow);
  }

  // Reconnect pixels
  void reconnectAffectedRows(int y, ArrayList<Integer> seam, BorderPixel border) {
    if (y >= height) {
      return;
    }

    int seamX = seam.get(y);
    reconnectAffectedInRow(Math.max(0, seamX - 1), Math.min(width - 1, seamX), y, border);

    reconnectAffectedRows(y + 1, seam, border);
  }

  // Reconnect pixels in a row
  void reconnectAffectedInRow(int startX, int endX, int y, BorderPixel border) {
    if (startX > endX) {
      return;
    }

    connectPixel(startX, y, border);
    reconnectAffectedInRow(startX + 1, endX, y, border);
  }

  public void onTick() {
    if (width <= 1) {
      return;
    }

    int minCol = findMinBottomCol();
    ArrayList<Integer> seam = traceSeam(minCol);

    removeSeam(seam);
  }

  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(this.width, this.height);
    ComputedPixelImage image = new ComputedPixelImage(this.width, this.height);

    drawRows(0, image);

    scene.placeImageXY(image, this.width / 2, this.height / 2);
    return scene;
  }

  // draw all rows
  void drawRows(int y, ComputedPixelImage image) {
    if (y >= this.height) {
      return;
    }

    drawRowPixels(0, y, image);
    drawRows(y + 1, image);
  }

  // draw pixels in a row
  void drawRowPixels(int x, int y, ComputedPixelImage image) {
    if (x >= this.width) {
      return;
    }

    image.setPixel(x, y, getPixelAt(y, x).getColor());
    drawRowPixels(x + 1, y, image);
  }
}

class ExamplesSeamCarving {
  
  void testBigbang(Tester t) {
    SeamCarver world = new SeamCarver("src/Balloons.jpg");
    world.bigBang(world.width, world.height, 1);
  }

  void testSeamCarverConstructor(Tester t) {
    SeamCarver sc = new SeamCarver("src/Balloons.jpg");
    t.checkExpect(sc.width > 0, true);
    t.checkExpect(sc.height > 0, true);
    t.checkExpect(sc.pixelGrid.size(), sc.height);
    t.checkExpect(sc.pixelGrid.get(0).size(), sc.width);
  }

  void testPixelColorAndBrightness(Tester t) {
    Pixel redPixel = new Pixel(Color.RED);
    Pixel greenPixel = new Pixel(Color.GREEN);
    Pixel bluePixel = new Pixel(Color.BLUE);

    t.checkExpect(redPixel.getColor(), Color.RED);
    t.checkExpect(greenPixel.getColor(), Color.GREEN);
    t.checkExpect(bluePixel.getColor(), Color.BLUE);

    t.checkExpect(redPixel.getBrightness(), (255 + 0 + 0) / 3);
    t.checkExpect(greenPixel.getBrightness(), (0 + 255 + 0) / 3);
    t.checkExpect(bluePixel.getBrightness(), (0 + 0 + 255) / 3);
  }

  void testPixelConnections(Tester t) {
    Pixel centerPixel = new Pixel(Color.RED);
    Pixel leftPixel = new Pixel(Color.GREEN);
    Pixel rightPixel = new Pixel(Color.BLUE);
    Pixel topPixel = new Pixel(Color.YELLOW);
    Pixel bottomPixel = new Pixel(Color.CYAN);

    centerPixel.setLeft(leftPixel);
    centerPixel.setRight(rightPixel);
    centerPixel.setTop(topPixel);
    centerPixel.setBottom(bottomPixel);

    t.checkExpect(centerPixel.getLeft(), leftPixel);
    t.checkExpect(centerPixel.getRight(), rightPixel);
    t.checkExpect(centerPixel.getTop(), topPixel);
    t.checkExpect(centerPixel.getBottom(), bottomPixel);
  }

  void testBorderPixel(Tester t) {
    BorderPixel border = new BorderPixel();

    t.checkExpect(border.getColor(), Color.BLACK);
    t.checkExpect(border.getBrightness(), 0);
    t.checkExpect(border.getEnergy(), Double.MAX_VALUE);

    t.checkExpect(border.getLeft(), border);
    t.checkExpect(border.getRight(), border);
    t.checkExpect(border.getTop(), border);
    t.checkExpect(border.getBottom(), border);
    t.checkExpect(border.getTopLeft(), border);
    t.checkExpect(border.getTopRight(), border);
    t.checkExpect(border.getBottomLeft(), border);
    t.checkExpect(border.getBottomRight(), border);

    Pixel pixel = new Pixel(Color.RED);
    border.setLeft(pixel);
    t.checkExpect(border.getLeft(), border);
  }

  void testSeamInfo(Tester t) {
    SeamInfo info1 = new SeamInfo(10.0, null, 5, 0);
    SeamInfo info2 = new SeamInfo(15.0, info1, 6, 1);

    t.checkExpect(info1.totalWeight, 10.0);
    t.checkExpect(info1.cameFrom, null);
    t.checkExpect(info1.x, 5);
    t.checkExpect(info1.y, 0);

    t.checkExpect(info2.totalWeight, 15.0);
    t.checkExpect(info2.cameFrom, info1);
    t.checkExpect(info2.x, 6);
    t.checkExpect(info2.y, 1);
  }

  void testGetPixelAt(Tester t) {
    SeamCarver sc = createTestSeamCarver();

    t.checkExpect(sc.getPixelAt(0, 0).getColor(), Color.RED);
    t.checkExpect(sc.getPixelAt(0, 1).getColor(), Color.GREEN);
    t.checkExpect(sc.getPixelAt(1, 0).getColor(), Color.BLUE);
    t.checkExpect(sc.getPixelAt(1, 1).getColor(), Color.YELLOW);

    t.checkExpect(sc.getPixelAt(-1, 0) instanceof BorderPixel, true);
    t.checkExpect(sc.getPixelAt(0, -1) instanceof BorderPixel, true);
    t.checkExpect(sc.getPixelAt(2, 0) instanceof BorderPixel, true);
    t.checkExpect(sc.getPixelAt(0, 2) instanceof BorderPixel, true);
  }

  void testGetEnergy(Tester t) {
    SeamCarver sc = createTestSeamCarver();

    double energy = sc.getEnergy(0, 0);
    t.checkExpect(sc.energies.containsKey("0,0"), true);

    double cachedEnergy = sc.getEnergy(0, 0);
    t.checkExpect(energy, cachedEnergy);
  }

  void testGetSeamCost(Tester t) {
    SeamCarver sc = createTestSeamCarver();

    double cost00 = sc.getSeamCost(0, 0);
    double cost01 = sc.getSeamCost(0, 1);

    t.checkExpect(cost00, sc.getEnergy(0, 0));
    t.checkExpect(cost01, sc.getEnergy(0, 1));

    t.checkExpect(sc.seamCosts.containsKey("0,0"), true);
    t.checkExpect(sc.seamCosts.containsKey("0,1"), true);

    double cost10 = sc.getSeamCost(1, 0);
    double cost11 = sc.getSeamCost(1, 1);

    t.checkExpect(cost10 >= sc.getEnergy(1, 0), true);
    t.checkExpect(cost11 >= sc.getEnergy(1, 1), true);
  }

  void testFindMinBottomCol(Tester t) {
    SeamCarver sc = createTestSeamCarver();

    sc.getSeamCost(0, 0);
    sc.getSeamCost(0, 1);
    sc.getSeamCost(1, 0);
    sc.getSeamCost(1, 1);

    int minCol = sc.findMinBottomCol();

    t.checkExpect(minCol >= 0 && minCol < sc.width, true);
  }

  void testTraceSeam(Tester t) {
    SeamCarver sc = createTestSeamCarver();

    int minCol = sc.findMinBottomCol();

    ArrayList<Integer> seam = sc.traceSeam(minCol);

    t.checkExpect(seam.size(), sc.height);

    t.checkExpect(seam.get(sc.height - 1), minCol);

    for (int x : seam) {
      t.checkExpect(x >= 0 && x < sc.width, true);
    }
  }

  void testRemoveSeam(Tester t) {
    SeamCarver sc = createTestSeamCarver();
    int originalWidth = sc.width;

    ArrayList<Integer> seam = new ArrayList<Integer>();
    for (int y = 0; y < sc.height; y++) {
      seam.add(0);
    }

    sc.removeSeam(seam);

    t.checkExpect(sc.width, originalWidth - 1);

    for (ArrayList<IPixel> row : sc.pixelGrid) {
      t.checkExpect(row.size(), originalWidth - 1);
    }

    t.checkExpect(sc.energies.isEmpty(), true);
    t.checkExpect(sc.seamCosts.isEmpty(), true);

    t.checkExpect(sc.getPixelAt(0, 0).getColor(), Color.GREEN);
    t.checkExpect(sc.getPixelAt(1, 0).getColor(), Color.YELLOW);
  }

  void testMakeScene(Tester t) {
    SeamCarver sc = createTestSeamCarver();

    WorldScene scene = sc.makeScene();

    t.checkExpect(scene.width, sc.width);
    t.checkExpect(scene.height, sc.height);
  }

  void testOnTick(Tester t) {
    SeamCarver sc = createTestSeamCarver();
    int originalWidth = sc.width;

    sc.onTick();

    t.checkExpect(sc.width, originalWidth - 1);

    for (ArrayList<IPixel> row : sc.pixelGrid) {
      t.checkExpect(row.size(), originalWidth - 1);
    }
  }

  SeamCarver createTestSeamCarver() {
    SeamCarver sc = new SeamCarver("src/Balloons.jpg");

    sc.width = 2;
    sc.height = 2;

    ArrayList<ArrayList<IPixel>> grid = new ArrayList<ArrayList<IPixel>>();

    ArrayList<IPixel> row1 = new ArrayList<IPixel>();
    ArrayList<IPixel> row2 = new ArrayList<IPixel>();

    Pixel p1 = new Pixel(Color.RED);
    Pixel p2 = new Pixel(Color.GREEN);
    Pixel p3 = new Pixel(Color.BLUE);
    Pixel p4 = new Pixel(Color.YELLOW);

    row1.add(p1);
    row1.add(p2);
    row2.add(p3);
    row2.add(p4);

    grid.add(row1);
    grid.add(row2);

    BorderPixel border = new BorderPixel();

    p1.setLeft(border);
    p1.setRight(p2);
    p1.setTop(border);
    p1.setBottom(p3);
    p1.setTopLeft(border);
    p1.setTopRight(border);
    p1.setBottomLeft(border);
    p1.setBottomRight(p4);

    p2.setLeft(p1);
    p2.setRight(border);
    p2.setTop(border);
    p2.setBottom(p4);
    p2.setTopLeft(border);
    p2.setTopRight(border);
    p2.setBottomLeft(p3);
    p2.setBottomRight(border);

    p3.setLeft(border);
    p3.setRight(p4);
    p3.setTop(p1);
    p3.setBottom(border);
    p3.setTopLeft(border);
    p3.setTopRight(p2);
    p3.setBottomLeft(border);
    p3.setBottomRight(border);

    p4.setLeft(p3);
    p4.setRight(border);
    p4.setTop(p2);
    p4.setBottom(border);
    p4.setTopLeft(p1);
    p4.setTopRight(border);
    p4.setBottomLeft(border);
    p4.setBottomRight(border);

    sc.pixelGrid = grid;

    sc.energies.clear();
    sc.seamCosts.clear();

    return sc;
  }
}