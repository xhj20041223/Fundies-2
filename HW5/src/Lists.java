import javalib.worldimages.*;

// list of cells
interface ILoCell {
  // draws the cells of this list
  WorldImage drawCells();

  // helper for drawing the cells, accumulates the images
  WorldImage drawCells(WorldImage row);

  // checks if there is an empty cell at the given location
  boolean hasEmptyCell(Posn pos);

  // keeps track of row count for empty cell checker
  boolean hasEmptyCell(int index);
  
  // checks if the ILoCell is the same color as the trophy/target cell
  boolean sameColorTargetTrophyCell(ILoCell c);

  boolean sameColorTargetTrophyCellMtLoCell(MtLoCell c);

  boolean sameColorTargetTrophyCellConsLoCell(ConsLoCell c);
}

// empty list of cells
class MtLoCell implements ILoCell {
  // draws this empty cell
  public WorldImage drawCells() {
    return new EmptyImage();
  }

  // draws the accumulated row cell image
  public WorldImage drawCells(WorldImage row) {
    return row;
  }

  // checks if there is an empty cell at the given location of this empty list
  public boolean hasEmptyCell(Posn pos) {
    return false;
  }

  // keeps track of row count for empty cell checker
  public boolean hasEmptyCell(int index) {
    // reached end of list
    return false;
  }

  public boolean sameColorTargetTrophyCell(ILoCell c) {
    return c.sameColorTargetTrophyCellMtLoCell(this);
  }

  public boolean sameColorTargetTrophyCellMtLoCell(MtLoCell c) {
    return true;
  }

  public boolean sameColorTargetTrophyCellConsLoCell(ConsLoCell c) {
    return false;
  }
}

// non-empty list of cells
class ConsLoCell implements ILoCell {
  // the cell of this element
  Content first;
  // the rest of the list
  ILoCell rest;

  // the constructor
  ConsLoCell(Content first, ILoCell rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * TEMPLATE: Fields: ... this.first ... -- Content ... this.rest ... -- ILoCell
   * 
   * Methods: ... drawCells() ... -- WorldImage ... drawCells(WorldImage row) ...
   * -- WorldImage ... hasEmptyCell(Posn pos) ... -- boolean ... hasEmptyCell(int
   * index) ... -- boolean
   * 
   * Methods for fields: ... this.first.draw() ... -- WorldImage ...
   * this.rest.drawCells() ... -- WorldImage ... this.rest.drawCells(WorldImage
   * row) ... -- WorldImage ... this.rest.hasEmptyCell(Posn pos) ... -- boolean
   * ... this.rest.hasEmptyCell(int index) ... -- boolean
   */

  // draws this list of cells
  public WorldImage drawCells() {
    return drawCells(new EmptyImage());
  }

  // draws the accumulated row cell image
  public WorldImage drawCells(WorldImage row) {
    return this.rest.drawCells(new BesideImage(row, this.first.draw()));
  }

  // checks if there is an empty cell at the given location of this empty list
  public boolean hasEmptyCell(Posn pos) {
    return hasEmptyCell(pos.x);
  }

  // keeps track of row count for empty cell checker
  public boolean hasEmptyCell(int index) {
    if (index == 0) {
      return this.first.isEmptyCell();
    }
    else {
      return this.rest.hasEmptyCell(index - 1);
    }
  }

  public boolean sameColorTargetTrophyCell(ILoCell c) {
    return c.sameColorTargetTrophyCellConsLoCell(this);
  }

  public boolean sameColorTargetTrophyCellMtLoCell(MtLoCell c) {
    return true;
  }

  public boolean sameColorTargetTrophyCellConsLoCell(ConsLoCell c) {
    if (this.first.sameColorTargetTrophy(c.first)) {
      return this.rest.sameColorTargetTrophyCell(c.rest);
    }
    else {
      return false;
    }
  }
}

// list of rows
interface ILoRow {
  // draws the rows of this list
  WorldImage drawRows();

  // helper for drawing the rows, accumulates the images
  WorldImage drawRows(WorldImage grid);

  // checks if there is an empty cell at the given location
  boolean hasEmptyCell(Posn pos);

  // keeps track of row count for empty cell checker
  boolean hasEmptyCell(Posn pos, int rowCount);

  //checks if at each row, the target contains a trophy of the same color
  boolean sameColorTargetTrophyRows(ILoRow rc);

  boolean sameColorTargetTrophyConsLoRow(ConsLoRow rc);

  boolean sameColorTargetTrophyMtLoRow(MtLoRow rc);
}

// empty list of rows
class MtLoRow implements ILoRow {
  // draws this empty row
  public WorldImage drawRows() {
    return new EmptyImage();
  }

  // draws the accumulated rows image
  public WorldImage drawRows(WorldImage grid) {
    return grid;
  }

  // checks if there is an empty cell at the given location of this empty list
  public boolean hasEmptyCell(Posn pos) {
    return false;
  }

  // keeps track of row count for empty cell checker
  public boolean hasEmptyCell(Posn pos, int rowCount) {
    // reached end of list
    return false;
  }

  // this means that each target has the correct color trophy on it
  public boolean sameColorTargetTrophyRows(ILoRow rc) {
    return rc.sameColorTargetTrophyMtLoRow(this);
  }
  
  // 
  public boolean sameColorTargetTrophyMtLoRow(MtLoRow rc) {
    return true;
  }

