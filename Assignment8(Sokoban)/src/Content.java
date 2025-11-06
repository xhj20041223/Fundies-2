import tester.*; // The tester library
import javalib.worldimages.*;
import javalib.funworld.*;
import javalib.worldcanvas.WorldCanvas;
import java.util.ArrayList;

// represents a type of content on a Sokoban grid
interface Cell {
  // renders the content as an image
  WorldImage draw();

  // checks whether this cell is empty
  boolean isEmptyCell();

  // checks whether the given content is in the same place as this content
  boolean isInSamePlace(Cell c);

  // checks whether the given position is the position of this cell
  boolean hasPosition(Posn pos);

  // add this cell to the list if it's a target
  void addToTargetList(ArrayList<Target> targets);

  // add this cell to the list if it's a hole
  void addToHoleList(ArrayList<Hole> holes);

  // add this cell to the list if it's a trophy
  void addToTrophyList(ArrayList<Trophy> trophies);

  // add this cell to the list if it's a trophy
  void addToIceList(ArrayList<Ice> ices);
  
  // get the position of the cell
  Posn getPosn();

  // returns the player if the content is a player
  Player findPlayer();
  
  // copy the cell
  Cell copy();
}

// represents ground content of a Sokoban grid
abstract class GroundContent implements Cell {
  // the cell position of this object, x being column, y being row number
  Posn position;

  // the constructor
  GroundContent(Posn pos) {
    this.position = pos;
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.position ...           -- Posn 
   * 
   * Methods: 
   * ... getPosition() ...           -- Posn
   * ... isEmptyCell() ...           -- boolean
   * ... isInSamePlace(Cell c) ...   -- boolean
   * ... hasPosition(Posn pos) ...   -- boolean
   * ... hasPosition(Posn pos) ...   -- boolean
   * ... addToTargetList(ArrayList<Target> targets) ...    -- void
   * ... addToHoleList(ArrayList<Hole> holes) ...          -- void
   * ... addToTrophyList(ArrayList<Trophy> trophies) ...   -- void
   * ... findPlayer() ...            -- Player
   * ... getPosn() ...               -- Posn
   */

  // gets the position of this ground object
  Posn getPosition() {
    return this.position;
  }

  // checks whether this cell is empty
  public boolean isEmptyCell() {
    return false;
  }

  // checks whether the given content is in the same place as this content
  public boolean isInSamePlace(Cell c) {
    // compare posn
    return c.hasPosition(this.position);
  }

  // checks whether the given position is the position of this cell
  public boolean hasPosition(Posn pos) {
    return position.equals(pos);
  }

  // add this content to the list of targets if it's a target
  public void addToTargetList(ArrayList<Target> targets) {
    return;
  }

  // add this content to the list of targets if it's a hole
  public void addToHoleList(ArrayList<Hole> holes) {
    return;
  }

  // add this content to the list of targets if it's a trophy
  public void addToTrophyList(ArrayList<Trophy> trophies) {
    return;
  }

  // add this content to the list of targets if it's a trophy
  public void addToIceList(ArrayList<Ice> ices) {
    return;
  }


  // returns the player if this content is a player
  public Player findPlayer() {
    return null;
  }

  // get the position of the cell
  public Posn getPosn() {
    return this.position;
  }

}

// represents cell content of a Sokoban grid
abstract class CellContent implements Cell {
  // the cell position of this object, x being column, y being row number
  Posn position;

  // the constructor
  CellContent(Posn pos) {
    this.position = pos;
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.position ...           -- Posn 
   * 
   * Methods: 
   * ... getPosition() ...           -- Posn
   * ... isEmptyCell() ...           -- boolean
   * ... isInSamePlace(Cell c) ...   -- boolean
   * ... hasPosition(Posn pos) ...   -- boolean
   * ... hasPosition(Posn pos) ...   -- boolean
   * ... addToTargetList(ArrayList<Target> targets) ...    -- void
   * ... addToHoleList(ArrayList<Hole> holes) ...          -- void
   * ... addToTrophyList(ArrayList<Trophy> trophies) ...   -- void
   * ... findPlayer() ...            -- Player
   * ... move(Posn newPos) ...       -- void
   * ... getPosn() ...               -- Posn
   */

  // gets the position of this content object
  Posn getPosition() {
    return this.position;
  }

  // checks whether this cell is empty
  public boolean isEmptyCell() {
    return false;
  }

  // checks whether the given content is in the same place as this content
  public boolean isInSamePlace(Cell c) {
    // compare posn
    return c.hasPosition(this.position);
  }

  // checks whether the given position is the position of this cell
  public boolean hasPosition(Posn pos) {
    return position.equals(pos);
  }

  // checks whether this content can move
  public boolean canMove() {
    return false;
  }

  // add this content to the list of targets if it's a target
  public void addToTargetList(ArrayList<Target> targets) {
    return;
  }

  // add this content to the list of targets if it's a hole
  public void addToHoleList(ArrayList<Hole> holes) {
    return;
  }

  // add this content to the list of targets if it's a trophy
  public void addToTrophyList(ArrayList<Trophy> trophies) {
    return;
  }

  // add this content to the list of targets if it's a target
  public void addToIceList(ArrayList<Ice> ices) {
    return;
  }

  // returns the player if this content is a player
  public Player findPlayer() {
    return null;
  }
  
  // moves this object in the indicated direction (changes this object's position)
  void move(Posn newPos) {
    this.position = newPos;
  }

  // get the position of the cell
  public Posn getPosn() {
    return this.position;
  }
}

// represents a blank ground content
class BlankGround extends GroundContent {
  // size of these cells
  static final int SIZE = 120;

