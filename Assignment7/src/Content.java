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

  // returns the player if the content is a player
  Player findPlayer();
}

// represents ground content of a Sokoban grid
abstract class GroundContent implements Cell {
  // the cell position of this object, x being column, y being row number
  Posn position;

  // the constructor
  GroundContent(Posn pos) {
    this.position = pos;
  }

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

  // returns the player if this content is a player
  public Player findPlayer() {
    return null;
  }
}

// represents cell content of a Sokoban grid
abstract class CellContent implements Cell {
  // the cell position of this object
  Posn position;

  // the constructor
  CellContent(Posn pos) {
    this.position = pos;
  }

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

  // returns the player if this content is a player
  public Player findPlayer() {
    return null;
  }
  
  // moves this object in the indicated direction (changes this object's position)
  void move(Posn newPos) {
    this.position = newPos;
  }
}

// represents a blank ground content
class BlankGround extends GroundContent {
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
   */

  // draws a blank
  public WorldImage draw() {
    // need to care about size of images?
    return new ComputedPixelImage(120, 120);
    //return new RectangleImage(120, 120, OutlineMode.OUTLINE, Color.black);
  }

  @Override
  // checks whether this cell is empty
  public boolean isEmptyCell() {
    return true;
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
}

// represents a blank cell
class BlankCell extends CellContent {
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
   */

  // draws a blank
  public WorldImage draw() {
    return new ComputedPixelImage(120, 120);
  }

  @Override
  // checks whether this cell is empty
  public boolean isEmptyCell() {
    return true;
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
   */
  
  // draws this wall
  public WorldImage draw() {
    return new FromFileImage("src/brickwall.png");
  }
}

// represents a movable object within the game
abstract class Movable extends CellContent {
  // the constructor
  Movable(Posn pos) {
    super(pos);
  }

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
   * ... getPosition() ...        -- Posn
   * ... isEmptyCell() ...        -- boolean
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
   */

  // draws this crate
  public WorldImage draw() {
    return new FromFileImage("src/crate.png");
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
}

class ExamplesContent {
  //
  Posn placementPos = new Posn(0, 0);
  Cell blankGround = new BlankGround(placementPos);
  Cell blankCell = new BlankCell(placementPos);
  Cell target = new Target(new Posn(1, 2), "R");
  Cell player = new Player(new Posn(1, 2), "v");

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
    t.checkExpect(new Player(new Posn(0, 0), ">").canMove(), true);
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(player.draw(), 250, 250)) && c.show();
  }
}
