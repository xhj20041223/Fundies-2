import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import tester.*;
import javalib.impworld.*;
import javalib.worldimages.*;
import java.awt.Color;

// Operations that can be performed on a pixel in an image
interface IPixel {
  // Calculates the energy value of this pixel
  double getEnergy();

  // Returns the brightness value of this pixel
  double getBrightness();

  // Returns the color of this pixel
  Color getColor();

  // Returns the energy of this pixel as a greyscale color
  Color getEnergyColor();

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

  // Calculates the horizontal energy gradient
  double getHorizEnergy();

  // Calculates the vertical energy gradient
  double getVertEnergy();

  // Draws this pixel to the given image at specified coordinates
  void drawToImage(ComputedPixelImage image, int x, int y);

  // Connects this pixel to its surrounding pixels
  void connectToNeighbors(int x, int y, int width, int height,
      ArrayList<ArrayList<IPixel>> pixelRows, IPixel border);

  // Initializes a new pixel at the given coordinates
  IPixel initPixelAt(int x, int y, FromFileImage image);

  // Recomputes the energy value of this pixel
  void recomputeEnergy();

  void drawEnergyToImage(ComputedPixelImage image, int x, int y);
}

// A standard pixel in an image with connections to surrounding pixels
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
  // The energy value of this pixel
  double energy;

  // Creates a new pixel with the given color
  Pixel(Color color) {
    this.color = color;
  }

  // Calculates the energy of this pixel
  public double getEnergy() {
    return this.energy;
  }

  // Recomputes the energy value and stores it
  public void recomputeEnergy() {
    this.energy = Math.sqrt(Math.pow(this.getHorizEnergy(), 2) + Math.pow(this.getVertEnergy(), 2));
  }

  // Returns the energy as a grayscale color for visualization
  public Color getEnergyColor() {
    int gray = (int) Math.floor(this.getEnergy() * 225 / (Math.sqrt(32)));
    return new Color(gray, gray, gray);
  }

  // Calculates the horizontal energy gradient
  public double getHorizEnergy() {
    return (this.getTopLeft().getBrightness() + 2 * this.getLeft().getBrightness()
        + this.getBottomLeft().getBrightness())
        - (this.getTopRight().getBrightness() + 2 * this.getRight().getBrightness()
            + this.getBottomRight().getBrightness());
  }

  // Calculates the vertical energy gradient
  public double getVertEnergy() {
    return (this.getTopLeft().getBrightness() + 2 * this.getTop().getBrightness()
        + this.getTopRight().getBrightness())
        - (this.getBottomLeft().getBrightness() + 2 * this.getBottom().getBrightness()
            + this.getBottomRight().getBrightness());
  }

  // Returns the brightness of this pixel (average of RGB values)
  public double getBrightness() {
    return (double) (this.color.getRed() + this.color.getGreen() + this.color.getBlue())
        / (255.0 * 3.0);
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

  // Draws this pixel to the given image
  public void drawToImage(ComputedPixelImage image, int x, int y) {
    image.setPixel(x, y, this.color);
  }

  public void drawEnergyToImage(ComputedPixelImage image, int x, int y) {
    image.setPixel(x, y, this.getEnergyColor());
  }

  // Gets a pixel at the given coordinates from the pixel rows
  public IPixel getPixelAt(int y, int x, ArrayList<ArrayList<IPixel>> pixelRows) {
    if (y < 0 || y >= pixelRows.size() || x < 0 || pixelRows.get(y).size() <= x) {
      return new BorderPixel();
    }
    return pixelRows.get(y).get(x);
  }

  // Connects this pixel to its surrounding pixels
  public void connectToNeighbors(int x, int y, int width, int height,
      ArrayList<ArrayList<IPixel>> pixelRows, IPixel border) {
    if (x > 0) {
      this.setLeft(this.getPixelAt(y, x - 1, pixelRows));
    }
    else {
      this.setLeft(border);
    }

    if (x < width - 1) {
      this.setRight(this.getPixelAt(y, x + 1, pixelRows));
    }
    else {
      this.setRight(border);
    }

    if (y > 0) {
      this.setTop(this.getPixelAt(y - 1, x, pixelRows));
    }
    else {
      this.setTop(border);
    }

    if (y < height - 1) {
      this.setBottom(this.getPixelAt(y + 1, x, pixelRows));
    }
    else {
      this.setBottom(border);
    }

    if (x > 0 && y > 0) {
      this.setTopLeft(this.getPixelAt(y - 1, x - 1, pixelRows));
    }
    else {
      this.setTopLeft(border);
    }

    if (x < width - 1 && y > 0) {
      this.setTopRight(this.getPixelAt(y - 1, x + 1, pixelRows));
    }
    else {
      this.setTopRight(border);
    }

    if (x > 0 && y < height - 1) {
      this.setBottomLeft(this.getPixelAt(y + 1, x - 1, pixelRows));
    }
    else {
      this.setBottomLeft(border);
    }

    if (x < width - 1 && y < height - 1) {
      this.setBottomRight(this.getPixelAt(y + 1, x + 1, pixelRows));
    }
    else {
      this.setBottomRight(border);
    }
  }

  // Initializes a new pixel at the given coordinates
  public IPixel initPixelAt(int x, int y, FromFileImage image) {
    return new Pixel(image.getColorAt(x, y));
  }
}

// A border pixel that surrounds the image
class BorderPixel implements IPixel {

  // Creates a new border pixel
  BorderPixel() {

  }

  // Border pixels have maximum energy
  public double getEnergy() {
    return Double.MAX_VALUE;
  }

  // Recompute energy (does nothing for border pixels)
  public void recomputeEnergy() {
    // Border pixels have fixed energy
  }

  // Returns the energy as a black color for visualization
  public Color getEnergyColor() {
    return Color.BLACK;
  }

  // Border pixels have zero brightness
  public double getBrightness() {
    return 0;
  }

  // Returns the color of this border pixel
  public Color getColor() {
    return Color.black;
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
    // do nothing because it is border pixels
  }

  // Border pixels ignore all setter methods
  public void setRight(IPixel p) {
    // do nothing because it is border pixels
  }

  // Border pixels ignore all setter methods
  public void setTop(IPixel p) {
    // do nothing because it is border pixels
  }

  // Border pixels ignore all setter methods
  public void setBottom(IPixel p) {
    // do nothing because it is border pixels
  }

  // Border pixels ignore all setter methods
  public void setTopLeft(IPixel p) {
    // do nothing because it is border pixels
  }

