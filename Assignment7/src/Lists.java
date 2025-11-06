import tester.*; // The tester library
import javalib.worldimages.*;
import javalib.funworld.*;
import javalib.worldcanvas.WorldCanvas;
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
   * ... toRows(CellFactory factory) ...                     -- ILoRow
   * ... toRowsHelper(CellFactory factory, int rowNum) ...   -- ILoRow
   * 
   * Methods for fields: 
   * ... this.rest.toRows(CellFactory factory) ...                     -- ILoRow
   * ... this.rest.toRowsHelper(CellFactory factory, int rowNum) ...   -- ILoRow
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

class Examples {
  //
}