  // the constructor
  BlankGround(Posn pos) {
    super(pos);
  }
  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.position ...        -- Posn 
   * 
   * Methods: 
   * ... draw() ...               -- WorldImage
   * ... isEmptyCell() ...        -- boolean
   * ... copy() ...               -- Cell
   */

  // draws a blank
  public WorldImage draw() {
    // need to care about size of images?
    return new ComputedPixelImage(SIZE, SIZE);
    //return new RectangleImage(120, 120, OutlineMode.OUTLINE, Color.black);
  }

  @Override
  // checks whether this cell is empty
  public boolean isEmptyCell() {
    return true;
  }

  @Override
  // copy the cell
  public Cell copy() {
    return new BlankGround(new Posn(this.position.x, this.position.y));
  }
}

// the target which to place the trophies on
class Target extends GroundContent {
  // the color of this target
  String color;

  // the constructor
  Target(Posn pos, String color) {
    super(pos);
    this.color = color;
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.position ...        -- Posn 
   * ... this.color ...           -- String
   * 
   * Methods: 
   * ... draw() ...               -- WorldImage
   * ... isEmptyCell() ...        -- boolean
   * ... copy() ...               -- Cell
   */

  // draws a this target
  public WorldImage draw() {
    if (this.color.equals("R")) {
      WorldImage target = new FromFileImage("src/red_target.png");
      return new ScaleImage(target, 0.25);
    }
    else if (this.color.equals("B")) {
      WorldImage target = new FromFileImage("src/blue_target.png");
      return new ScaleImage(target, 0.25);
    }
    else if (this.color.equals("Y")) {
      WorldImage target = new FromFileImage("src/yellow_target.png");
      return new ScaleImage(target, 0.25);
    }
    else if (this.color.equals("G")) {
      WorldImage target = new FromFileImage("src/green_target.png");
      return new ScaleImage(target, 0.25);
    }
    else {
      throw new RuntimeException("Trophy color does not exist.");
    }
  }

  @Override
  // add this content to the list of targets 
  public void addToTargetList(ArrayList<Target> targets) {
    targets.add(this);
    return;
  }

  @Override
  // copy the cell
  public Cell copy() {
    return new Target(new Posn(position.x, position.y), color);
  }
}

class Hole extends GroundContent {
  // the constructor
  Hole(Posn pos) {
    super(pos);
  }
  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.position ...        -- Posn 
   * 
   * Methods: 
   * ... draw() ...               -- WorldImage
   * ... isEmptyCell() ...        -- boolean
   * ... copy() ...               -- Cell
   */

  // draws a hole
  public WorldImage draw() {
    return new FromFileImage("src/hole.png");
    //return new RectangleImage(120, 120, OutlineMode.OUTLINE, Color.black);
  }

  @Override
  // checks whether this cell is empty
  public boolean isEmptyCell() {
    return true;
  }

  @Override
  // add this content to the list of holes 
  public void addToHoleList(ArrayList<Hole> holes) {
    holes.add(this);
    return;
  }

  @Override
  // copy the cell
  public Cell copy() {
    return new Hole(new Posn(position.x, position.y));
  }
}


//represents an ice ground content
class Ice extends GroundContent {
  
  // the constructor
  Ice(Posn pos) {
    super(pos);
  }
  /*
  * TEMPLATE: 
  * Fields: 
  * ... this.position ...        -- Posn 
  * 
  * Methods: 
  * ... draw() ...               -- WorldImage
  * ... isEmptyCell() ...        -- boolean
   * ... copy() ...               -- Cell
  */
  