  // Border pixels ignore all setter methods
  public void setTopRight(IPixel p) {
    // do nothing because it is border pixels
  }

  // Border pixels ignore all setter methods
  public void setBottomLeft(IPixel p) {
    // do nothing because it is border pixels
  }

  // Border pixels ignore all setter methods
  public void setBottomRight(IPixel p) {
    // do nothing because it is border pixels
  }

  // Border pixels have zero horizontal energy
  public double getHorizEnergy() {
    return 0.0;
  }

  // Border pixels have zero vertical energy
  public double getVertEnergy() {
    return 0.0;
  }

  // Does nothing since borders pixels shouldn't be drawn
  public void drawToImage(ComputedPixelImage image, int x, int y) {
    // border pixels should never be drawn
  }

  // Does nothing since borders pixels shouldn't be drawn
  public void drawEnergyToImage(ComputedPixelImage image, int x, int y) {
    // border pixels should never be drawn
  }

  // Border pixels don't need to connect to neighbors
  public void connectToNeighbors(int x, int y, int width, int height,
      ArrayList<ArrayList<IPixel>> pixelRows, IPixel border) {
    // do nothing because it is border pixels
  }

  // Border pixels always create border pixels
  public IPixel initPixelAt(int x, int y, FromFileImage image) {
    return new BorderPixel();
  }
}

// Information about a pixel in a seam and methods for seam operations
class SeamInfo {
  // Total energy of the seam up to this pixel
  double totalWeight;
  // Previous pixel in the seam
  SeamInfo cameFrom;
  // X-coordinate of this pixel
  int x;
  // Y-coordinate of this pixel
  int y;

  // Creates a new seam info with the given parameters
  SeamInfo(double totalWeight, SeamInfo cameFrom, int x, int y) {
    this.totalWeight = totalWeight;
    this.cameFrom = cameFrom;
    this.x = x;
    this.y = y;
  }

  // Find the column in the bottom row with minimum energy
  public int findMinBottomColVert(int width, int height, HashMap<String, Double> seamCostMemo) {
    return this.findMinBottomColVertHelper(0, 1, Double.MAX_VALUE, 0, width, height, seamCostMemo);
  }

  // Helper for finding minimum energy column in bottom row
  public int findMinBottomColVertHelper(int x, int nextX, double minCost, int minX, int width,
      int height, HashMap<String, Double> seamCostMemo) {
    if (nextX > width) {
      return minX;
    }

    String key = (height - 1) + "," + x;
    double cost = seamCostMemo.getOrDefault(key, Double.MAX_VALUE);

    if (cost < minCost) {
      return this.findMinBottomColVertHelper(nextX, nextX + 1, cost, x, width, height,
          seamCostMemo);
    }
    else {
      return this.findMinBottomColVertHelper(nextX, nextX + 1, minCost, minX, width, height,
          seamCostMemo);
    }
  }

  // Find the row in the rightmost column with minimum energy
  public int findMinRightRowHori(int width, int height, HashMap<String, Double> seamCostMemo) {
    double minCost = Double.MAX_VALUE;
    int minY = 0;

    for (int y = 0; y < height; y++) {
      String key = y + "," + (width - 1);
      double cost = seamCostMemo.getOrDefault(key, Double.MAX_VALUE);

      if (cost < minCost) {
        minCost = cost;
        minY = y;
      }
    }

    return minY;
  }

  // Helper for finding minimum energy row in rightmost column
  public int findMinRightRowHoriHelper(int y, int nextY, double minCost, int minY, int width,
      int height, HashMap<String, Double> seamCostMemo) {
    if (nextY > height) {
      return minY;
    }

    String key = y + "," + (width - 1);
    double cost = seamCostMemo.getOrDefault(key, Double.MAX_VALUE);

    if (cost < minCost) {
      return this.findMinRightRowHoriHelper(nextY, nextY + 1, cost, y, width, height, seamCostMemo);
    }
    else {
      return this.findMinRightRowHoriHelper(nextY, nextY + 1, minCost, minY, width, height,
          seamCostMemo);
    }
  }

  // Trace the vertical seam path from bottom to top
  public ArrayList<Integer> traceVertSeam(int bottomX, int height,
      HashMap<String, Double> seamCostMemo) {
    ArrayList<Integer> seam = new ArrayList<Integer>();
    this.traceVertSeamHelper(height - 1, bottomX, seam, seamCostMemo);
    return seam;
  }

  // Helper for tracing the vertical seam path
  public void traceVertSeamHelper(int y, int x, ArrayList<Integer> seam,
      HashMap<String, Double> seamCostMemo) {
    seam.add(0, x);

    if (y <= 0) {
      return;
    }

    String leftKey = (y - 1) + "," + (x - 1);
    String centerKey = (y - 1) + "," + x;
    String rightKey = (y - 1) + "," + (x + 1);

    double leftCost = seamCostMemo.getOrDefault(leftKey, Double.MAX_VALUE);
    double centerCost = seamCostMemo.getOrDefault(centerKey, Double.MAX_VALUE);
    double rightCost = seamCostMemo.getOrDefault(rightKey, Double.MAX_VALUE);

    int nextX = x;
    if (leftCost < centerCost && leftCost < rightCost) {
      nextX = x - 1;
    }
    else if (rightCost < centerCost && rightCost < leftCost) {
      nextX = x + 1;
    }

    this.traceVertSeamHelper(y - 1, nextX, seam, seamCostMemo);
  }

  // Trace the horizontal seam path from right to left
  public ArrayList<Integer> traceHoriSeam(int rightY, int width,
      HashMap<String, Double> seamCostMemo) {
    ArrayList<Integer> seam = new ArrayList<>();
    for (int i = 0; i < width; i++) {
      seam.add(0);
    }

    this.traceHoriSeamHelper(rightY, width - 1, width, seam, seamCostMemo);
    return seam;
  }

