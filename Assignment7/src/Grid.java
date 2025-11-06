import tester.*; // The tester library
import javalib.worldimages.*;
import javalib.funworld.*;
import javalib.worldcanvas.WorldCanvas;
import java.util.ArrayList;

// to represent placement of objects in a Sokoban level
abstract class Grid {

  // the rows of this grid with ground content
  ArrayList<Row> rows;

  // the constructor
  Grid() {
    this.rows = new ArrayList<Row>();
  }

  // constructor that takes in a list of lists
  Grid(ArrayList<Row> rows) {
    this.rows = rows;
  }

  // draws this grid
  WorldImage drawGrid() {
    // return rows.drawRows();
    // for every row in the list, stack the row image on top of each other
    WorldImage grid = new EmptyImage();
    for (Row r : rows) {
      grid = new AboveImage(grid, r.drawRow());
    }
    return grid;
  }

  // creates the list of cells of this row
  ArrayList<Row> createRows(ILoString rowDescriptions, CellFactory factory) {
    ArrayList<Row> rows = new ArrayList<Row>();
    int rowNumber = 0;
    ILoString current = rowDescriptions;
    while (current instanceof ConsLoString) {
      ConsLoString cons = (ConsLoString) current;
      Row row = new Row(rowNumber, cons.first, factory);
      rows.add(row);
      current = cons.rest;
      rowNumber++;
    }
    return rows;
  }

  // sets the cell at the specified position as the given cell
  void setCellAs(Posn pos, Cell c) {
    Row rowOfObject = this.rows.get(pos.y);
    rowOfObject.set(pos.x, c);
  }
}

// represents a grid of ground content
class GridGround extends Grid {
  // list of targets
  ArrayList<Target> targets;

  // list of holes
  ArrayList<Hole> holes;

  // default constructor
  GridGround() {
    // create blank row with blank content
    this.targets = new ArrayList<Target>();
    this.holes = new ArrayList<Hole>();
  }

  // convenience constructor that takes a String description
  GridGround(String description) {
    // utils class
    Utils util = new Utils();
    this.targets = new ArrayList<Target>();
    this.holes = new ArrayList<Hole>();
    // specifies to create ground cells
    GroundFactory groundFactory = new GroundFactory();
    ILoString rowDescriptions = util.createListOfString(description);
    this.rows = createRows(rowDescriptions, groundFactory);

    for (Row r : this.rows) {
      r.addToTargetList(targets);
      r.addToHoleList(holes);
    }
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

  // determine whether all targets are filled by trophies
  boolean allTargetsFilled(GridContent contents) {
    for (Target t : this.targets) {
      if (!contents.existTargetTrophy(t.position, t.color.toLowerCase())) {
        return false;
      }
    }
    return true;
  }

  // determine whether the target cannot be filled anyway
  boolean noEnoughtTrophies(GridContent contents) {
    return contents.noEnoughtTrophiesHelp(this.targets);
  }

  // checks whether the given content is overlapping with a hole
  boolean isOnHole(CellContent c) {
    for (Hole h : this.holes) {
      if (c.isInSamePlace(h)) {
        return true;
      }
    }
    return false;
  }

  // EFFECT: removes any objects that collided with holes and hole itself
  void removeObjectsOnHoles(GridContent content) {
    for (int i = 0 ; i < this.holes.size() ; i ++) {
      Posn holePos = this.holes.get(i).getPosition();
      if (content.isNotEmptyAt(holePos)) {
        // set ground as blank
        this.setCellAs(holePos, new BlankGround(holePos));
        // set contentgrid as blank
        content.setCellAs(holePos, new BlankCell(holePos));
        this.holes.remove(i);
      }
    }
  }
}

// represents a grid of cell contents
class GridContent extends Grid {
  // player position
  Player player;
  // list of trophies
  ArrayList<Trophy> trophies;

  // constructor
  GridContent() {
    // create blank row with blank content
    this.trophies = new ArrayList<Trophy>();
  }

  // convenience constructor
  GridContent(String description) {
    Utils util = new Utils();
    this.trophies = new ArrayList<Trophy>();
    ContentFactory contentFactory = new ContentFactory();
    ILoString rowDescriptions = util.createListOfString(description);
    this.rows = createRows(rowDescriptions, contentFactory);

    for (int i = 0; i < this.rows.size(); i++) {
      Row row = this.rows.get(i);
      row.rowNumber = i;
      row.addToTrophyList(trophies);
    }
    this.player = this.findPlayer();
  }