  // draws a blank
  public WorldImage draw() {
    return new FromFileImage("src/ice.png");
    //return new RectangleImage(120, 120, OutlineMode.OUTLINE, Color.black);
  }
  
  @Override
  // checks whether this cell is empty
  public boolean isEmptyCell() {
    return false;
  }

  @Override
  // add this content to the list of ices 
  public void addToIceList(ArrayList<Ice> ices) {
    ices.add(this);
    return;
  }

  @Override
  // copy the cell
  public Cell copy() {
    return new Ice(new Posn(position.x, position.y));
  }
}

// represents a blank cell
class BlankCell extends CellContent {
  // size of these cells
  static final int SIZE = 120;

  // the constructor
  BlankCell(Posn pos) {
    super(pos);
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.position ...        -- Posn 
   * 
   * Methods: 
   * ... draw() ...               -- WorldImage
   * ... isEmptyCell() ...        -- boolean
   * ... copy() ...               -- Cell
   */

  // draws a blank
  public WorldImage draw() {
    return new ComputedPixelImage(SIZE, SIZE);
  }

  @Override
  // checks whether this cell is empty
  public boolean isEmptyCell() {
    return true;
  }

  @Override
  // copy the cell
  public Cell copy() {
    return new BlankCell(new Posn(position.x, position.y));
  }
}

// walls that contain the player within a limited space
class BrickWall extends CellContent {
  // the constructor
  BrickWall(Posn pos) {
    super(pos);
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.position ...        -- Posn 
   * 
   * Methods: 
   * ... draw() ...               -- WorldImage
   * ... isEmptyCell() ...        -- boolean
   * ... copy() ...               -- Cell
   */
  
  // draws this wall
  public WorldImage draw() {
    return new FromFileImage("src/brickwall.png");
  }

  @Override
  // copy the cell
  public Cell copy() {
    return new BrickWall(new Posn(position.x, position.y));
  }
}

// represents a movable object within the game
abstract class Movable extends CellContent {
  // the constructor
  Movable(Posn pos) {
    super(pos);
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.position ...        -- Posn 
   * 
   * Methods: 
   * ... canMove() ...            -- boolean
   */

  @Override
  // checks whether this content can move
  public boolean canMove() {
    return true;
  }
}

// represents the player
class Player extends Movable {
  // the direction this player is facing
  String direction;

  // the constructor
  Player(Posn pos, String direction) {
    super(pos);
    this.direction = direction;
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.position ...        -- Posn 
   * ... this.direction ...       -- String
   * 
   * Methods: 
   * ... draw() ...               -- WorldImage
   * ... changeDirection() ...    -- void
   * ... copy() ...               -- Cell
   */

  // draws this player facing in the appropriate direction
  public WorldImage draw() {
    if (this.direction.equals(">")) {
      return new FromFileImage("src/player.png");
    }
    else if (this.direction.equals("<")) {
      return new FromFileImage("src/player_flipped.png");
    }
    else if (this.direction.equals("^")) {
      return new RotateImage(new FromFileImage("src/player.png"), 270);
    }
    else if (this.direction.equals("v")) {
      return new RotateImage(new FromFileImage("src/player.png"), 90);
    }
    else {
      throw new RuntimeException("Player direction does not exist.");
    }
  }

  // moves this object in the indicated direction (changes this object's position)
  void changeDirection(String newDirection) {
    if (newDirection.equals("up")) {
      this.direction = "^";
    }
    else if (newDirection.equals("down")) {
      this.direction = "v";
    }
    else if (newDirection.equals("left")) {
      this.direction = "<";
    }
    else if (newDirection.equals("right")) {
      this.direction = ">";
    }
  }

  @Override
  // returns the player if this content is a player
  public Player findPlayer() {
    return this;
  }

  @Override
  // copy the cell
  public Cell copy() {
    return new Player(new Posn(position.x, position.y), this.direction);
  }
}

// represents a crate that can be pushed
class Crate extends Movable {
  // the constructor
  Crate(Posn pos) {
    super(pos);
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.position ...        -- Posn 
   * 
   * Methods: 
   * ... draw() ...               -- WorldImage
   * ... getPosition() ...        -- Posn
   * ... isEmptyCell() ...        -- boolean
   * ... copy() ...               -- Cell
   */

  // draws this crate
  public WorldImage draw() {
    return new FromFileImage("src/crate.png");
  }

  @Override
  // copy the cell
  public Cell copy() {
    return new Crate(new Posn(position.x, position.y));
  }
}

// represents a trophy in which the player should match with a target
class Trophy extends Movable {
  // the color of this trophy
  String color;