  // Helper for tracing the horizontal seam path
  public void traceHoriSeamHelper(int y, int x, int width, ArrayList<Integer> seam,
      HashMap<String, Double> seamCostMemo) {
    seam.set(x, y);

    if (x <= 0) {
      return;
    }

    double topCost = Double.MAX_VALUE;
    double middleCost = Double.MAX_VALUE;
    double bottomCost = Double.MAX_VALUE;

    if (y > 0) {
      String topKey = (y - 1) + "," + (x - 1);
      topCost = seamCostMemo.getOrDefault(topKey, Double.MAX_VALUE);
    }

    String middleKey = y + "," + (x - 1);
    middleCost = seamCostMemo.getOrDefault(middleKey, Double.MAX_VALUE);

    if (y < width - 1) {
      String bottomKey = (y + 1) + "," + (x - 1);
      bottomCost = seamCostMemo.getOrDefault(bottomKey, Double.MAX_VALUE);
    }

    int nextY = y;
    if (topCost < middleCost && topCost < bottomCost) {
      nextY = y - 1;
    }
    else if (bottomCost < middleCost && bottomCost < topCost) {
      nextY = y + 1;
    }

    this.traceHoriSeamHelper(nextY, x - 1, width, seam, seamCostMemo);
  }

  // Get minimum cost to reach a pixel (y,x) from the top row
  public double getVertSeamCost(int y, int x, int height, int width,
      HashMap<String, Double> seamCostMemo, HashMap<String, Double> energyMemo) {
    if (y < 0 || x < 0 || y >= height || x >= width) {
      return Double.MAX_VALUE;
    }

    String key = y + "," + x;
    if (seamCostMemo.containsKey(key)) {
      return seamCostMemo.get(key);
    }

    double cost;
    if (y == 0) {
      cost = energyMemo.getOrDefault(key, Double.MAX_VALUE);
    }
    else {
      double leftCost = this.getVertSeamCost(y - 1, x - 1, height, width, seamCostMemo, energyMemo);
      double centerCost = this.getVertSeamCost(y - 1, x, height, width, seamCostMemo, energyMemo);
      double rightCost = this.getVertSeamCost(y - 1, x + 1, height, width, seamCostMemo,
          energyMemo);

      double minCost = centerCost;
      if (leftCost < minCost) {
        minCost = leftCost;
      }
      if (rightCost < minCost) {
        minCost = rightCost;
      }

      cost = minCost + energyMemo.getOrDefault(key, Double.MAX_VALUE);
    }

    seamCostMemo.put(key, cost);
    return cost;
  }

  // Get minimum cost to reach a pixel (y,x) from the leftmost column
  public double getHoriSeamCost(int y, int x, int height, int width,
      HashMap<String, Double> seamCostMemo, HashMap<String, Double> energyMemo) {
    if (y < 0 || x < 0 || y >= height || x >= width) {
      return Double.MAX_VALUE;
    }

    String key = y + "," + x;
    if (seamCostMemo.containsKey(key)) {
      return seamCostMemo.get(key);
    }

    double cost;
    if (x == 0) {
      cost = energyMemo.getOrDefault(key, Double.MAX_VALUE);
    }
    else {
      double topLeftCost = Double.MAX_VALUE;
      double leftCost = Double.MAX_VALUE;
      double bottomLeftCost = Double.MAX_VALUE;

      if (y > 0) {
        topLeftCost = this.getHoriSeamCost(y - 1, x - 1, height, width, seamCostMemo, energyMemo);
      }

      leftCost = this.getHoriSeamCost(y, x - 1, height, width, seamCostMemo, energyMemo);

      if (y < height - 1) {
        bottomLeftCost = this.getHoriSeamCost(y + 1, x - 1, height, width, seamCostMemo,
            energyMemo);
      }

      double minCost = leftCost;
      if (topLeftCost < minCost) {
        minCost = topLeftCost;
      }
      if (bottomLeftCost < minCost) {
        minCost = bottomLeftCost;
      }

      cost = minCost + energyMemo.getOrDefault(key, Double.MAX_VALUE);
    }

    seamCostMemo.put(key, cost);
    return cost;
  }

  // Calculate all vertical seam costs for the image
  public void calculateAllVertSeamCosts(int height, int width, HashMap<String, Double> seamCostMemo,
      HashMap<String, Double> energyMemo) {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        this.getVertSeamCost(y, x, height, width, seamCostMemo, energyMemo);
      }
    }
  }

  // Calculate all horizontal seam costs for the image
  public void calculateAllHoriSeamCosts(int height, int width, HashMap<String, Double> seamCostMemo,
      HashMap<String, Double> energyMemo) {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        this.getHoriSeamCost(y, x, height, width, seamCostMemo, energyMemo);
      }
    }
  }

  // Remove a vertical seam from the image
  public void removeVertSeam(ArrayList<Integer> seam, ArrayList<ArrayList<IPixel>> pixelRows,
      int height, int width, BorderPixel border) {
    ArrayList<ArrayList<IPixel>> newPixelRows = new ArrayList<ArrayList<IPixel>>();
    this.removeVertSeamRows(0, seam, pixelRows, newPixelRows, height);

    for (int i = 0; i < height; i++) {
      pixelRows.set(i, newPixelRows.get(i));
    }

    this.reconnectAffectedAfterVertSeam(0, seam, pixelRows, border, width - 1, height);
  }

  // Recursively remove vertical seam from all rows
  public void removeVertSeamRows(int y, ArrayList<Integer> seam,
      ArrayList<ArrayList<IPixel>> oldRows, ArrayList<ArrayList<IPixel>> newRows, int height) {
    if (y >= height) {
      return;
    }

    ArrayList<IPixel> newRow = new ArrayList<IPixel>();
    this.removeVertSeamFromRow(0, y, seam.get(y), oldRows.get(y), newRow);
    newRows.add(newRow);

    this.removeVertSeamRows(y + 1, seam, oldRows, newRows, height);
  }

  // Recursively build a new row without the pixel at seamX
  public void removeVertSeamFromRow(int x, int y, int seamX, ArrayList<IPixel> oldRow,
      ArrayList<IPixel> newRow) {
    if (x >= oldRow.size()) {
      return;
    }

    if (x != seamX) {
      newRow.add(oldRow.get(x));
    }

    this.removeVertSeamFromRow(x + 1, y, seamX, oldRow, newRow);
  }

  // Remove a horizontal seam from the image
  public void removeHoriSeam(ArrayList<Integer> seam, ArrayList<ArrayList<IPixel>> pixelRows,
      int height, int width, BorderPixel border) {
    if (seam.size() != width) {
      return;
    }

    ArrayList<ArrayList<IPixel>> newPixelRows = new ArrayList<>();

    for (int y = 0; y < height - 1; y++) {
      newPixelRows.add(new ArrayList<>());
    }

    for (int x = 0; x < width; x++) {
      int seamY = seam.get(x);
      int newY = 0;

      for (int y = 0; y < height; y++) {
        if (y != seamY) {
          newPixelRows.get(newY).add(pixelRows.get(y).get(x));
          newY++;
        }
      }
    }

    pixelRows.clear();
    pixelRows.addAll(newPixelRows);

    for (int y = 0; y < height - 1; y++) {
      for (int x = 0; x < width; x++) {
        pixelRows.get(y).get(x).connectToNeighbors(x, y, width, height - 1, pixelRows, border);
      }
    }
  }

  // Reconnect pixels affected by vertical seam removal
  public void reconnectAffectedAfterVertSeam(int y, ArrayList<Integer> seam,
      ArrayList<ArrayList<IPixel>> pixelRows, BorderPixel border, int width, int height) {
    if (y >= height) {
      return;
    }

    int seamX = seam.get(y);
    this.reconnectAffectedInRowAfterVert(Math.max(0, seamX - 1), Math.min(width - 1, seamX), y,
        pixelRows, border, width, height);

    this.reconnectAffectedAfterVertSeam(y + 1, seam, pixelRows, border, width, height);
  }

  // Reconnect affected pixels in a row after vertical seam removal
  public void reconnectAffectedInRowAfterVert(int startX, int endX, int y,
      ArrayList<ArrayList<IPixel>> pixelRows, BorderPixel border, int width, int height) {
    if (startX > endX) {
      return;
    }

    pixelRows.get(y).get(startX).connectToNeighbors(startX, y, width, height, pixelRows, border);
    this.reconnectAffectedInRowAfterVert(startX + 1, endX, y, pixelRows, border, width, height);
  }

  // Reconnect pixels affected by horizontal seam removal
  public void reconnectAffectedAfterHoriSeam(int x, ArrayList<ArrayList<IPixel>> pixelRows,
      BorderPixel border, int width, int height) {
    if (x >= width) {
      return;
    }

    this.reconnectColumnAfterHori(0, height - 1, x, pixelRows, border, width, height);

    this.reconnectAffectedAfterHoriSeam(x + 1, pixelRows, border, width, height);
  }

  // Reconnect affected pixels in a column after horizontal seam removal
  public void reconnectColumnAfterHori(int startY, int endY, int x,
      ArrayList<ArrayList<IPixel>> pixelRows, BorderPixel border, int width, int height) {
    if (startY > endY) {
      return;
    }

    pixelRows.get(startY).get(x).connectToNeighbors(x, startY, width, height, pixelRows, border);
    this.reconnectColumnAfterHori(startY + 1, endY, x, pixelRows, border, width, height);
  }
}

