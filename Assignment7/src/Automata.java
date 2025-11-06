import tester.*;
import javalib.worldimages.*;
import javalib.impworld.*;
import java.awt.Color;
import java.util.ArrayList;

interface ICell {
  // gets the state of this ICell
  int getState();

  // render this ICell as an image of a rectangle with this width and height
  WorldImage render(int width, int height);

  // produces the child cell of this ICell with the given left and right neighbors
  ICell childCell(ICell left, ICell right);
}

class InertCell implements ICell {

  // gets the state of this ICell
  public int getState() {
    return 0;
  }

  // render this ICell as an image of a rectangle with this width and height
  public WorldImage render(int width, int height) {
    return new RectangleImage(width, height, OutlineMode.SOLID, Color.white);
  }

  // produces the child cell of this ICell with the given left and right neighbors
  public ICell childCell(ICell left, ICell right) {
    return this;
  }
}

abstract class ARuleCell implements ICell {
  // the state of the cell
  int state;
  // the rule to generate the children. If the combination of
  // the adjacent 3 cells get a specific value in binary, generate
  // the child with the certain state
  ArrayList<Integer> rule;

  ARuleCell(int rule, int state) {
    this.state = state;
    this.rule = this.generateRule(rule);
  }

  // generate the rule by the rule number
  ArrayList<Integer> generateRule(int rule) {
    ArrayList<Integer> result = new ArrayList<Integer>();
    for (int i = 7; i >= 0; i -= 1) {
      if (rule >= Math.pow(2, i)) {
        rule -= Math.pow(2, i);
        result.add(0, 1);
      }
      else {
        result.add(0, 0);
      }
    }
    return result;
  }

  // gets the state of this ICell
  public int getState() {
    return this.state;
  }

  // render this ICell as an image of a rectangle with this width and height
  public WorldImage render(int width, int height) {
    if (this.state == 0) {
      return new RectangleImage(width, height, OutlineMode.SOLID, Color.white);
    }
    else {
      return new RectangleImage(width, height, OutlineMode.SOLID, Color.black);
    }
  }

  // produces the child cell of this ICell with the given left and right neighbors
  public abstract ICell childCell(ICell left, ICell right);

}

class Rule60 extends ARuleCell {
  Rule60(int state) {
    super(60, state);
  }

  // produces the child cell of this ICell with the given left and right neighbors
  public ICell childCell(ICell left, ICell right) {
    return new Rule60(this.rule.get(left.getState() * 4 + this.state * 2 + right.getState()));
  }
}

class Rule30 extends ARuleCell {
  Rule30(int state) {
    super(30, state);
  }

  // produces the child cell of this ICell with the given left and right neighbors
  public ICell childCell(ICell left, ICell right) {
    return new Rule30(this.rule.get(left.getState() * 4 + this.state * 2 + right.getState()));
  }
}

class Rule182 extends ARuleCell {
  Rule182(int state) {
    super(182, state);
  }

  // produces the child cell of this ICell with the given left and right neighbors
  public ICell childCell(ICell left, ICell right) {
    return new Rule182(this.rule.get(left.getState() * 4 + this.state * 2 + right.getState()));
  }
}

class CellArray {
  // the array of cells
  ArrayList<ICell> cells;

  public CellArray(ArrayList<ICell> cells) {
    this.cells = cells;
  }

  // generate the next generation of the cells
  CellArray nextGen() {
    ArrayList<ICell> result = new ArrayList<ICell>();
    if (this.cells.size() == 1) {
      result.add(this.cells.get(0).childCell(new InertCell(), new InertCell()));
    }
    else if (this.cells.size() != 0) {
      result.add(this.cells.get(0).childCell(new InertCell(), this.cells.get(1)));
      for (int i = 1; i < this.cells.size() - 1; i += 1) {
        result.add(this.cells.get(i).childCell(this.cells.get(i - 1), this.cells.get(i + 1)));
      }
      result.add(this.cells.get(this.cells.size() - 1)
          .childCell(this.cells.get(this.cells.size() - 2), new InertCell()));
    }
    return new CellArray(result);
  }

  // draw the cells with the given width and height
  WorldImage draw(int width, int height) {
    WorldImage result = new EmptyImage();
    for (ICell c : this.cells) {
      result = new BesideImage(result, c.render(width, height));
    }
    return result;
  }
}

class CAWorld extends World {

  // constants
  static final int CELL_WIDTH = 10;
  static final int CELL_HEIGHT = 10;
  static final int INITIAL_OFF_CELLS = 20;
  static final int TOTAL_CELLS = INITIAL_OFF_CELLS * 2 + 1;
  static final int NUM_HISTORY = 41;
  static final int TOTAL_WIDTH = TOTAL_CELLS * CELL_WIDTH;
  static final int TOTAL_HEIGHT = NUM_HISTORY * CELL_HEIGHT;

  // the current generation of cells
  CellArray curGen;
  // the history of previous generations (earliest state at the start of the list)
  ArrayList<CellArray> history;

  // Constructs a CAWorld with INITIAL_OFF_CELLS of off cells on the left,
  // then one on cell, then INITIAL_OFF_CELLS of off cells on the right
  CAWorld(ICell off, ICell on) {
    ArrayList<ICell> initialGen = new ArrayList<ICell>();
    for (int i = 0; i < INITIAL_OFF_CELLS; i += 1) {
      initialGen.add(off);
    }
    initialGen.add(on);
    for (int i = 0; i < INITIAL_OFF_CELLS; i += 1) {
      initialGen.add(off);
    }
    this.curGen = new CellArray(initialGen);
    this.history = new ArrayList<CellArray>();
  }