  // sets the cell at the specified position as the given cell
  void setCellAs(Posn pos, Cell c) {
    Row rowOfObject = this.rows.get(pos.y);
    rowOfObject.set(pos.x, c);
    if (rowOfObject.cells.get(pos.x) instanceof Trophy) {
      this.trophies.remove(rowOfObject);
    }
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

  // EFFECT: moves the player of this grid in the specified direction if possible
  void movePlayer(String direction) {
    Posn currentPos = player.getPosition();

    if (!canMove(currentPos, direction)) {
      return;
    }
    PosDirection d = new PosDirection();
    Posn newPos = d.posOfDirection(currentPos, direction);
    Row newRow = rows.get(newPos.y);
    CellContent newCell = (CellContent) newRow.getObject(newPos.x);

    if (newCell instanceof Crate) {
      Posn behindPos = d.posOfDirection(newPos, direction);
      Crate crate = (Crate) newCell;
      crate.move(behindPos);
      swapContents(newPos, behindPos);
    }

    if (newCell instanceof Trophy) {
      Posn behindPos = d.posOfDirection(newPos, direction);
      Trophy trophy = (Trophy) newCell;
      trophy.move(behindPos);
      swapContents(newPos, behindPos);
    }

    player.move(newPos);
    swapContents(currentPos, newPos);

    player.changeDirection(direction);
  }

  // EFFECT: moves the given object of this grid in the specified direction if
  // possible
  void moveContent(Movable content, String direction) {
    PosDirection d = new PosDirection();
    // the position of the movable object
    Posn contentPos = content.getPosition();
    // the position to move to
    Posn newPos = d.posOfDirection(contentPos, direction);

    // check if next space is empty
    if (canMove(contentPos, direction)) {
      // content.move(newPos);
      this.swapContents(contentPos, newPos);
    }
    // if not, check if next space is movable object next to empty space
    else if (this.canMove(newPos, direction)) {
      // change position of content
      // content.move(newPos);
      // position of cell two away
      Posn nextToNewPos = d.posOfDirection(newPos, direction);
      this.swapContents(newPos, nextToNewPos);
      this.swapContents(contentPos, newPos);
    }
    else {
      return;
    }
  }

  // swaps the two given cell positions in this grid
  void swapContents(Posn pos1, Posn pos2) {
    Row rowOfObject1 = this.rows.get(pos1.y);
    Row rowOfObject2 = this.rows.get(pos2.y);
    CellContent object1 = (CellContent) rowOfObject1.getObject(pos1.x);
    CellContent object2 = (CellContent) rowOfObject2.getObject(pos2.x);
    object1.move(pos2);
    object2.move(pos1);
    // set object1's index to object2
    rowOfObject1.set(pos1.x, object2);
    // set object2's index to object1
    rowOfObject2.set(pos2.x, object1);
  }

  // checks whether the object in the specified position in this grid can
  // move to the position of the specified direction
  boolean canMove(Posn objPos, String direction) {
    // check if object position is valid
    if (objPos.x < 0 || objPos.y < 0 || objPos.y >= rows.size()) {
      return false;
    }

    Row currentRow = rows.get(objPos.y);
    if (objPos.x >= currentRow.cells.size()) {
      return false;
    }

    CellContent currentCell = (CellContent) currentRow.getObject(objPos.x);
    if (!currentCell.canMove()) {
      return false;
    }

    PosDirection d = new PosDirection();
    Posn newPos = d.posOfDirection(objPos, direction);

    if (newPos.y < 0 || newPos.y >= rows.size()) {
      return false;
    }
    Row newRow = rows.get(newPos.y);
    if (newPos.x < 0 || newPos.x >= newRow.cells.size()) {
      return false;
    }
    CellContent newCell = (CellContent) newRow.getObject(newPos.x);

    if (newCell.isEmptyCell()) {
      return true;
    }
    if (newCell instanceof Crate || newCell instanceof Trophy) {
      Posn behindPos = d.posOfDirection(newPos, direction);
      if (behindPos.y < 0 || behindPos.y >= rows.size())
        return false;
      Row behindRow = rows.get(behindPos.y);
      if (behindPos.x < 0 || behindPos.x >= behindRow.cells.size())
        return false;

      return behindRow.getObject(behindPos.x).isEmptyCell();
    }
    return false;
  }

  // determine whether there is a specific trophy at that posn
  boolean existTargetTrophy(Posn posn, String color) {
    for (Trophy t : this.trophies) {
      if (t.getPosition().equals(posn) && t.color.equals(color)) {
        return true;
      }
    }
    return false;
  }

  // a helper method for noEnoughtTrophies
  boolean noEnoughtTrophiesHelp(ArrayList<Target> targets) {
    ArrayList<Trophy> temp = new ArrayList<Trophy>();
    for (Trophy t : this.trophies) {
      temp.add(t);
    }
    for (Target target : targets) {
      boolean exist = false;
      for (Trophy trophy : temp) {
        if (target.color.toLowerCase().equals(trophy.color)) {
          exist = true;
        }
      }
      if (!exist) {
        return true;
      }
    }
    return false;
  }

  // determine whether the trophy with the specific color exist
  boolean existSuchTrophy(String color) {
    for (Trophy t : this.trophies) {
      if (t.color.equals(color.toLowerCase())) {
        return true;
      }
    }
    return false;
  }

  // determines whether the player fell in the hole
  boolean playerDied(GridGround ground) {
    for (Hole h : ground.holes) {
      if (this.player.isInSamePlace(h)) {
        return true;
      }
    }
    return false;
  }

  // determines whether the cell at the indicated position contains an object
  boolean isNotEmptyAt(Posn pos) {
    Row rowOfObject = this.rows.get(pos.y);
    Cell object = rowOfObject.getObject(pos.x);
    return !object.isEmptyCell();
  }

  // finds the player in this grid and returns that player, if no player
  // is found, throw an exception
  Player findPlayer() {
    Player p = null;
    for (Row r : this.rows) {
      p = r.findPlayer();
      if (p != null) {
        return p;
      }
    }
    throw new RuntimeException("No player found");
  }
}

// a function that takes in a position and direction and returns the position 
// of the cell in that direction
class PosDirection {
  // gets the new position in the specified direction, otherwise
  // return the original position
  Posn posOfDirection(Posn pos, String direction) {
    Posn newPos = pos;
    if (direction.equals("up")) {
      newPos = new Posn(pos.x, pos.y - 1);
    }
    else if (direction.equals("down")) {
      newPos = new Posn(pos.x, pos.y + 1);
    }
    else if (direction.equals("left")) {
      newPos = new Posn(pos.x - 1, pos.y);
    }
    else if (direction.equals("right")) {
      newPos = new Posn(pos.x + 1, pos.y);
    }
    // check if new position is valid (none are negative)
    if (newPos.x < 0 || newPos.y < 0) {
      return pos;
    }
    else {
      return newPos;
    }
  }
}

// utility methods
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