// The seam carving algorithm and display
class SeamCarver extends World {
  // 2D structure of pixels in the image
  ArrayList<ArrayList<IPixel>> grid;
  // Current width of the image
  int width;
  // Height of the image
  int height;
  // Memo for pixel energy values
  HashMap<String, Double> energyMemo;
  // Memo for seam cost values
  HashMap<String, Double> seamCostMemo;
  // SeamInfo object for seam calculations
  SeamInfo seamInfo;
  // Border pixel for boundary handling
  BorderPixel border;
  // Direction to carve: "vertical", "horizontal", or "both"
  String directionToCarve;
  // Visual mode to display normal image or energy
  String visualMode;
  // Flag to pause animation
  boolean paused;
  // Represent whether we have a painted seam
  boolean paintedSeam;
  // Represent the seam to be removed
  ArrayList<Posn> seam;
  // Represent the location of seams to be removed
  ArrayList<Integer> seamLocation;

  // Creates a new seam carver from an image file
  SeamCarver(String fileName) {
    FromFileImage image = new FromFileImage(fileName);
    this.width = (int) image.getWidth();
    this.height = (int) image.getHeight();
    this.grid = new ArrayList<ArrayList<IPixel>>();
    this.energyMemo = new HashMap<String, Double>();
    this.seamCostMemo = new HashMap<String, Double>();
    this.seamInfo = new SeamInfo(0, null, 0, 0);
    this.border = new BorderPixel();
    this.directionToCarve = "vertical";
    this.visualMode = "normal";
    this.paused = false;
    this.paintedSeam = false;
    this.seam = new ArrayList<Posn>();
    this.seamLocation = new ArrayList<Integer>();

    this.initRows(0, image);
    this.connectAllRows(0);
  }

  // Initialize all rows of pixels
  public void initRows(int y, FromFileImage image) {
    if (y >= this.height) {
      return;
    }

    ArrayList<IPixel> row = new ArrayList<IPixel>();
    this.initRow(0, y, row, image);
    this.grid.add(row);

    this.initRows(y + 1, image);
  }

  // Initialize pixels in a single row
  public void initRow(int x, int y, ArrayList<IPixel> row, FromFileImage image) {
    if (x >= this.width) {
      return;
    }

    IPixel pixel = new Pixel(Color.BLACK);
    row.add(pixel.initPixelAt(x, y, image));
    this.initRow(x + 1, y, row, image);
  }

  // Connect all rows of pixels
  public void connectAllRows(int y) {
    if (y >= this.height) {
      return;
    }

    this.connectRowPixels(0, y);
    this.connectAllRows(y + 1);
  }

  // Connect pixels in a single row
  public void connectRowPixels(int x, int y) {
    if (x >= this.width) {
      return;
    }

    this.getPixelAt(y, x).connectToNeighbors(x, y, this.width, this.height, this.grid, this.border);
    this.connectRowPixels(x + 1, y);
  }

  // Get a pixel at the given coordinates
  public IPixel getPixelAt(int y, int x) {
    if (y < 0 || y >= this.grid.size() || x < 0 || this.grid.get(y).size() <= x) {
      return new BorderPixel();
    }
    return this.grid.get(y).get(x);
  }

  // Get pixel energy with caching
  public double getEnergy(int y, int x) {
    String key = y + "," + x;
    if (!this.energyMemo.containsKey(key)) {
      this.energyMemo.put(key, this.getPixelAt(y, x).getEnergy());
    }
    return this.energyMemo.get(key);
  }

  // Update all energy values in the memo
  public void updateAllEnergy() {
    for (int y = 0; y < this.height; y++) {
      for (int x = 0; x < this.width; x++) {
        this.getPixelAt(y, x).recomputeEnergy();
        this.getEnergy(y, x);
      }
    }
  }

