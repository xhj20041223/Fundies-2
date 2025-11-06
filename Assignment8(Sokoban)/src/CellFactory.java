import tester.*; // The tester library
import javalib.worldimages.*;

// to represent a factory that creates cells of a Sokoban grid
interface CellFactory {
  // default create
  Cell createCell(Posn pos);

  // creates cell based on String
  Cell createCell(Posn pos, String s);
}

// creates ground cells of a Sokoban grid
class GroundFactory implements CellFactory {
  /*
   * TEMPLATE: 
   * Methods: 
   * ... createCell(Posn pos) ...              -- Content
   * ... createCell(Posn pos, String s) ...    -- Content
   */

  // creates blank ground cell
  public Cell createCell(Posn pos) {
    return new BlankGround(pos);
  }

  // creates appropriate ground cell
  public Cell createCell(Posn pos, String s) {
    // if it is a color
    if (s.equals("Y")) {
      return new Target(pos, s);
    }
    else if (s.equals("G")) {
      return new Target(pos, s);
    }
    else if (s.equals("B")) {
      return new Target(pos, s);
    }
    else if (s.equals("R")) {
      return new Target(pos, s);
    }
    else if (s.equals("_")) {
      return new BlankGround(pos);
    }
    else if (s.equals("h")) {
      return new Hole(pos);
    }
    else if (s.equals("i")) {
      return new Ice(pos);
    }
    else {
      throw new IllegalArgumentException("Not a valid ground cell");
    }
  }
}

// creates content cells for a Sokoban grid
class ContentFactory implements CellFactory {
  /*
   * TEMPLATE: 
   * Methods: 
   * ... createCell(Posn pos) ...              -- Content
   * ... createCell(Posn pos, String s) ...    -- Content
   */

  // creates blank ground cell
  public Cell createCell(Posn pos) {
    return new BlankCell(pos);
  }

  // creates appropriate content cell
  public Cell createCell(Posn pos, String s) {
    // checks what kind of content to create
    // if it is a color
    if (s.equals("y")) {
      return new Trophy(pos, s);
    }
    else if (s.equals("g")) {
      return new Trophy(pos, s);
    }
    else if (s.equals("b")) {
      return new Trophy(pos, s);
    }
    else if (s.equals("r")) {
      return new Trophy(pos, s);
    }
    else if (s.equals("_")) {
      return new BlankCell(pos);
    }
    else if (s.equals("W")) {
      return new BrickWall(pos);
    }
    else if (s.equals("B")) {
      return new Crate(pos);
    }
    else if (s.equals(">") || s.equals(">") || s.equals("v") || s.equals("^")) {
      return new Player(pos, s);
    }
    else {
      throw new IllegalArgumentException("Not a valid content cell");
    }
  }
}

class ExamplesFactory {
  GroundFactory groundFactory = new GroundFactory();
  ContentFactory contentFactory = new ContentFactory();
  Posn testPos = new Posn(0, 0);

  // tests the creation of a row of ground cells
  boolean testBlankCell(Tester t) {
    ContentFactory contentFactory = new ContentFactory();
    Posn pos = new Posn(0, 0);
    return t.checkExpect(contentFactory.createCell(pos, "_"), new BlankCell(pos));
  }

  // tests the creation of a row of ground cells
  boolean testBlankGround(Tester t) {
    GroundFactory groundFactory = new GroundFactory();
    Posn pos = new Posn(0, 0);
    return t.checkExpect(groundFactory.createCell(pos, "_"), new BlankGround(pos));
  }

  void testGroundCreateCellWithParam(Tester t) {
    t.checkExpect(groundFactory.createCell(testPos, "R") instanceof Target, true);
    t.checkExpect(groundFactory.createCell(testPos, "_") instanceof BlankGround, true);
    t.checkException(new IllegalArgumentException("Not a valid ground cell"), groundFactory,
        "createCell", testPos, "X");
  }

  void testContentCreateCellDefault(Tester t) {
    t.checkExpect(contentFactory.createCell(testPos) instanceof BlankCell, true);
    t.checkExpect(contentFactory.createCell(new Posn(1, 1)) instanceof BlankCell, true);
  }

  void testContentCreateCellWithParam(Tester t) {
    t.checkExpect(contentFactory.createCell(testPos, "b") instanceof Trophy, true);
    t.checkExpect(contentFactory.createCell(testPos, "W") instanceof BrickWall, true);
    t.checkExpect(contentFactory.createCell(testPos, ">") instanceof Player, true);
  }
}