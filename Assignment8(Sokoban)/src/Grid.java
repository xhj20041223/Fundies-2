import tester.*; // The tester library
import javalib.worldimages.*;
import java.util.ArrayList;

// to represent placement of objects in a Sokoban level
abstract class Grid {

  // the rows of this grid with ground content
  ArrayList<Row> rows;
  
  // determine whether the player is alive or not
  boolean playerAlive = true;

  // the constructor
  Grid() {
    this.rows = new ArrayList<Row>();
  }

  // constructor that takes in a list of lists
  Grid(ArrayList<Row> rows) {
    this.rows = rows;
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.rows ...            -- ArrayList<Row> 
   * ... this.playerAlive ...     -- boolean
   * 
   * Methods: 
   * ... drawGrid() ...                                                     -- WorldImage
   * ... createRows(ILoString rowDescriptions, CellFactory factory) ...     -- ArrayList<Row>
   * ... setCellAs(Posn pos, Cell c) ...                                    -- void
   * 
   * Methods for fields: 
   * ... this.rows.draw() ...                    -- WorldImage
   * ... this.rows.createRowCells() ...          -- ArrayList<Cell>
   * ... this.rows.getObject(int index)          -- Cell
   * ... this.rows.set(int index, Cell object)   -- void
   * ... this.rows.addToTargetList(ArrayList<Target> targets) ...     -- void
   * ... this.rows.addToHoleList(ArrayList<Hole> holes) ...           -- void
   * ... this.rows.addToTrophyList(ArrayList<Trophy> trophies) ...    -- void
   * ... this.rows.findPlayer() ...              -- Player
   */

  // draws this grid
  WorldImage drawGrid() {
    //return rows.drawRows();
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
  public GridGround(ArrayList<Target> targets, ArrayList<Hole> holes, ArrayList<Ice> ices) {
    this.targets = targets;
    this.holes = holes;
    this.ices = ices;
  }

  // clone the GridGround
  GridGround(GridGround other) {
    this.targets = new ArrayList<>();
    for (Target t : other.targets) {
      this.targets.add((Target) t.copy());
    }
    this.holes = new ArrayList<>();
    for (Hole h : other.holes) {
      this.holes.add((Hole) h.copy());
    }
    this.ices = new ArrayList<>();
    for (Ice i : other.ices) {
      this.ices.add((Ice) i.copy());
    }
    this.rows = new ArrayList<>();
    for (Row row : other.rows) {
      this.rows.add(new Row(row));
    }
  }

  // list of targets
  ArrayList<Target> targets;

  // list of holes
  ArrayList<Hole> holes;
  
  // list of ices
  ArrayList<Ice> ices;

  // default constructor
  GridGround() {
    // create blank row with blank content
    this.targets = new ArrayList<Target>();
    this.holes = new ArrayList<Hole>();
    this.ices = new ArrayList<Ice>();
  }

  // convenience constructor that takes a String description
  GridGround(String description) {
    // utils class
    Utils util = new Utils();
    this.targets = new ArrayList<Target>();
    this.holes = new ArrayList<Hole>();
    this.ices = new ArrayList<Ice>();
    // specifies to create ground cells
    GroundFactory groundFactory = new GroundFactory();
    ILoString rowDescriptions = util.createListOfString(description);
    this.rows = createRows(rowDescriptions, groundFactory);

    for (Row r : this.rows) {
      r.addToTargetList(targets);
      r.addToHoleList(holes);
      r.addToIceList(ices);
    }
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.rows ...            -- ArrayList<Row> 
   * ... this.targets ...         -- ArrayList<Target>
   * ... this.holes ...           -- ArrayList<Hole>
   * 
   * Methods: 
   * ... drawGrid() ...                                                     -- WorldImage
   * ... createRows(ILoString rowDescriptions, CellFactory factory) ...     -- ArrayList<Row> 
   * ... allTargetsFilled(GridContent contents) ...                         -- boolean
   * ... noEnoughtTrophies(GridContent contents) ...                        -- boolean
   * ... isOnHole(CellContent c) ...                                        -- boolean 
   * ... removeObjectsOnHoles(GridContent content) ...                      -- void
   * 
   * Methods for fields: 
   * ... this.rows.draw() ...                    -- WorldImage
   * ... this.rows.createRowCells() ...          -- ArrayList<Cell>
   * ... this.rows.getObject(int index)          -- Cell
   * ... this.rows.set(int index, Cell object)   -- void
   * ... this.rows.addToTargetList(ArrayList<Target> targets) ...     -- void
   * ... this.rows.addToHoleList(ArrayList<Hole> holes) ...           -- void
   * ... this.rows.addToTrophyList(ArrayList<Trophy> trophies) ...    -- void
   * ... this.rows.findPlayer() ...              -- Player
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
      Row currentRow = content.rows.get(holePos.y);
      CellContent currentCell = (CellContent) currentRow.getObject(holePos.x);
      if (content.isNotEmptyAt(holePos)) {
        if (currentCell instanceof Player) {
          this.playerAlive = false;
        }
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
  // list of ices
  ArrayList<Ice> ices = new ArrayList<Ice>();

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
  
  // clone the GridContent
  GridContent(GridContent other) {
    this.rows = new ArrayList<>();
    for (Row row : other.rows) {
      this.rows.add(new Row(row));
    }
    this.ices = new ArrayList<>();
    for (Ice ice : other.ices) {
      this.ices.add((Ice) ice.copy());
    }
    this.trophies = new ArrayList<>();
    for (Row row : this.rows) {
      row.addToTrophyList(this.trophies);
    }
    this.player = this.findPlayer();
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.rows ...            -- ArrayList<Row> 
   * ... this.player ...          -- Player
   * ... this.trophies ...        -- ArrayList<Trophy>
   * 
   * Methods: 
   * ... drawGrid() ...                                                     -- WorldImage
   * ... createRows(ILoString rowDescriptions, CellFactory factory) ...     -- ArrayList<Row> 
   * ... movePlayer(String direction) ...                                   -- void
   * ... moveContent(Movable content, String direction) ...                 -- void
   * ... swapContents(Posn pos1, Posn pos2) ...                             -- void 
   * ... removeObjectsOnHoles(GridContent content) ...                      -- void
   * ... canMove(Posn objPos, String direction) ...                         -- boolean
   * ... existTargetTrophy(Posn posn, String color) ...                     -- boolean
   * ... noEnoughtTrophiesHelp(ArrayList<Target> targets) ...               -- boolean
   * ... existSuchTrophy(String color) ...                                  -- boolean
   * ... isNotEmptyAt(Posn pos) ...                                         -- boolean
   * ... findPlayer() ...                                                   -- Player
   * ... onIce(Posn) ...                                                    -- boolean
   * 
   * Methods for fields: 
   * ... this.rows.draw() ...                    -- WorldImage
   * ... this.rows.createRowCells() ...          -- ArrayList<Cell>
   * ... this.rows.getObject(int index)          -- Cell
   * ... this.rows.set(int index, Cell object)   -- void
   * ... this.rows.addToTargetList(ArrayList<Target> targets) ...     -- void
   * ... this.rows.addToHoleList(ArrayList<Hole> holes) ...           -- void
   * ... this.rows.addToTrophyList(ArrayList<Trophy> trophies) ...    -- void
   * ... this.rows.findPlayer() ...              -- Player
   */
  
  // will increase the count if the player really move and drive the
  // player to move
  boolean moveDriver(String direction) {
    if (this.canMove(this.player.getPosition(), direction)) {
      this.movePlayer(direction);
      return true;
    }
    return false;
  }

  // EFFECT: moves the player of this grid in the specified direction if possible
  void movePlayer(String direction) {
    if (!this.canMove(this.player.getPosition(), direction)) {
      return;
    }
    Posn currentPos = this.player.getPosition();
    PosDirection d = new PosDirection();
    Posn newPos = d.posOfDirection(currentPos, direction);
    Row newRow = this.rows.get(newPos.y);
    CellContent newCell = (CellContent) newRow.getObject(newPos.x);

    if (this.onIce(newPos) && newCell.isEmptyCell()) {
      player.move(newPos);
      this.swapContents(currentPos, newPos);
      player.changeDirection(direction);
      this.movePlayer(direction);
      return;
    }
    
    else if (newCell instanceof Crate) {
      Posn behindPos = new Posn(newPos.x, newPos.y);
      behindPos = d.posOfDirection(behindPos, direction);
      if (this.onIce(newPos) || this.onIce(behindPos)) {
        this.slide(newPos, direction);
      }
      else {
        Crate crate = (Crate) newCell;
        crate.move(behindPos);
        this.swapContents(d.posOfDirection(currentPos, direction), behindPos);
      }
    }
    else if (newCell instanceof Trophy) {
      Posn behindPos = new Posn(newPos.x, newPos.y);
      behindPos = d.posOfDirection(behindPos, direction);
      if (this.onIce(newPos) || this.onIce(behindPos)) {
        this.slide(newPos, direction);
      }
      else {
        Trophy crate = (Trophy) newCell;
        crate.move(behindPos);
        this.swapContents(d.posOfDirection(currentPos, direction), behindPos);
      }
    }
    
    player.move(newPos);
    this.swapContents(currentPos, newPos);

    player.changeDirection(direction);
  }

  
  // EFFECT: moves the given object of this grid in the specified direction if possible
  void moveContent(Movable content, String direction) {
    PosDirection d = new PosDirection();
    // the position of the movable object
    Posn contentPos = content.getPosition();
    // the position to move to
    Posn newPos = d.posOfDirection(contentPos, direction);

    // check if next space is empty
    if (this.canMove(contentPos, direction)) {
      //content.move(newPos);
      this.swapContents(contentPos, newPos);
    }
    // if not, check if next space is movable object next to empty space
    else if (this.canMove(newPos, direction)) {
      // change position of content
      //content.move(newPos);
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

    Row newRow = rows.get(newPos.y);
    CellContent newCell = (CellContent) newRow.getObject(newPos.x);

    if (newCell.isEmptyCell()) {
      return true;
    }
    else if (newCell instanceof Crate  || newCell instanceof Trophy) {
      Posn behindPos = d.posOfDirection(newPos, direction);
      Row behindRow = rows.get(behindPos.y);
      if (behindRow.getObject(behindPos.x).isEmptyCell()) {
        return true;
      }
      if (this.onIce(newPos)) {
        return this.canMove(newPos, direction) && this.onIce(behindPos);
      }
      if (this.onIce(behindPos)) {
        return this.canMove(behindPos, direction);
      }
    }
    return false;
  }


  // checks whether the object in the specified position in this grid can 
  // slide to the position of the specified direction
  boolean canSlide(Posn objPos, String direction) {
    Row currentRow = rows.get(objPos.y);

    CellContent currentCell = (CellContent) currentRow.getObject(objPos.x);
    if (!currentCell.canMove()) {
      return false;
    }

    PosDirection d = new PosDirection();
    Posn newPos = d.posOfDirection(objPos, direction);

    Row newRow = rows.get(newPos.y);
    CellContent newCell = (CellContent) newRow.getObject(newPos.x);

    return newCell.isEmptyCell();
  }
  
  //EFFECT: let an object slide alone the ices till the end or hit other objects
  void slide(Posn pos, String direction) {

    PosDirection d = new PosDirection();
    Posn newPos = d.posOfDirection(pos, direction);

    Row newRow = rows.get(newPos.y);
    CellContent newCell = (CellContent) newRow.getObject(newPos.x);
    if (newCell.isEmptyCell()) {
      newCell.move(newPos);
      this.swapContents(pos, newPos);
      if (this.onIce(newPos)) {
        this.slide(newPos, direction);        
      }
    }
    else if (newCell.canMove()) {
      if (this.onIce(newPos)) {
        this.slide(newPos, direction);
        this.decelerateSlide(pos, direction);
      }
      else if (this.canSlide(newPos, direction)) {
        this.moveContent((Movable) newCell, direction);
      }
    }
  }
  
  //EFFECT: let an object slide alone the ices till the end or hit other objects
  // this slide is decelerated so it can move nothing but itself
  void decelerateSlide(Posn pos, String direction) {

    PosDirection d = new PosDirection();
    Posn newPos = d.posOfDirection(pos, direction);

    Row newRow = rows.get(newPos.y);
    CellContent newCell = (CellContent) newRow.getObject(newPos.x);
    if (newCell.isEmptyCell()) {
      newCell.move(newPos);
      this.swapContents(pos, newPos);
      if (this.onIce(newPos)) {
        this.decelerateSlide(newPos, direction);        
      }
    }
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
        if (target.color.equalsIgnoreCase(trophy.color)) {
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
  
  // determine whether the content on that posn is on ice
  boolean onIce(Posn pos) {
    for (Ice i : this.ices) {
      if (i.hasPosition(pos)) {
        return true;
      }
    }
    return false;
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

  void testFindPlayer(Tester t) {
    t.checkExpect(gridContent.findPlayer().getPosition(), new Posn(3, 2));
  }

  void initData() {
    baseTargets = new ArrayList<Target>();
    baseTargets.add(new Target(new Posn(1,1), "r"));
    baseTargets.add(new Target(new Posn(2,2), "b"));
    baseTargets.add(new Target(new Posn(3,3), "g"));

    baseTrophies = new ArrayList<Trophy>();
    baseTrophies.add(new Trophy(new Posn(1,1), "r"));
    baseTrophies.add(new Trophy(new Posn(5,5), "b"));
    baseTrophies.add(new Trophy(new Posn(3,3), "y"));

    baseGrid = new GridGround();
    baseGrid.targets = baseTargets;

    baseContent = new GridContent();
    baseContent.trophies = baseTrophies;
    

    baseTargets = new ArrayList<>();
    baseTargets.add(new Target(new Posn(1,1), "r"));
    baseTargets.add(new Target(new Posn(2,2), "b"));
    baseTargets.add(new Target(new Posn(3,3), "g"));

    baseTrophies = new ArrayList<>();
    baseTrophies.add(new Trophy(new Posn(1,1), "r"));
    baseTrophies.add(new Trophy(new Posn(5,5), "b"));
    baseTrophies.add(new Trophy(new Posn(3,3), "y"));

    baseGrid = new GridGround();
    baseGrid.targets = baseTargets;

    baseContent = new GridContent();
    baseContent.trophies = baseTrophies;

    testPlayer = new Player(new Posn(0, 0), ">");
    ContentFactory factory = new ContentFactory();
    
    ArrayList<Cell> row1Cells = new ArrayList<>();
    row1Cells.add(factory.createCell(new Posn(0,0), "_"));
    row1Cells.add(factory.createCell(new Posn(1,0), ">"));
    row1Cells.add(factory.createCell(new Posn(2,0), "_")); 

    ArrayList<Cell> row2Cells = new ArrayList<>();
    row2Cells.add(factory.createCell(new Posn(0,1), "b"));
    row2Cells.add(factory.createCell(new Posn(1,1), "W"));
    row2Cells.add(factory.createCell(new Posn(2,1), "_"));

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

  String exampleLevelGround = 
      "________\n" +
      "___R____\n" +
      "________\n" +
      "_B____Y_\n" +
      "________\n" +
      "___G____\n" +
      "________";
  String exampleLevelContent = 
      "__WWW___\n" +
      "__W_WW__\n" +
      "WWWr_WWW\n" +
      "Wb_>_ByW\n" +
      "WW__WWWW\n" +
      "_WWgW___\n" +
      "__WWW___";
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

    baseContent.trophies.add(new Trophy(new Posn(3,3), "g"));
    baseContent.trophies.add(new Trophy(new Posn(2,2), "b"));
    t.checkExpect(baseGrid.allTargetsFilled(baseContent), true);

    baseContent.trophies.remove(0);
    t.checkExpect(baseGrid.allTargetsFilled(baseContent), false);
  }

  void testNoEnoughtTrophies(Tester t) {
    this.initData();
    t.checkExpect(baseGrid.noEnoughtTrophies(baseContent), true);

    baseContent.trophies.add(new Trophy(new Posn(0,0), "g"));
    t.checkExpect(baseGrid.noEnoughtTrophies(baseContent), false);

    baseContent.trophies.remove(0);
    t.checkExpect(baseGrid.noEnoughtTrophies(baseContent), true);
  }

  void testExistTargetTrophy(Tester t) {
    this.initData();
    t.checkExpect(baseContent.existTargetTrophy(new Posn(1,1), "r"), true);

    t.checkExpect(baseContent.existTargetTrophy(new Posn(3,3), "b"), false);

    t.checkExpect(baseContent.existTargetTrophy(new Posn(2,2), "b"), false);
  }

  void testNoEnoughtTrophiesHelp(Tester t) {
    this.initData();
    ArrayList<Target> subTargets = new ArrayList<>();
    subTargets.add(baseTargets.get(0));
    subTargets.add(baseTargets.get(1));
    t.checkExpect(baseContent.noEnoughtTrophiesHelp(subTargets), false);

    subTargets.add(new Target(new Posn(4,4), "y"));
    t.checkExpect(baseContent.noEnoughtTrophiesHelp(subTargets), false);

    baseContent.trophies.remove(1);
    t.checkExpect(baseContent.noEnoughtTrophiesHelp(subTargets), true);
  }

  void testExistSuchTrophy(Tester t) {
    this.initData();
    t.checkExpect(baseContent.existSuchTrophy("r"), true);

    t.checkExpect(baseContent.existSuchTrophy("x"), false);

    baseContent.trophies.add(new Trophy(new Posn(6,6), "y"));
    t.checkExpect(baseContent.existSuchTrophy("y"), true);
  }
  

  void testMovePlayerBasic(Tester t) {
    this.initData();
    baseContent.movePlayer("up");
    t.checkExpect(testPlayer.getPosition(), new Posn(0,0));
    baseContent.movePlayer("right");
    t.checkExpect(testPlayer.getPosition(), new Posn(1, 0));
    baseContent.movePlayer("right");
    baseContent.movePlayer("down");
    t.checkExpect(testPlayer.getPosition(), new Posn(2, 1));
    t.checkExpect(this.game.lvl.contentGrid.player.getPosition(), new Posn(3, 2));
  }

  void testCanMove(Tester t) {
    this.initData();
    t.checkExpect(baseContent.canMove(new Posn(0,0), "right"), true);
    t.checkExpect(baseContent.canMove(new Posn(1,1), "down"), false);
  }

  void testPosOfDirection(Tester t) {
    this.initData();
    t.checkExpect(dirUtil.posOfDirection(new Posn(1,1), "right"), new Posn(2,1));
    t.checkExpect(dirUtil.posOfDirection(new Posn(0,0), "left"), new Posn(0,0));
    t.checkExpect(dirUtil.posOfDirection(new Posn(1,1), "invalid"), new Posn(1,1));
  }
  
  void testOnIce(Tester t) {
    String descGround = "_______\n" + "__i_i__\n" + "_______";
    String descContent = "_______\n" + "___>___\n" + "_______";
    Level level = new Level(descGround, descContent);
    t.checkExpect(level.contentGrid.onIce(new Posn(2, 1)), true);
    t.checkExpect(level.contentGrid.onIce(new Posn(3, 1)), false);
    t.checkExpect(level.contentGrid.onIce(new Posn(4, 1)), true);
  }
  
  void testSlide(Tester t) {
    String descGround = "_________\n" + "__i_i___\n" + "_________";
    String descContent = "_________\n" + "_>_b__W_\n" + "_________";
    Level level = new Level(descGround, descContent);

    level.contentGrid.slide(new Posn(3, 1), "right");
    t.checkExpect(level.contentGrid.rows.get(1).cells.get(4) instanceof Trophy, false);
    t.checkExpect(level.contentGrid.rows.get(1).cells.get(3).isEmptyCell(), true);
  }
  
  void testCanSlide(Tester t) {
    String descGround = "_________\n" + "__i_i___\n" + "_________";
    String descContent = "_________\n" + "_>_b__W_\n" + "_________";
    Level level = new Level(descGround, descContent);
    t.checkExpect(level.contentGrid.canSlide(new Posn(3, 1), "right"), true);
    t.checkExpect(level.contentGrid.canSlide(new Posn(4, 1), "right"), false);
    t.checkExpect(level.contentGrid.canSlide(new Posn(3, 1), "left"), true);
  }
}