  // Find and remove a vertical seam
  public void findAndRemoveVertSeam() {
    if (this.paintedSeam) {
      this.seamInfo.removeVertSeam(this.seamLocation, this.grid, this.height, this.width,
          this.border);
      this.width--;
      this.energyMemo.clear();
      this.seamCostMemo.clear();
      this.seam.clear();
    }
    else {
      this.updateAllEnergy();
      this.seamInfo.calculateAllVertSeamCosts(this.height, this.width, this.seamCostMemo,
          this.energyMemo);
      int minCol = this.seamInfo.findMinBottomColVert(this.width, this.height, this.seamCostMemo);
      this.seamLocation = this.seamInfo.traceVertSeam(minCol, this.height, this.seamCostMemo);
      for (int i = 0; i < this.height; i += 1) {
        this.seam.add(new Posn(this.seamLocation.get(i), i));
      }
    }
  }

  // Find and remove a horizontal seam
  public void findAndRemoveHoriSeam() {
    if (this.paintedSeam) {
      this.seamInfo.removeHoriSeam(this.seamLocation, this.grid, this.height, this.width,
          this.border);
      this.height--;
      this.energyMemo.clear();
      this.seamCostMemo.clear();
      this.seam.clear();
    }
    else {
      this.updateAllEnergy();
      this.seamCostMemo.clear();
      this.seamInfo.calculateAllHoriSeamCosts(this.height, this.width, this.seamCostMemo,
          this.energyMemo);
      int minRow = this.seamInfo.findMinRightRowHori(this.width, this.height, this.seamCostMemo);
      this.seamLocation = this.seamInfo.traceHoriSeam(minRow, this.width, this.seamCostMemo);
      for (int i = 0; i < this.width; i += 1) {
        this.seam.add(new Posn(i, this.seamLocation.get(i)));
      }
    }
  }

  // Process one tick: find and remove a minimum energy seam
  public void onTick() {
    if (this.paused) {
      return;
    }

    if (this.width <= 1 || this.height <= 1) {
      return;
    }

    if (this.directionToCarve.equals("both")) {
      if (Math.random() < 0.5) {
        if (this.height > 1) {
          this.findAndRemoveHoriSeam();
        }
        else if (this.width > 1) {
          this.findAndRemoveVertSeam();
        }
      }
      else {
        if (this.width > 1) {
          this.findAndRemoveVertSeam();
        }
        else if (this.height > 1) {
          this.findAndRemoveHoriSeam();
        }
      }
    }
    else if (this.directionToCarve.equals("horizontal")) {
      if (this.height > 1) {
        this.findAndRemoveHoriSeam();
      }
    }
    else if (this.directionToCarve.equals("vertical")) {
      if (this.width > 1) {
        this.findAndRemoveVertSeam();
      }
    }
    this.paintedSeam = !this.paintedSeam;

  }

  // Handle key events to allow user to switch modes
  public void onKeyEvent(String key) {
    if (key.equals("v")) {
      this.directionToCarve = "vertical";
    }
    else if (key.equals("h")) {
      this.directionToCarve = "horizontal";
    }
    else if (key.equals("b")) {
      this.directionToCarve = "both";
    }
    else if (key.equals(" ")) {
      this.paused = !this.paused;
    }
    else if (key.equals("e")) {
      if (!this.visualMode.equals("byEnergy")) {
        this.visualMode = "byEnergy";
      }
      else {
        this.visualMode = "normal";
      }
    }
  }

  //Create the scene to be displayed
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(this.width, this.height);
    ComputedPixelImage image = new ComputedPixelImage(this.width, this.height);

    this.drawRows(0, image);
    if (this.paintedSeam) {
      this.paintRedSeam(image);
    }

    scene.placeImageXY(image, this.width / 2, this.height / 2);
    return scene;
  }

  // Paints all pixel locations in this.seam to red
  void paintRedSeam(ComputedPixelImage image) {
    // iterates through this.seam and sets all positions in it
    // to red
    for (Posn posn : this.seam) {
      image.setColorAt(posn.x, posn.y, Color.red);
    }
  }

  // Draw all rows
  public void drawRows(int y, ComputedPixelImage image) {
    if (y >= this.height) {
      return;
    }

    this.drawRowPixels(0, y, image);
    this.drawRows(y + 1, image);
  }

  // Draw pixels in a row
  public void drawRowPixels(int x, int y, ComputedPixelImage image) {
    if (x >= this.width) {
      return;
    }

    if (this.visualMode.equals("normal")) {
      this.getPixelAt(y, x).drawToImage(image, x, y);
    }
    else if (this.visualMode.equals("byEnergy")) {
      this.getPixelAt(y, x).drawEnergyToImage(image, x, y);
    }

    this.drawRowPixels(x + 1, y, image);
  }
}

class ExamplesSeamCarving {
  Pixel redPixel;
  Pixel greenPixel;
  Pixel bluePixel;
  Pixel yellowPixel;
  Pixel cyanPixel;
  Pixel magentaPixel;
  Pixel orangePixel;
  Pixel pinkPixel;
  Pixel whitePixel;
  BorderPixel border;
  SeamInfo seamInfo;
  HashMap<String, Double> seamCostMemo;
  HashMap<String, Double> energyMemo;
  SeamCarver testSeamCarver;

  void init() {
    redPixel = new Pixel(Color.RED);
    greenPixel = new Pixel(Color.GREEN);
    bluePixel = new Pixel(Color.BLUE);
    yellowPixel = new Pixel(Color.YELLOW);
    cyanPixel = new Pixel(Color.CYAN);
    magentaPixel = new Pixel(Color.MAGENTA);
    orangePixel = new Pixel(Color.ORANGE);
    pinkPixel = new Pixel(Color.PINK);
    whitePixel = new Pixel(Color.WHITE);
    border = new BorderPixel();
    seamInfo = new SeamInfo(0, null, 0, 0);

    seamCostMemo = new HashMap<>();
    seamCostMemo.put("0,0", 5.0);
    seamCostMemo.put("0,1", 10.0);
    seamCostMemo.put("0,2", 8.0);
    seamCostMemo.put("1,0", 12.0);
    seamCostMemo.put("1,1", 7.0);
    seamCostMemo.put("1,2", 11.0);
    seamCostMemo.put("2,0", 15.0);
    seamCostMemo.put("2,1", 9.0);
    seamCostMemo.put("2,2", 14.0);

    energyMemo = new HashMap<>();
    energyMemo.put("0,0", 1.0);
    energyMemo.put("0,1", 2.0);
    energyMemo.put("0,2", 3.0);
    energyMemo.put("1,0", 4.0);
    energyMemo.put("1,1", 2.0);
    energyMemo.put("1,2", 5.0);
    energyMemo.put("2,0", 3.0);
    energyMemo.put("2,1", 1.0);
    energyMemo.put("2,2", 6.0);

    testSeamCarver = createTestSeamCarver();
  }