  GridGround baseGrid;
  GridContent baseContent;
  ArrayList<Target> baseTargets;
  ArrayList<Trophy> baseTrophies;
  Player testPlayer;
  Row testRow1;
  Row testRow2;
  PosDirection dirUtil;

  String descriptionGround = "_______\n" + "__B____\n" + "____Y__\n" + "___R___\n" + "____G__\n"
      + "_____h_\n" + "_______";
  GridGround gridGround = new GridGround(descriptionGround);

  String descriptionContent = "WWWWWWW\n" + "W_____W\n" + "W_b>y_W\n" + "W_g_r_W\n" + "W___B_W\n"
      + "W_____W\n" + "WWWWWWW";
  GridContent gridContent = new GridContent(descriptionContent);

  Level exampleLevel = new Level(gridGround, gridContent);
  Game game = new Game(exampleLevel);

  void initData() {
    baseTargets = new ArrayList<Target>();
    baseTargets.add(new Target(new Posn(1, 1), "r"));
    baseTargets.add(new Target(new Posn(2, 2), "b"));
    baseTargets.add(new Target(new Posn(3, 3), "g"));

    baseTrophies = new ArrayList<Trophy>();
    baseTrophies.add(new Trophy(new Posn(1, 1), "r"));
    baseTrophies.add(new Trophy(new Posn(5, 5), "b"));
    baseTrophies.add(new Trophy(new Posn(3, 3), "y"));

    baseGrid = new GridGround();
    baseGrid.targets = baseTargets;

    baseContent = new GridContent();
    baseContent.trophies = baseTrophies;

    baseTargets = new ArrayList<>();
    baseTargets.add(new Target(new Posn(1, 1), "r"));
    baseTargets.add(new Target(new Posn(2, 2), "b"));
    baseTargets.add(new Target(new Posn(3, 3), "g"));

    baseTrophies = new ArrayList<>();
    baseTrophies.add(new Trophy(new Posn(1, 1), "r"));
    baseTrophies.add(new Trophy(new Posn(5, 5), "b"));
    baseTrophies.add(new Trophy(new Posn(3, 3), "y"));

    baseGrid = new GridGround();
    baseGrid.targets = baseTargets;

    baseContent = new GridContent();
    baseContent.trophies = baseTrophies;

    testPlayer = new Player(new Posn(0, 0), ">");
    ContentFactory factory = new ContentFactory();

    ArrayList<Cell> row1Cells = new ArrayList<>();
    row1Cells.add(factory.createCell(new Posn(0, 0), "_"));
    row1Cells.add(factory.createCell(new Posn(1, 0), ">"));
    row1Cells.add(factory.createCell(new Posn(2, 0), "_"));

    ArrayList<Cell> row2Cells = new ArrayList<>();
    row2Cells.add(factory.createCell(new Posn(0, 1), "b"));
    row2Cells.add(factory.createCell(new Posn(1, 1), "W"));
    row2Cells.add(factory.createCell(new Posn(2, 1), "_"));

    testRow1 = new Row(0, ">__", factory);
    testRow2 = new Row(1, "bW_", factory);

    baseContent.rows.add(testRow1);
    baseContent.rows.add(testRow2);
    baseContent.player = testPlayer;

    dirUtil = new PosDirection();
  }

