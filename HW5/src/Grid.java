import tester.*; // The tester library
import javalib.worldimages.*;
import javalib.funworld.*;
import javalib.worldcanvas.WorldCanvas;

//to represent placement of objects in a Sokoban level
abstract class Grid {

  // the rows of this grid with ground content
  ILoRow rows;

  // the constructor
  Grid() {
    this.rows = new MtLoRow();
  }

  // constructor that takes in a list of lists
  Grid(ILoRow rows) {
    this.rows = rows;
  }

  // draws this grid
  WorldImage drawGrid() {
    return rows.drawRows();
  }

  // creates the list of cells of this row
  ILoRow createRows(ILoString rowDescriptions, CellFactory factory) {
    return rowDescriptions.toRows(factory);
  }
}

//represents a grid of ground content
class GridGround extends Grid {
  // list of targets
  IList<Target> targets;

  // default constructor
  GridGround() {
    // create blank row with blank content
    this.targets = new MtList<Target>();
  }

  // convenience constructor that takes a String description
  GridGround(String description) {
    // utils class
    Utils util = new Utils();
    // specifies to create ground cells
    GroundFactory groundFactory = new GroundFactory();
    ILoString rowDescriptions = util.createListOfString(description);
    this.rows = createRows(rowDescriptions, groundFactory);
  }

  /*
   * TEMPLATE: Fields: ... this.rows ... -- ILoRow ... this.targets ... --
   * IList<Target>
   * 
   * Methods: ... drawGrid() ... -- WorldImage ... createRows(ILoString
   * rowDescriptions, CellFactory factory) ... -- ILoRow
   * 
   * Methods for fields: ... this.rows.drawRows() ... -- WorldImage ...
   * this.rows.drawRows(WorldImage grid) ... -- WorldImage ...
   * this.rows.hasEmptyCell(Posn pos) ... -- boolean ...
   * this.rows.hasEmptyCell(Posn pos, int rowCount) ... -- boolean
   */

  boolean allTargetsFilled(GridContent gc) {
    return this.rows.sameColorTargetTrophyRows(gc.rows);
  }
}

//represents a grid of cell contents
class GridContent extends Grid {
  // player position
  Player player;
  // list of trophies
  IList<Trophy> trophies;

  // constructor
  GridContent() {
    // create blank row with blank content
  }

  // convenience constructor
  GridContent(String description) {
    // utils class
    Utils util = new Utils();
    // specifies to create ground cells
    ContentFactory contentFactory = new ContentFactory();
    ILoString rowDescriptions = util.createListOfString(description);
    this.rows = createRows(rowDescriptions, contentFactory);
  }

  /*
   * TEMPLATE: Fields: ... this.rows ... -- ILoRow ... this.player ... -- Player
   * ... this.trophies ... -- IList<Trophy>
   * 
   * Methods: ... drawGrid() ... -- WorldImage ... createRows(ILoString
   * rowDescriptions, CellFactory factory) ... -- ILoRow ... movePlayerUp() ... --
   * void
   * 
   * Methods for fields: ... this.rows.drawRows() ... -- WorldImage ...
   * this.rows.drawRows(WorldImage grid) ... -- WorldImage ...
   * this.rows.hasEmptyCell(Posn pos) ... -- boolean ...
   * this.rows.hasEmptyCell(Posn pos, int rowCount) ... -- boolean ...
   * this.player.draw() ... -- WorldImage ... this.player.getPosition() ... --
   * Posn
   */

  // EFFECT: moves the player of this grid up if cell above is empty
  void movePlayerUp() {
    // if is empty cell, move, otherwise don't move
    // changes list
    // needs to be valid place to move to
    if (this.player.getPosition().y - 1 < 0) {
      return;
    }
    Posn cellAbove = new Posn(this.player.getPosition().x, this.player.getPosition().y - 1);
    if (this.rows.hasEmptyCell(cellAbove)) {
      // move player up and make new list
      this.player.updatePosition(cellAbove);
    }
    else {
      return;
    }
  }

  // EFFECT: moves the player of this grid up if cell above is empty
  void movePlayerDown() {
    // if is empty cell, move, otherwise don't move
    // changes list
    // needs to be valid place to move to
    if (this.player.getPosition().y + 1 < 0) {
      return;
    }
    Posn cellBelow = new Posn(this.player.getPosition().x, this.player.getPosition().y + 1);
    if (this.rows.hasEmptyCell(cellBelow)) {
      // move player up and make new list
      this.player.updatePosition(cellBelow);
    }
    else {
      return;
    }
  }

  // EFFECT: moves the player of this grid up if cell above is empty
  void movePlayerLeft() {
    // if is empty cell, move, otherwise don't move
    // changes list
    // needs to be valid place to move to
    if (this.player.getPosition().x - 1 < 0) {
      return;
    }
    Posn cellToLeft = new Posn(this.player.getPosition().x - 1, this.player.getPosition().y);
    if (this.rows.hasEmptyCell(cellToLeft)) {
      // move player up and make new list
      this.player.updatePosition(cellToLeft);
    }
    else {
      return;
    }
  }