  // the constructor
  Trophy(Posn pos, String color) {
    super(pos);
    this.color = color;
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.position ...        -- Posn 
   * ... this.color ...           -- String
   * 
   * Methods: 
   * ... draw() ...               -- WorldImage
   * ... getPosition() ...        -- Posn
   * ... isEmptyCell() ...        -- boolean
   * ... copy() ...               -- Cell
   */

  // draws this trophy
  public WorldImage draw() {
    if (this.color.equals("r")) {
      return new FromFileImage("src/red_trophy.png");
    }
    else if (this.color.equals("b")) {
      return new FromFileImage("src/blue_trophy.png");
    }
    else if (this.color.equals("y")) {
      return new FromFileImage("src/yellow_trophy.png");
    }
    else if (this.color.equals("g")) {
      return new FromFileImage("src/green_trophy.png");
    }
    else {
      throw new RuntimeException("Trophy color does not exist.");
    }
  }

  @Override
  // add this content to the list of trophies 
  public void addToTrophyList(ArrayList<Trophy> trophies) {
    trophies.add(this);
    return;
  }

  @Override
  // copy the cell
  public Cell copy() {
    return new Trophy(new Posn(this.position.x, this.position.y), this.color);
  }
}

class ExamplesContent {
  //
  Posn placementPos = new Posn(0, 0);
  Cell blankGround = new BlankGround(placementPos);
  Cell blankCell = new BlankCell(placementPos);
  Cell target = new Target(new Posn(1, 2), "R");
  Cell player = new Player(new Posn(1, 2), "v");

  ArrayList<Target> targets;
  ArrayList<Hole> holes;
  ArrayList<Trophy> trophies;

  // test drawing a blank
  boolean testDrawBlank(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(blankGround.draw(), 250, 250)) && c.show();
  }

  // test drawing a target
  boolean testDrawTarget(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(target.draw(), 250, 250)) && c.show();
  }

  // test drawing a player
  boolean testDrawPlayer(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(player.draw(), 250, 250)) && c.show();
  }

  void testIsEmptyCell(Tester t) {
    t.checkExpect(blankGround.isEmptyCell(), true);
    t.checkExpect(blankCell.isEmptyCell(), true);
    t.checkExpect(target.isEmptyCell(), false);
    t.checkExpect(player.isEmptyCell(), false);
  }

  void testIsInSamePlace(Tester t) {
    t.checkExpect(blankGround.isInSamePlace(blankCell), true);
    t.checkExpect(blankGround.isInSamePlace(target), false);
    t.checkExpect(player.isInSamePlace(target), true);
  }

  void testHasPosition(Tester t) {
    t.checkExpect(blankGround.hasPosition(new Posn(0, 0)), true);
    t.checkExpect(blankGround.hasPosition(new Posn(1, 0)), false);
    t.checkExpect(target.hasPosition(new Posn(1, 2)), true);
  }

  void initData() {
    targets = new ArrayList<Target>();
    holes = new ArrayList<Hole>();
    trophies = new ArrayList<Trophy>();
  }

  void testAddToTargetList(Tester t) {
    initData();
    player.addToTargetList(targets);
    ArrayList<Target> expectedTargets = new ArrayList<Target>();
    t.checkExpect(targets, new ArrayList<Target>());

    target.addToTargetList(targets);
    expectedTargets.add((Target) target);
    t.checkExpect(targets, expectedTargets);
  }

  void testAddToHoleList(Tester t) {
    initData();
    Hole hole = new Hole((new Posn(0, 1)));
    player.addToHoleList(holes);
    ArrayList<Hole> expectedHoles = new ArrayList<Hole>();
    t.checkExpect(holes, new ArrayList<Hole>());

    hole.addToHoleList(holes);
    expectedHoles.add(hole);
    t.checkExpect(holes, expectedHoles);
  }
  
  void testAddToTrophyList(Tester t) {
    initData();
    Trophy trophy = new Trophy((new Posn(0, 1)), "g");
    player.addToTrophyList(trophies);
    ArrayList<Trophy> expectedTrophies = new ArrayList<Trophy>();
    t.checkExpect(trophies, new ArrayList<Trophy>());

    trophy.addToTrophyList(trophies);
    expectedTrophies.add(trophy);
    t.checkExpect(trophies, expectedTrophies);
  }

  void testFindPlayer(Tester t) {
    t.checkExpect(target.findPlayer(), null);
    t.checkExpect(player.findPlayer(), player);
  }
}