  void testBigbang(Tester t) {
    SeamCarver world = new SeamCarver("src/Balloons.jpg");
    world.bigBang(world.width, world.height, 1);
  }

  void testSeamCarverConstructor(Tester t) {
    init();
    SeamCarver sc = new SeamCarver("src/Balloons.jpg");
    t.checkExpect(sc.width > 0, true);
    t.checkExpect(sc.height > 0, true);
    t.checkExpect(sc.grid.size(), sc.height);
    t.checkExpect(sc.grid.get(0).size(), sc.width);
    t.checkExpect(sc.energyMemo instanceof HashMap, true);
    t.checkExpect(sc.seamCostMemo instanceof HashMap, true);
  }

  void testPixelColorAndBrightness(Tester t) {
    init();
    t.checkExpect(redPixel.getColor(), Color.RED);
    t.checkExpect(greenPixel.getColor(), Color.GREEN);
    t.checkExpect(bluePixel.getColor(), Color.BLUE);

    t.checkExpect(redPixel.getBrightness(), (double) (255 + 0 + 0) / (255 * 3));
    t.checkExpect(greenPixel.getBrightness(), (double) (0 + 255 + 0) / (255 * 3));
    t.checkExpect(bluePixel.getBrightness(), (double) (0 + 0 + 255) / (255 * 3));
  }

  void testPixelConnections(Tester t) {
    init();
    redPixel.setLeft(greenPixel);
    redPixel.setRight(bluePixel);
    redPixel.setTop(yellowPixel);
    redPixel.setBottom(cyanPixel);
    redPixel.setTopLeft(magentaPixel);
    redPixel.setTopRight(orangePixel);
    redPixel.setBottomLeft(pinkPixel);
    redPixel.setBottomRight(whitePixel);

    t.checkExpect(redPixel.getLeft(), greenPixel);
    t.checkExpect(redPixel.getRight(), bluePixel);
    t.checkExpect(redPixel.getTop(), yellowPixel);
    t.checkExpect(redPixel.getBottom(), cyanPixel);
    t.checkExpect(redPixel.getTopLeft(), magentaPixel);
    t.checkExpect(redPixel.getTopRight(), orangePixel);
    t.checkExpect(redPixel.getBottomLeft(), pinkPixel);
    t.checkExpect(redPixel.getBottomRight(), whitePixel);
  }

  void testPixelEnergy(Tester t) {
    init();
    redPixel.setLeft(greenPixel);
    redPixel.setRight(bluePixel);
    redPixel.setTop(yellowPixel);
    redPixel.setBottom(cyanPixel);
    redPixel.setTopLeft(magentaPixel);
    redPixel.setTopRight(orangePixel);
    redPixel.setBottomLeft(pinkPixel);
    redPixel.setBottomRight(whitePixel);

    double horizEnergy = redPixel.getHorizEnergy();
    double vertEnergy = redPixel.getVertEnergy();

    double expectedHorizEnergy = (magentaPixel.getBrightness() + 2 * greenPixel.getBrightness()
        + pinkPixel.getBrightness())
        - (orangePixel.getBrightness() + 2 * bluePixel.getBrightness()
            + whitePixel.getBrightness());

    double expectedVertEnergy = (magentaPixel.getBrightness() + 2 * yellowPixel.getBrightness()
        + orangePixel.getBrightness())
        - (pinkPixel.getBrightness() + 2 * cyanPixel.getBrightness() + whitePixel.getBrightness());

    t.checkExpect(horizEnergy, expectedHorizEnergy);
    t.checkExpect(vertEnergy, expectedVertEnergy);
    redPixel.recomputeEnergy();
    double expectedTotalEnergy = Math
        .sqrt(Math.pow(expectedHorizEnergy, 2) + Math.pow(expectedVertEnergy, 2));
    t.checkExpect(redPixel.getEnergy(), expectedTotalEnergy);

    // Test recomputeEnergy
    redPixel.recomputeEnergy();
    t.checkExpect(redPixel.energy, expectedTotalEnergy);
  }

  void testBorderPixel(Tester t) {
    init();
    t.checkExpect(border.getColor(), Color.BLACK);
    t.checkExpect(border.getBrightness(), 0.0);
    t.checkExpect(border.getEnergy(), Double.MAX_VALUE);
    t.checkExpect(border.getEnergyColor(), Color.BLACK);

    t.checkExpect(border.getLeft(), border);
    t.checkExpect(border.getRight(), border);
    t.checkExpect(border.getTop(), border);

    border.setLeft(redPixel);
    t.checkExpect(border.getLeft(), border);

    t.checkExpect(border.getHorizEnergy(), 0.0);
    t.checkExpect(border.getVertEnergy(), 0.0);
  }

  void testSeamInfo(Tester t) {
    init();
    SeamInfo info1 = new SeamInfo(10.0, null, 5, 0);
    SeamInfo info2 = new SeamInfo(15.0, info1, 6, 1);
    SeamInfo info3 = new SeamInfo(20.0, info2, 7, 2);

    t.checkExpect(info1.totalWeight, 10.0);
    t.checkExpect(info1.cameFrom, null);
    t.checkExpect(info1.x, 5);
    t.checkExpect(info1.y, 0);

    t.checkExpect(info2.totalWeight, 15.0);
    t.checkExpect(info2.cameFrom, info1);
    t.checkExpect(info2.x, 6);
    t.checkExpect(info2.y, 1);

    t.checkExpect(info3.totalWeight, 20.0);
    t.checkExpect(info3.cameFrom, info2);
    t.checkExpect(info3.x, 7);
    t.checkExpect(info3.y, 2);
  }

  void testVertSeamOperations(Tester t) {
    init();
    int minCol = seamInfo.findMinBottomColVert(3, 3, seamCostMemo);
    t.checkExpect(minCol, 1);

    ArrayList<Integer> seam = seamInfo.traceVertSeam(minCol, 3, seamCostMemo);
    t.checkExpect(seam.size(), 3);
    t.checkExpect(seam.get(2), 1);

    for (int x : seam) {
      t.checkExpect(x >= 0 && x < 3, true);
    }

    HashMap<String, Double> altMemo = new HashMap<>();
    altMemo.put("2,0", 5.0);
    altMemo.put("2,1", 10.0);
    altMemo.put("2,2", 3.0);

    int altCol = seamInfo.findMinBottomColVert(3, 3, altMemo);
    t.checkExpect(altCol, 2);
  }