  // Modifies this CAWorld by adding the current generation to the history
  // and setting the current generation to the next one
  public void onTick() {
    this.history.add(this.curGen);
    this.curGen = this.curGen.nextGen();
  }

  // Draws the current world, ``scrolling up'' from the bottom of the image
  public WorldImage makeImage() {
    // make a light-gray background image big enough to hold 41 generations of 41
    // cells each
    WorldImage bg = new RectangleImage(TOTAL_WIDTH, TOTAL_HEIGHT, OutlineMode.SOLID,
        new Color(240, 240, 240));

    // build up the image containing the past and current cells
    WorldImage cells = new EmptyImage();
    for (CellArray array : this.history) {
      cells = new AboveImage(cells, array.draw(CELL_WIDTH, CELL_HEIGHT));
    }
    cells = new AboveImage(cells, this.curGen.draw(CELL_WIDTH, CELL_HEIGHT));

    // draw all the cells onto the background
    return new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, cells, 0, 0, bg);
  }

  public WorldScene makeScene() {
    WorldScene canvas = new WorldScene(TOTAL_WIDTH, TOTAL_HEIGHT);
    canvas.placeImageXY(this.makeImage(), TOTAL_WIDTH / 2, TOTAL_HEIGHT / 2);
    return canvas;
  }
}

class AutomataExamples {
  static final int WIDTH = 411;
  static final int HEIGHT = 411;

  ICell inertCell = new InertCell();
  ICell rule30Cell0 = new Rule30(0);
  ICell rule30Cell1 = new Rule30(1);
  ICell rule60Cell1 = new Rule60(1);
  WorldImage black = new RectangleImage(10, 10, OutlineMode.SOLID, Color.BLACK);
  WorldImage white = new RectangleImage(10, 10, OutlineMode.SOLID, Color.WHITE);

  ARuleCell c = new Rule30(0);
  CellArray emptyArray;
  CellArray singleCellArray;
  CellArray edgeCaseArray;
  ArrayList<Integer> rule30Expected;
  ArrayList<Integer> rule0Expected;
  ArrayList<Integer> rule255Expected;
  CAWorld testWorld;

  void initData() {
    emptyArray = new CellArray(new ArrayList<>());

    ArrayList<ICell> single = new ArrayList<>();
    single.add(rule30Cell1);
    singleCellArray = new CellArray(single);

    ArrayList<ICell> edge = new ArrayList<>();
    edge.add(rule30Cell1);
    edge.add(inertCell);
    edge.add(rule60Cell1);
    edgeCaseArray = new CellArray(edge);

    rule30Expected = new ArrayList<>();
    rule30Expected.add(0);
    rule30Expected.add(1);
    rule30Expected.add(1);
    rule30Expected.add(1);
    rule30Expected.add(1);
    rule30Expected.add(0);
    rule30Expected.add(0);
    rule30Expected.add(0);

    rule0Expected = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      rule0Expected.add(0);
    }
    rule255Expected = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      rule255Expected.add(1);
    }
    testWorld = new CAWorld(inertCell, rule30Cell1);
  }

  void testInertCellGetState(Tester t) {
    t.checkExpect(inertCell.getState(), 0);
  }

  void testInertCellRender(Tester t) {
    t.checkExpect(inertCell.render(10, 10), white);
  }

  void testInertCellChildCell(Tester t) {
    t.checkExpect(inertCell.childCell(rule30Cell1, rule60Cell1), inertCell);
  }

  void testARuleCellGenerateRule(Tester t) {
    this.initData();
    t.checkExpect(c.generateRule(30), rule30Expected);
    t.checkExpect(c.generateRule(0), rule0Expected);
    t.checkExpect(c.generateRule(255), rule255Expected);
  }

  void testARuleCellGetState(Tester t) {
    t.checkExpect(rule30Cell0.getState(), 0);
    t.checkExpect(rule30Cell1.getState(), 1);
  }

  void testARuleCellRender(Tester t) {
    t.checkExpect(rule30Cell0.render(10, 10), white);
    t.checkExpect(rule30Cell1.render(10, 10), black);
  }

  void testRule30ChildCell(Tester t) {
    this.initData();
    t.checkExpect(rule30Cell1.childCell(inertCell, inertCell).getState(), 1);
    t.checkExpect(rule30Cell1.childCell(rule30Cell1, inertCell).getState(), 0);
  }

  void testCellArrayNextGen(Tester t) {
    this.initData();
    t.checkExpect(emptyArray.nextGen().cells.size(), 0);
    t.checkExpect(singleCellArray.nextGen().cells.get(0).getState(), 1);
    t.checkExpect(edgeCaseArray.nextGen().cells.size(), 3);
  }

  void testCellArrayDraw(Tester t) {
    this.initData();
    t.checkExpect(singleCellArray.draw(10, 10),
        new BesideImage(new EmptyImage(), rule30Cell1.render(10, 10)));
  }

  void testCAWorldOnTick(Tester t) {
    this.initData();
    testWorld.onTick();
    t.checkExpect(testWorld.history.size(), 1);
    t.checkExpect(testWorld.curGen, testWorld.history.get(0).nextGen());
  }

  void testBigBang(Tester t) {
    new CAWorld(new Rule30(0), new Rule30(1)).bigBang(WIDTH, HEIGHT, 1);
    new CAWorld(new Rule60(0), new Rule60(1)).bigBang(WIDTH, HEIGHT, 1);
  }
}