  public boolean sameColorTargetTrophyConsLoRow(ConsLoRow rc) {
    return false;
  }
}

// non-empty list of rows
class ConsLoRow implements ILoRow {
  // the Row of this element
  Row first;
  // the rest of the list
  ILoRow rest;

  ConsLoRow(Row first, ILoRow rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * TEMPLATE: Fields: ... this.first ... -- Row ... this.rest ... -- ILoRow
   * 
   * Methods: ... drawRows() ... -- WorldImage ... drawRows(WorldImage grid) ...
   * -- WorldImage ... hasEmptyCell(Posn pos) ... -- boolean ... hasEmptyCell(Posn
   * pos, int rowCount) ... -- boolean
   * 
   * Methods for fields: ... this.first.draw() ... -- WorldImage ...
   * this.first.createRowCells() ... -- ILoCell ... this.rest.drawRows() ... --
   * WorldImage ... this.rest.drawRows(WorldImage grid) ... -- WorldImage ...
   * this.rest.hasEmptyCell(Posn pos) ... -- boolean ...
   * this.rest.hasEmptyCell(Posn pos, int rowCount) ... -- boolean
   */

  // draws this list of rows
  public WorldImage drawRows() {
    return drawRows(new EmptyImage());
  }

  // draws the accumulated rows image
  public WorldImage drawRows(WorldImage grid) {
    return this.rest.drawRows(new AboveImage(grid, this.first.drawRow()));
  }

  // checks if there is an empty cell at the given location of this empty list
  public boolean hasEmptyCell(Posn pos) {
    return hasEmptyCell(pos, pos.y);
  }

  // keeps track of row count for empty cell checker
  public boolean hasEmptyCell(Posn pos, int rowCount) {
    if (rowCount == 0) {
      // delegate to Row
      return this.first.hasEmptyCell(pos);
    }
    else {
      return this.rest.hasEmptyCell(pos, rowCount - 1);
    }
  }

  public boolean sameColorTargetTrophyRows(ILoRow rc) {
    return rc.sameColorTargetTrophyConsLoRow(this);
  }

  public boolean sameColorTargetTrophyMtLoRow(MtLoRow rc) {
    return false;
  }

  public boolean sameColorTargetTrophyConsLoRow(ConsLoRow rc) {
    if (this.first.sameColorTargetTrophyRow(rc.first)) {
      return this.rest.sameColorTargetTrophyRows(rc.rest);
    }
    else {
      return false;
    }
  }
}

// list of Strings
interface ILoString {
  // creates a list of rows based on this list of Strings
  ILoRow toRows(CellFactory factory);

  // helper for creating rows
  ILoRow toRowsHelper(CellFactory factory, int rowNum);
}

// empty list of Strings
class MtLoString implements ILoString {
  // creates a list of rows based on this empty list
  public ILoRow toRows(CellFactory factory) {
    return new MtLoRow();
  }

  // helper for creating rows
  public ILoRow toRowsHelper(CellFactory factory, int rowNum) {
    return new MtLoRow();
  }
}

// non-empty list of Strings
class ConsLoString implements ILoString {
  // the String of this element
  String first;
  // the rest of the list
  ILoString rest;

  // the constructor
  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * TEMPLATE: Fields: ... this.first ... -- String ... this.rest ... -- ILoString
   * 
   * Methods: ... toRows(CellFactory factory) ... -- ILoRow ...
   * toRowsHelper(CellFactory factory, int rowNum) ... -- ILoRow
   * 
   * Methods for fields: ... this.rest.toRows(CellFactory factory) ... -- ILoRow
   * ... this.rest.toRowsHelper(CellFactory factory, int rowNum) ... -- ILoRow
   */

  // creates a list of rows based on this non-empty list
  public ILoRow toRows(CellFactory factory) {
    return toRowsHelper(factory, 0);
  }

  // helper for creating rows
  public ILoRow toRowsHelper(CellFactory factory, int rowNum) {
    Row row = new Row(rowNum, this.first, factory);
    return new ConsLoRow(row, this.rest.toRowsHelper(factory, rowNum + 1));
  }
}

// Represents functions of signature A -> R, for some argument type A and
// result type R
interface IFunc<A, R> {
  R apply(A input);
}

// Represents functions of signature A -> R, for some argument type A1, A2 
// and result type R
interface IFunc2<A1, A2, R> {
  R apply(A1 input1, A2 input2);
}

// generic list
interface IList<T> {
  // map over a list, and produce a new list with a (possibly different)
  // element type
  <U> IList<U> map(IFunc<T, U> f);

  <U> U foldr(IFunc2<T, U, U> func, U base);

  int length();
}

// empty generic list
class MtList<T> implements IList<T> {
  public <U> IList<U> map(IFunc<T, U> f) {
    return new MtList<U>();
  }

  //
  public <U> U foldr(IFunc2<T, U, U> func, U base) {
    return base;
  }

  // returns length of this list
  public int length() {
    return 0;
  }
}

// non-empty generic list
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  public <U> IList<U> map(IFunc<T, U> f) {
    return new ConsList<U>(f.apply(this.first), this.rest.map(f));
  }

  public <U> U foldr(IFunc2<T, U, U> func, U base) {
    return func.apply(this.first, this.rest.foldr(func, base));
  }

  // returns length of this list
  public int length() {
    return 1 + this.length();
  }
}

class ExamplesLists {
  IList<Integer> list = new ConsList<Integer>(1,
      new ConsList<Integer>(2, new ConsList<Integer>(3, new MtList<Integer>())));

}