  // tests the creation of a grid of ground cells
  void testCreateGridGround(Tester t) {
    String description = "___\n" + "B_Y\n" + "_G_";
    GroundFactory groundFactory = new GroundFactory();
    GridGround grid = new GridGround(description);
    Row row1 = new Row(0, "___", groundFactory);
    Row row2 = new Row(1, "B_Y", groundFactory);
    Row row3 = new Row(2, "_G_", groundFactory);
    ArrayList<Row> rows = new ArrayList<Row>();

    rows.add(row1);
    rows.add(row2);
    rows.add(row3);

    t.checkExpect(grid.rows, rows);
  }

  // tests the creation of a grid of content cells
  void testCreateGridContent(Tester t) {
    String description = "_W_\n" + "b>y\n" + "_gB";
    ContentFactory contentFactory = new ContentFactory();
    GridContent grid = new GridContent(description);
    Row row1 = new Row(0, "_W_", contentFactory);
    Row row2 = new Row(1, "b>y", contentFactory);
    Row row3 = new Row(2, "_gB", contentFactory);
    ArrayList<Row> rows = new ArrayList<Row>();
    rows.add(row1);
    rows.add(row2);
    rows.add(row3);
    t.checkExpect(grid.rows, rows);
  }

//  // draws grid content
//  boolean testDrawGridGround(Tester t) {
//    WorldCanvas c = new WorldCanvas(1250, 750);
//    WorldScene s = new WorldScene(1250, 750);
//    String description = "B_Y\n" + "___\n" + "_G_";
//    GridGround grid = new GridGround(description);
//    return c.drawScene(s.placeImageXY(grid.drawGrid(), 625, 375)) && c.show();
//  }
//
//  // draws grid content
//  boolean testDrawGridContent(Tester t) {
//    WorldCanvas c = new WorldCanvas(1250, 750);
//    WorldScene s = new WorldScene(1250, 750);
//    String description = "_W_\n" + "b>y\n" + "_gB";
//    GridContent grid = new GridContent(description);
//    return c.drawScene(s.placeImageXY(grid.drawGrid(), 625, 375)) && c.show();
//  }

  // testing utils methods

  // test that list of strings is created from one string
  void testCreatListOfString(Tester t) {
    Utils util = new Utils();
    String exampleLevelContents = "WWW_\n" + "W_WW\n" + "WWWr_WWW\n" + "W_b>yB_W\n" + "WW_gWWWW\n"
        + "_WWW__\n" + "WWW_";
    ILoString strings3 = new ConsLoString("WW_gWWWW",
        new ConsLoString("_WWW__", new ConsLoString("WWW_", new MtLoString())));
    ILoString strings2 = new ConsLoString("W_WW",
        new ConsLoString("WWWr_WWW", new ConsLoString("W_b>yB_W", strings3)));
    ILoString strings = new ConsLoString("WWW_", strings2);
    t.checkExpect(util.createListOfString(exampleLevelContents), strings);
  }

  String exampleLevelGround = "________\n" + "___R____\n" + "________\n" + "_B____Y_\n"
      + "________\n" + "___G____\n" + "________";
  String exampleLevelContent = "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb_>_ByW\n"
      + "WW__WWWW\n" + "_WWgW___\n" + "__WWW___";
  GroundFactory groundFactory = new GroundFactory();
  GridGround grid2 = new GridGround(exampleLevelGround);
  GridContent grid3 = new GridContent(exampleLevelContent);
  ArrayList<Row> r1 = grid2.rows;
  ArrayList<Row> r2 = grid3.rows;