  void testHoriSeamOperations(Tester t) {
    init();
    int minRow = seamInfo.findMinRightRowHori(3, 3, seamCostMemo);
    t.checkExpect(minRow, 0);

    ArrayList<Integer> seam = seamInfo.traceHoriSeam(minRow, 3, seamCostMemo);
    t.checkExpect(seam.size(), 3);
    t.checkExpect(seam.get(2), 0);

    for (int y : seam) {
      t.checkExpect(y >= 0 && y < 3, true);
    }

    HashMap<String, Double> altMemo = new HashMap<>();
    altMemo.put("0,2", 3.0);
    altMemo.put("1,2", 8.0);
    altMemo.put("2,2", 5.0);

    int altRow = seamInfo.findMinRightRowHori(3, 3, altMemo);
    t.checkExpect(altRow, 0);
  }

  void testVertSeamCost(Tester t) {
    init();
    double cost00 = seamInfo.getVertSeamCost(0, 0, 3, 3, seamCostMemo, energyMemo);
    t.checkExpect(cost00, 5.0);
    t.checkExpect(seamCostMemo.containsKey("0,0"), true);

    double cost10 = seamInfo.getVertSeamCost(1, 0, 3, 3, seamCostMemo, energyMemo);
    t.checkExpect(cost10, 12.0);

    double cost11 = seamInfo.getVertSeamCost(1, 1, 3, 3, seamCostMemo, energyMemo);
    t.checkExpect(cost11, 7.0);

    // Add one more test case for invalid coordinates
    double costInvalid = seamInfo.getVertSeamCost(-1, 0, 3, 3, seamCostMemo, energyMemo);
    t.checkExpect(costInvalid, Double.MAX_VALUE);
  }

  void testHoriSeamCost(Tester t) {
    init();
    double cost00 = seamInfo.getHoriSeamCost(0, 0, 3, 3, seamCostMemo, energyMemo);
    t.checkExpect(cost00, 5.0);
    t.checkExpect(seamCostMemo.containsKey("0,0"), true);

    double cost01 = seamInfo.getHoriSeamCost(0, 1, 3, 3, seamCostMemo, energyMemo);
    t.checkExpect(cost01, 10.0);

    double cost11 = seamInfo.getHoriSeamCost(1, 1, 3, 3, seamCostMemo, energyMemo);
    t.checkExpect(cost11, 7.0);

    // Add one more test case for out of bounds
    double costOutOfBounds = seamInfo.getHoriSeamCost(3, 3, 3, 3, seamCostMemo, energyMemo);
    t.checkExpect(costOutOfBounds, Double.MAX_VALUE);
  }

  void testGetPixelAt(Tester t) {
    init();
    t.checkExpect(testSeamCarver.getPixelAt(0, 0).getColor(), Color.RED);
    t.checkExpect(testSeamCarver.getPixelAt(0, 1).getColor(), Color.GREEN);
    t.checkExpect(testSeamCarver.getPixelAt(1, 0).getColor(), Color.BLUE);

    t.checkExpect(testSeamCarver.getPixelAt(-1, 0) instanceof BorderPixel, true);
    t.checkExpect(testSeamCarver.getPixelAt(0, -1) instanceof BorderPixel, true);
    t.checkExpect(testSeamCarver.getPixelAt(2, 0) instanceof BorderPixel, true);
  }

  void testGetEnergy(Tester t) {
    init();
    double energy00 = testSeamCarver.getEnergy(0, 0);
    t.checkExpect(testSeamCarver.energyMemo.containsKey("0,0"), true);
    t.checkExpect(energy00, testSeamCarver.getEnergy(0, 0));

    t.checkExpect(testSeamCarver.energyMemo.containsKey("0,1"), true);

    t.checkExpect(testSeamCarver.energyMemo.containsKey("1,0"), true);
  }

  void testVerticalSeamRemoval(Tester t) {
    init();
    int originalWidth = testSeamCarver.width;

    testSeamCarver.onTick();
    t.checkExpect(testSeamCarver.width, originalWidth);
    t.checkExpect(testSeamCarver.height, testSeamCarver.grid.size());

    for (ArrayList<IPixel> row : testSeamCarver.grid) {
      t.checkExpect(row.size(), originalWidth);
    }

    // One more tick to remove another seam
    testSeamCarver.onTick();
    t.checkExpect(testSeamCarver.width, 2);
  }

  void testHorizontalSeamRemoval(Tester t) {
    init();
    testSeamCarver.directionToCarve = "horizontal";
    int originalHeight = testSeamCarver.height;

    testSeamCarver.onTick();
    t.checkExpect(testSeamCarver.height, originalHeight);
    t.checkExpect(testSeamCarver.width, testSeamCarver.grid.get(0).size());
    t.checkExpect(testSeamCarver.grid.size(), originalHeight);

    // Check for size limit handling
    SeamCarver scMinSize = createTestSeamCarver();
    scMinSize.width = 1;
    scMinSize.height = 1;
    scMinSize.onTick();
    t.checkExpect(scMinSize.height, 1); // Should not change
  }

  void testKeyEvents(Tester t) {
    init();
    testSeamCarver.directionToCarve = "horizontal";
    testSeamCarver.visualMode = "normal";
    testSeamCarver.paused = false;

    testSeamCarver.onKeyEvent("v");
    t.checkExpect(testSeamCarver.directionToCarve, "vertical");

    testSeamCarver.onKeyEvent("h");
    t.checkExpect(testSeamCarver.directionToCarve, "horizontal");

    testSeamCarver.onKeyEvent("b");
    t.checkExpect(testSeamCarver.directionToCarve, "both");

    testSeamCarver.onKeyEvent(" ");
    t.checkExpect(testSeamCarver.paused, true);

    testSeamCarver.onKeyEvent("e");
    t.checkExpect(testSeamCarver.visualMode, "byEnergy");

    testSeamCarver.onKeyEvent("e");
    t.checkExpect(testSeamCarver.visualMode, "normal");
  }

  void testDrawToImage(Tester t) {
    init();
    ComputedPixelImage image = new ComputedPixelImage(2, 2);

    redPixel.drawToImage(image, 0, 0);
    t.checkExpect(image.getColorAt(0, 0), Color.RED);

    greenPixel.drawToImage(image, 0, 1);
    t.checkExpect(image.getColorAt(0, 1), Color.GREEN);

    bluePixel.drawToImage(image, 1, 0);
    t.checkExpect(image.getColorAt(1, 0), Color.BLUE);
  }

