import tester.*; // The tester library
import javalib.worldimages.*;
import javalib.funworld.*;
import javalib.worldcanvas.WorldCanvas;
import java.util.ArrayList;

// to represent a row in the grid
class Row {
  // the cells of this row
  ArrayList<Cell> cells;
  // the number of this row
  int rowNumber;

  // the constructor
  Row() {
    this.cells = new ArrayList<Cell>();
    rowNumber = 0;
  }

  // clone the row
  Row(Row other) {
    this.cells = new ArrayList<>();
    for (Cell cell : other.cells) {
      this.cells.add(cell.copy());
    }
    this.rowNumber = other.rowNumber;
  }

  // the constructor that makes the row based on description and type of grid
  Row(int rowNumber, String description, CellFactory factory) {
    this.cells = new ArrayList<Cell>();
    this.rowNumber = rowNumber;
    createRowCells(description, factory, 0);
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.cells ...            -- ArrayList<Cell> 
   * ... this.rowNumber ...        -- int
   * 
   * Methods: 
   * ... draw() ...                    -- WorldImage
   * ... createRowCells() ...          -- ArrayList<Cell>
   * ... getObject(int index)          -- Cell
   * ... set(int index, Cell object)   -- void
   * ... addToTargetList(ArrayList<Target> targets) ...     -- void
   * ... addToHoleList(ArrayList<Hole> holes) ...           -- void
   * ... addToTrophyList(ArrayList<Trophy> trophies) ...    -- void
   * ... findPlayer() ...              -- Player
   * 
   * Methods for fields: 
   * ... this.cells.drawCells() ...                  -- WorldImage
   * ... this.cells.drawCells(WorldImage row) ...    -- WorldImage
   * ... this.cells.hasEmptyCell(Posn pos) ...       -- boolean
   * ... this.cells.hasEmptyCell(int index) ...      -- boolean
   */

  // draws the cells of this row
  WorldImage drawRow() {
    // for every cell in the list, stack the cell image on top of each other
    WorldImage row = new EmptyImage();
    for (Cell c : cells) {
      row = new BesideImage(row, c.draw());
    }
    return row;
  }

  // creates the list of cells of this row
  void createRowCells(String description, CellFactory factory, int count) {
    if (count == description.length()) {
      return;
    } else {
      Cell cell = factory.createCell(new Posn(count, rowNumber),
          description.substring(count, count + 1));
      this.cells.add(cell);
      this.createRowCells(description, factory, count + 1);
    }
  }

  // returns the object at the specified index in the arraylist
  Cell getObject(int index) {
    return this.cells.get(index);
  }

  // sets the object at the specified index as the given object
  void set(int index, Cell object) {
    this.cells.set(index, object);
  }

  // if cell is a target, adds it to the given list
  void addToTargetList(ArrayList<Target> targets) {
    for (Cell c : this.cells) {
      c.addToTargetList(targets);
    }
  }

  // if cell is a hole, adds it to the given list
  void addToHoleList(ArrayList<Hole> holes) {
    for (Cell c : this.cells) {
      c.addToHoleList(holes);
    }
  }

  // if cell is a trophy, adds it to the given list
  void addToTrophyList(ArrayList<Trophy> trophies) {
    for (Cell c : this.cells) {
      c.addToTrophyList(trophies);
    }
  }
  
  // if cell is an ice, adds it to the given list
  void addToIceList(ArrayList<Ice> ices) {
    for (Cell c : this.cells) {
      c.addToIceList(ices);
    }
  }

  // finds the player in this row of cells
  // if no player is found, return null
  Player findPlayer() {
    Player p = null;
    for (Cell c : this.cells) {
      p = c.findPlayer();
      if (p != null) {
        return p;
      }
    }
    return p;
  }
}

class ExamplesRow {
  // tests the creation of a row of ground cells
  void testCreateRowGround(Tester t) {
    String rowDescription = "___R____";
    GroundFactory groundFactory = new GroundFactory();
    Row row = new Row(0, rowDescription, groundFactory);
    Cell blank0 = new BlankGround(new Posn(0, 0));
    Cell blank1 = new BlankGround(new Posn(0, 1));
    Cell blank2 = new BlankGround(new Posn(0, 2));
    Cell blank4 = new BlankGround(new Posn(0, 4));
    Cell blank5 = new BlankGround(new Posn(0, 5));
    Cell blank6 = new BlankGround(new Posn(0, 6));
    Cell blank7 = new BlankGround(new Posn(0, 7));
    Cell redTarget = new Target(new Posn(0, 3), "R");
    ArrayList<Cell> expectedRow = new ArrayList<Cell>();
    expectedRow.add(blank0);
    expectedRow.add(blank1);
    expectedRow.add(blank2);
    expectedRow.add(redTarget);
    expectedRow.add(blank4);
    expectedRow.add(blank5);
    expectedRow.add(blank6);
    expectedRow.add(blank7);
    t.checkExpect(row.cells, expectedRow);
  }

  // tests the creation of a row of content cells
  boolean testCreateRowContent(Tester t) {
    String rowDescription = "W_b>yB_W";
    ContentFactory contentFactory = new ContentFactory();
    Row row = new Row(1, rowDescription, contentFactory);
    Cell wall1 = new BrickWall(new Posn(1, 0));
    Cell blank1 = new BlankCell(new Posn(1, 1));
    Cell blueTrophy = new Trophy(new Posn(1, 2), "b");
    Cell playerRight = new Player(new Posn(1, 3), ">");
    Cell yellowTrophy = new Trophy(new Posn(1, 4), "y");
    Cell crate = new Crate(new Posn(1, 5));
    Cell blank2 = new BlankCell(new Posn(1, 6));
    Cell wall2 = new BrickWall(new Posn(1, 7));
    ArrayList<Cell> arrCells = new ArrayList<Cell>();
    arrCells.add(wall1);
    arrCells.add(blank1);
    arrCells.add(blueTrophy);
    arrCells.add(playerRight);
    arrCells.add(yellowTrophy);
    arrCells.add(crate);
    arrCells.add(blank2);
    arrCells.add(wall2);
    return t.checkExpect(row.cells, arrCells);
  }

  // draws row content
  boolean testDrawRowGround(Tester t) {
    WorldCanvas c = new WorldCanvas(1250, 750);
    WorldScene s = new WorldScene(1250, 750);
    String rowDescription = "___R____";
    GroundFactory groundFactory = new GroundFactory();
    Row row1 = new Row(0, rowDescription, groundFactory);
    return c.drawScene(s.placeImageXY(row1.drawRow(), 625, 375)) && c.show();
  }

  // draws row content
  boolean testDrawRowContent(Tester t) {
    WorldCanvas c = new WorldCanvas(1250, 750);
    WorldScene s = new WorldScene(1250, 750);
    String rowDescription = "W_b>yB_W";
    ContentFactory contentFactory = new ContentFactory();
    Row row1 = new Row(0, rowDescription, contentFactory);
    return c.drawScene(s.placeImageXY(row1.drawRow(), 625, 375)) && c.show();
  }
}