  void testCreateGridGround2(Tester t) {
    String description = "___\n" + "B_Y\n" + "_G_";
    GroundFactory groundFactory = new GroundFactory();
    GridGround grid = new GridGround(description);
    Row row1 = new Row(0, "___", groundFactory);
    Row row2 = new Row(1, "B_Y", groundFactory);
    Row row3 = new Row(2, "_G_", groundFactory);
    ArrayList<Row> rows = new ArrayList<Row>();
    rows.add(row1);
    rows.add(row2);
    rows.add(row3);
    t.checkExpect(grid.rows, rows);
  }

  void testAllTargetsFilled(Tester t) {
    this.initData();
    t.checkExpect(baseGrid.allTargetsFilled(baseContent), false);

    baseContent.trophies.add(new Trophy(new Posn(3, 3), "g"));
    baseContent.trophies.add(new Trophy(new Posn(2, 2), "b"));
    t.checkExpect(baseGrid.allTargetsFilled(baseContent), true);

    baseContent.trophies.remove(0);
    t.checkExpect(baseGrid.allTargetsFilled(baseContent), false);
  }

  void testNoEnoughtTrophies(Tester t) {
    this.initData();
    t.checkExpect(baseGrid.noEnoughtTrophies(baseContent), true);

    baseContent.trophies.add(new Trophy(new Posn(0, 0), "g"));
    t.checkExpect(baseGrid.noEnoughtTrophies(baseContent), false);

    baseContent.trophies.remove(0);
    t.checkExpect(baseGrid.noEnoughtTrophies(baseContent), true);
  }

  void testExistTargetTrophy(Tester t) {
    this.initData();
    t.checkExpect(baseContent.existTargetTrophy(new Posn(1, 1), "r"), true);

    t.checkExpect(baseContent.existTargetTrophy(new Posn(3, 3), "b"), false);

    t.checkExpect(baseContent.existTargetTrophy(new Posn(2, 2), "b"), false);
  }

  void testNoEnoughtTrophiesHelp(Tester t) {
    this.initData();
    ArrayList<Target> subTargets = new ArrayList<>();
    subTargets.add(baseTargets.get(0));
    subTargets.add(baseTargets.get(1));
    t.checkExpect(baseContent.noEnoughtTrophiesHelp(subTargets), false);

    subTargets.add(new Target(new Posn(4, 4), "y"));
    t.checkExpect(baseContent.noEnoughtTrophiesHelp(subTargets), false);

    baseContent.trophies.remove(1);
    t.checkExpect(baseContent.noEnoughtTrophiesHelp(subTargets), true);
  }

  void testExistSuchTrophy(Tester t) {
    this.initData();
    t.checkExpect(baseContent.existSuchTrophy("r"), true);

    t.checkExpect(baseContent.existSuchTrophy("x"), false);

    baseContent.trophies.add(new Trophy(new Posn(6, 6), "y"));
    t.checkExpect(baseContent.existSuchTrophy("y"), true);
  }

  void testMovePlayerBasic(Tester t) {
    this.initData();
    baseContent.movePlayer("up");
    t.checkExpect(testPlayer.getPosition(), new Posn(0, 0));
    baseContent.movePlayer("right");
    t.checkExpect(testPlayer.getPosition(), new Posn(1, 0));
    baseContent.movePlayer("right");
    baseContent.movePlayer("down");
    t.checkExpect(testPlayer.getPosition(), new Posn(2, 1));
    t.checkExpect(this.game.lvl.contentGrid.player.getPosition(), new Posn(3, 3));
  }

  void testCanMove(Tester t) {
    this.initData();
    t.checkExpect(baseContent.canMove(new Posn(0, 0), "right"), true);
    t.checkExpect(baseContent.canMove(new Posn(1, 1), "down"), false);
  }

  void testPosOfDirection(Tester t) {
    this.initData();
    t.checkExpect(dirUtil.posOfDirection(new Posn(1, 1), "right"), new Posn(2, 1));
    t.checkExpect(dirUtil.posOfDirection(new Posn(0, 0), "left"), new Posn(0, 0));
    t.checkExpect(dirUtil.posOfDirection(new Posn(1, 1), "invalid"), new Posn(1, 1));
  }

  /*
   * void testLevelWon(Tester t) { t.checkExpect(grid2.allTargetsFilled(grid3),
   * false); }
   */
}