  void testConnectToNeighbors(Tester t) {
    init();
    ArrayList<ArrayList<IPixel>> pixelRows = new ArrayList<>();

    ArrayList<IPixel> row0 = new ArrayList<>();
    ArrayList<IPixel> row1 = new ArrayList<>();

    row0.add(redPixel);
    row0.add(greenPixel);
    row1.add(bluePixel);
    row1.add(yellowPixel);

    pixelRows.add(row0);
    pixelRows.add(row1);

    redPixel.connectToNeighbors(0, 0, 2, 2, pixelRows, border);

    t.checkExpect(redPixel.getLeft(), border);
    t.checkExpect(redPixel.getRight(), greenPixel);
    t.checkExpect(redPixel.getTop(), border);

    yellowPixel.connectToNeighbors(1, 1, 2, 2, pixelRows, border);
    t.checkExpect(yellowPixel.getLeft(), bluePixel);
    t.checkExpect(yellowPixel.getTop(), greenPixel);
    t.checkExpect(yellowPixel.getTopLeft(), redPixel);
  }

  void testRemoveVertSeam(Tester t) {
    init();
    ArrayList<ArrayList<IPixel>> pixelRows = new ArrayList<>();

    for (int y = 0; y < 3; y++) {
      ArrayList<IPixel> row = new ArrayList<>();
      for (int x = 0; x < 3; x++) {
        row.add(new Pixel(new Color(x * 50, y * 50, 0)));
      }
      pixelRows.add(row);
    }

    ArrayList<Integer> seam = new ArrayList<>();
    seam.add(1);
    seam.add(1);
    seam.add(1);

    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        pixelRows.get(y).get(x).connectToNeighbors(x, y, 3, 3, pixelRows, border);
      }
    }

    seamInfo.removeVertSeam(seam, pixelRows, 3, 3, border);

    t.checkExpect(pixelRows.get(0).size(), 2);
    t.checkExpect(pixelRows.get(0).get(0).getColor(), new Color(0, 0, 0));
    t.checkExpect(pixelRows.get(0).get(1).getColor(), new Color(100, 0, 0));

    // Test connections
    t.checkExpect(pixelRows.get(0).get(0).getRight(), pixelRows.get(0).get(1));
    t.checkExpect(pixelRows.get(0).get(1).getLeft(), pixelRows.get(0).get(0));
  }

  void testRemoveHoriSeam(Tester t) {
    init();
    ArrayList<ArrayList<IPixel>> pixelRows = new ArrayList<>();

    for (int y = 0; y < 3; y++) {
      ArrayList<IPixel> row = new ArrayList<>();
      for (int x = 0; x < 3; x++) {
        row.add(new Pixel(new Color(x * 50, y * 50, 0)));
      }
      pixelRows.add(row);
    }

    ArrayList<Integer> seam = new ArrayList<>();
    seam.add(1);
    seam.add(1);
    seam.add(1);

    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        pixelRows.get(y).get(x).connectToNeighbors(x, y, 3, 3, pixelRows, border);
      }
    }

    t.checkExpect(pixelRows.size(), 3);
    seamInfo.removeHoriSeam(seam, pixelRows, 3, 3, border);
    t.checkExpect(pixelRows.size(), 2);

    // Test pixel colors
    t.checkExpect(pixelRows.get(0).get(0).getColor(), new Color(0, 0, 0));
    t.checkExpect(pixelRows.get(0).get(1).getColor(), new Color(50, 0, 0));

    // Test connections
    t.checkExpect(pixelRows.get(0).get(0).getBottom(), pixelRows.get(1).get(0));
  }

  void testVisualMode(Tester t) {
    init();
    testSeamCarver.visualMode = "normal";
    ComputedPixelImage image1 = new ComputedPixelImage(2, 2);
    testSeamCarver.drawRowPixels(0, 0, image1);
    t.checkExpect(image1.getColorAt(0, 0), Color.RED);

    testSeamCarver.visualMode = "byEnergy";
    ComputedPixelImage image2 = new ComputedPixelImage(2, 2);
    testSeamCarver.drawRowPixels(0, 0, image2);
    t.checkExpect(image2.getColorAt(0, 0), testSeamCarver.getPixelAt(0, 0).getEnergyColor());
  }

  void testDirectionToCarve(Tester t) {
    init();
    SeamCarver sc = createTestSeamCarver();
    sc.directionToCarve = "both";
    sc.width = 2;
    sc.height = 2;

    // Save original dimensions
    int origWidth = sc.width;
    int origHeight = sc.height;

    // Force vertical carving
    sc.directionToCarve = "vertical";
    sc.onTick();
    t.checkExpect(sc.width, origWidth);
    t.checkExpect(sc.height, origHeight);

    // Reset and force horizontal carving
    sc = createTestSeamCarver();
    sc.directionToCarve = "horizontal";
    sc.width = 2;
    sc.height = 2;
    origWidth = sc.width;
    origHeight = sc.height;

    sc.onTick();
    t.checkExpect(sc.width, origWidth);
    t.checkExpect(sc.height, origHeight);

  }

  void testPaintRedSeam(Tester t) {
    this.init();
    ArrayList<Posn> seam = new ArrayList<Posn>(Arrays.asList(new Posn(0, 0), new Posn(1, 1)));
    this.testSeamCarver.seam = seam;

    ComputedPixelImage testImage = new ComputedPixelImage(2, 2);
    testImage.setColorAt(0, 0, Color.red);
    testImage.setColorAt(1, 1, Color.red);
    ComputedPixelImage newImage = new ComputedPixelImage(2, 2);
    this.testSeamCarver.paintRedSeam(newImage);
    t.checkExpect(newImage, testImage);
  }

  void testPause(Tester t) {
    this.init();

    SeamCarver testCarverCopy = this.createTestSeamCarver();

    this.testSeamCarver.onKeyEvent(" ");
    this.testSeamCarver.onTick();
    this.testSeamCarver.onTick();
    this.testSeamCarver.onTick();
    this.testSeamCarver.onTick();
    this.testSeamCarver.onTick();
    this.testSeamCarver.onTick();
    this.testSeamCarver.onKeyEvent(" ");
    t.checkExpect(testCarverCopy, this.testSeamCarver);
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

    sc.grid = grid;
    sc.energyMemo.clear();
    sc.seamCostMemo.clear();
    sc.visualMode = "normal";
    sc.directionToCarve = "both";
    sc.paused = false;

    return sc;
  }
}