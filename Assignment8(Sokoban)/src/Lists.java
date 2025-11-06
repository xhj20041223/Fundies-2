import tester.*; // The tester library
import java.util.ArrayList;

// list of Strings
interface ILoString {
  // creates a list of rows based on this list of Strings
  ArrayList<Row> toRows(CellFactory factory);

  // helper for creating rows
  void toRowsHelper(CellFactory factory, ArrayList<Row> currentList, int rowNum);
}

// empty list of Strings
class MtLoString implements ILoString {
  // creates a list of rows based on this empty list
  public ArrayList<Row> toRows(CellFactory factory) {
    return new ArrayList<Row>();
  }

  // helper for creating rows
  public void toRowsHelper(CellFactory factory, ArrayList<Row> currentList, int rowNum) {
    return;
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
   * TEMPLATE: 
   * Fields: 
   * ... this.first ...         -- String 
   * ... this.rest ...          -- ILoString
   * 
   * Methods: 
   * ... toRows(CellFactory factory) ...                     -- ArrayList<Row>
   * ... toRowsHelper(CellFactory factory, int rowNum) ...   -- ArrayList<Row>
   * 
   * Methods for fields: 
   * ... this.rest.toRows(CellFactory factory) ...                     -- ArrayList<Row>
   * ... this.rest.toRowsHelper(CellFactory factory, int rowNum) ...   -- ArrayList<Row>
   */

  // creates a list of rows based on this non-empty list of strings
  public ArrayList<Row> toRows(CellFactory factory) {
    ArrayList<Row> stringToRows = new ArrayList<Row>();
    this.toRowsHelper(factory, stringToRows, 0);
    return stringToRows;
  }

  // helper for creating rows
  public void toRowsHelper(CellFactory factory, ArrayList<Row> currentList, int rowNum) {
    Row row = new Row(rowNum, this.first, factory);
    currentList.add(row);
    this.rest.toRowsHelper(factory, currentList, rowNum + 1);
  }
}

class Examples {
  CellFactory contentFact = new ContentFactory();
  ILoString contentDescrip = new ConsLoString("_bv", new ConsLoString("_WW", new MtLoString()));
  ArrayList<Row> rowsExpected = new ArrayList<Row>();

  void testToRows(Tester t) {
    ArrayList<Row> rows = contentDescrip.toRows(contentFact);
    rowsExpected.add(new Row(0, "_bv", contentFact));
    rowsExpected.add(new Row(1, "_WW", contentFact));
    t.checkExpect(rows, rowsExpected);
  }
}










