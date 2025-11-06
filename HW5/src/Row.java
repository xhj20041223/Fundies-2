import tester.*; // The tester library
import javalib.worldimages.*;

// to represent a row in the grid
class Row {
  // the cells of this row
  ILoCell cells;
  // the number of this row
  int rowNumber;

  // the constructor
  Row() {
    cells = new MtLoCell();
    rowNumber = 0;
  }

  // the constructor that makes the row based on description and type of grid
  Row(int rowNumber, String description, CellFactory factory) {
    this.cells = createRowCells(description, factory, 0);
    this.rowNumber = rowNumber;
  }

  /*
   * TEMPLATE: Fields: ... this.cells ... -- ILoCell ... this.rowNumber ... -- int
   * 
   * Methods: ... draw() ... -- WorldImage ... createRowCells() ... -- ILoCell ...
   * hasEmptyCell(Posn position) -- boolean
   * 
   * Methods for fields: ... this.cells.drawCells() ... -- WorldImage ...
   * this.cells.drawCells(WorldImage row) ... -- WorldImage ...
   * this.cells.hasEmptyCell(Posn pos) ... -- boolean ...
   * this.cells.hasEmptyCell(int index) ... -- boolean
   */

  // draws the cells of this row
  WorldImage drawRow() {
    return cells.drawCells();
  }

  // creates the list of cells of this row
  ILoCell createRowCells(String description, CellFactory factory, int count) {
    //
    if (count == description.length()) {
      return new MtLoCell();
    }
    else {
      Content cell = factory.createCell(new Posn(rowNumber, count),
          description.substring(count, count + 1));
      return new ConsLoCell(cell, createRowCells(description, factory, count + 1));
    }
  }

  // checks whether the cell at the specified position is empty
  boolean hasEmptyCell(Posn position) {
    return this.cells.hasEmptyCell(position);
  }

  // checks whether each target in the row has the
  // same color trophy
  boolean sameColorTargetTrophyRow(Row r) {
    return this.cells.sameColorTargetTrophyCell(r.cells);
  }
}

class ExamplesRow {
  // tests the creation of rows
  boolean testCreateRow(Tester t) {
    String rowDescription = "___R____";
    GroundFactory groundFactory = new GroundFactory();
    Row row1 = new Row(0, rowDescription, groundFactory);
    Content blank0 = new BlankGround(new Posn(0, 0));
    Content blank1 = new BlankGround(new Posn(0, 1));
    Content blank2 = new BlankGround(new Posn(0, 2));
    Content blank4 = new BlankGround(new Posn(0, 4));
    Content blank5 = new BlankGround(new Posn(0, 5));
    Content blank6 = new BlankGround(new Posn(0, 6));
    Content blank7 = new BlankGround(new Posn(0, 7));
    Content redTarget = new Target(new Posn(0, 3), "R");
    ILoCell fourBlanks = new ConsLoCell(blank4,
        new ConsLoCell(blank5, new ConsLoCell(blank6, new ConsLoCell(blank7, new MtLoCell()))));
    ILoCell expectedRow = new ConsLoCell(blank0,
        new ConsLoCell(blank1, new ConsLoCell(blank2, new ConsLoCell(redTarget, fourBlanks))));
    return t.checkExpect(row1.cells, expectedRow);
  }
}