  // EFFECT: moves the player of this grid up if cell above is empty
  void movePlayerRight() {
    // if is empty cell, move, otherwise don't move
    // changes list
    // needs to be valid place to move to
    if (this.player.getPosition().x + 1 < 0) {
      return;
    }
    Posn cellToRight = new Posn(this.player.getPosition().x + 1, this.player.getPosition().y);
    if (this.rows.hasEmptyCell(cellToRight)) {
      // move player up and make new list
      this.player.updatePosition(cellToRight);
    }
    else {
      return;
    }
  }
}

//utility methods
class Utils {
  // parses through a String and makes a list of Strings, separating by line
  ILoString createListOfString(String s) {
    return createListOfStringHelper(s, 0, "");
  }

  // converts a String into a list of String's, separated by line
  ILoString createListOfStringHelper(String s, int i, String acc) {
    if (i < s.length() - 1) {
      if (s.substring(i, i + 1).equals("\n")) {
        return new ConsLoString(acc, createListOfStringHelper(s, i + 1, ""));
      }
      else if (!s.substring(i).contains("\n")) {
        return new ConsLoString(s.substring(i), createListOfStringHelper(s, s.length() - 1, ""));
      }
      else {
        return createListOfStringHelper(s, i + 1, acc.concat(s.substring(i, i + 1)));
      }
    }
    return new MtLoString();
  }
}

class ExamplesGrid {
  String exampleLevelGround = "________\n" + "___R____\n" + "________\n" + "_B____Y_\n"
      + "________\n" + "___G____\n" + "________";
  String exampleLevelContent = "__WWW___\n" + "__WrWW__\n" + "WWW__WWW\n" + "W__>_ByW\n"
      + "WWb_WWWW\n" + "_WWgW___\n" + "__WWW___";

  // tests the creation of a grid of ground cells
  boolean testCreateGridGround(Tester t) {
    String description = "___\n" + "B_Y\n" + "_G_";
    GroundFactory groundFactory = new GroundFactory();
    GridGround grid = new GridGround(description);
    Row row1 = new Row(0, "___", groundFactory);
    Row row2 = new Row(1, "B_Y", groundFactory);
    Row row3 = new Row(2, "_G_", groundFactory);
    return t.checkExpect(grid.rows,
        new ConsLoRow(row1, new ConsLoRow(row2, new ConsLoRow(row3, new MtLoRow()))));
  }

  // tests the creation of a grid of content cells
  boolean testCreateGridContent(Tester t) {
    String description = "_W_\n" + "b>y\n" + "_gB";
    ContentFactory contentFactory = new ContentFactory();
    GridContent grid = new GridContent(description);
    Row row1 = new Row(0, "_W_", contentFactory);
    Row row2 = new Row(1, "b>y", contentFactory);
    Row row3 = new Row(2, "_gB", contentFactory);
    return t.checkExpect(grid.rows,
        new ConsLoRow(row1, new ConsLoRow(row2, new ConsLoRow(row3, new MtLoRow()))));
  }

  // draws grid content
  boolean testDrawGridGround(Tester t) {
    WorldCanvas c = new WorldCanvas(1250, 750);
    WorldScene s = new WorldScene(1250, 750);
    String description = "B_Y\n" + "___\n" + "_G_";
    GridGround grid = new GridGround(description);
    return c.drawScene(s.placeImageXY(grid.drawGrid(), 625, 375)) && c.show();
  }

  // draws grid content
  boolean testDrawGridContent(Tester t) {
    WorldCanvas c = new WorldCanvas(1250, 750);
    WorldScene s = new WorldScene(1250, 750);
    String description = "_W_\n" + "b>y\n" + "_gB";
    GridContent grid = new GridContent(description);
    return c.drawScene(s.placeImageXY(grid.drawGrid(), 625, 375)) && c.show();
  }

  // tests that all targets are 
  // occupied by the respect color trophy
  boolean testLevelWon(Tester t) {
    GridGround grid2 = new GridGround(exampleLevelGround);
    GridContent grid3 = new GridContent(exampleLevelContent);
    return t.checkExpect(grid2.allTargetsFilled(grid3), false);
  }

  // test that list of strings is created from one string
  boolean testCreatListOfString(Tester t) {
    Utils util = new Utils();
    String exampleLevelContents = "WWW_\n" + "W_WW\n" + "WWWr_WWW\n" + "W_b>yB_W\n" + "WW_gWWWW\n"
        + "_WWW__\n" + "WWW_";
    ILoString strings3 = new ConsLoString("WW_gWWWW",
        new ConsLoString("_WWW__", new ConsLoString("WWW_", new MtLoString())));
    ILoString strings2 = new ConsLoString("W_WW",
        new ConsLoString("WWWr_WWW", new ConsLoString("W_b>yB_W", strings3)));
    ILoString strings = new ConsLoString("WWW_", strings2);
    return t.checkExpect(util.createListOfString(exampleLevelContents), strings);
  }
}
