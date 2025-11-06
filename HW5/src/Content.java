import tester.*; // The tester library
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import javalib.worldcanvas.WorldCanvas;

// represents a type of content on a Sokoban grid
interface Content {
  // renders the content as an image
  WorldImage draw();

  // checks if the content are the same target and trophy color
  boolean sameColorTargetTrophy(Content c);

  // checks if the content are the same target and trophy color
  boolean sameColorTargetTrophyTarget(Target c);

  // checks if the content are the same target and trophy color
  boolean sameColorTargetTrophyTrophy(Trophy c);

  // checks if the content are the same target and trophy color
  boolean sameColorTargetTrophyBG(BlankGround c);

  // checks if the content are the same target and trophy color
  boolean sameColorTargetTrophyBC(BlankCell c);

  // checks if the content are the same target and trophy color
  boolean sameColorTargetTrophyBW(BrickWall c);

  // checks if the content are the same target and trophy color
  boolean sameColorTargetTrophyPlayer(Player c);

  // checks if the content are the same target and trophy color
  boolean sameColorTargetTrophyCrate(Crate c);

  // checks whether this cell is empty
  boolean isEmptyCell();

}

// represents ground content of a Sokoban grid
abstract class GroundContent implements Content {
  // the cell position of this object
  Posn position;

  // the constructor
  GroundContent(Posn pos) {
    this.position = pos;
  }

  // checks whether this cell is empty
  public boolean isEmptyCell() {
    return false;
  }
}

// represents cell content of a Sokoban grid
abstract class CellContent implements Content {
  // the cell position of this object
  Posn position;

  // the constructor
  CellContent(Posn pos) {
    this.position = pos;
  }

  // checks whether this cell is empty
  public boolean isEmptyCell() {
    return false;
  }
}

// represents a blank ground content
class BlankGround extends GroundContent {
  // the constructor
  BlankGround(Posn pos) {
    super(pos);
  }
  /*
   * TEMPLATE: Fields: ... this.position ... -- Posn
   * 
   * Methods: ... draw() ... -- WorldImage ... isEmptyCell() ... -- boolean
   */

  // draws a blank
  public WorldImage draw() {
    // need to care about size of images?
    return new ComputedPixelImage(120, 120);
    // return new EmptyImage();
  }

  @Override
  // checks whether this cell is empty
  public boolean isEmptyCell() {
    return true;
  }

  // checks if the content are the same target and trophy color
  // as the blank ground
  public boolean sameColorTargetTrophy(Content c) {
    return c.sameColorTargetTrophyBG(this);
  }

  // checks if the target are the same target and trophy color
  // as the blank ground
  public boolean sameColorTargetTrophyTarget(Target c) {
    return true;
  }

  // checks if the trophy are the same target and trophy color
  // as the blank ground
  public boolean sameColorTargetTrophyTrophy(Trophy c) {
    return false;
  }

  // checks if the blank ground are the same target and trophy color
  // as the blank ground
  public boolean sameColorTargetTrophyBG(BlankGround c) {
    return true;
  }

  // checks if the blank cell are the same target and trophy color
  // as the blank ground
  public boolean sameColorTargetTrophyBC(BlankCell c) {
    return true;
  }

  // checks if the brick wall are the same target and trophy color
  // as the blank ground
  public boolean sameColorTargetTrophyBW(BrickWall c) {
    return true;
  }

  // checks if the player are the same target and trophy color
  // as the blank ground
  public boolean sameColorTargetTrophyPlayer(Player c) {
    return true;
  }

  // checks if the crate are the same target and trophy color
  // as the blank ground
  public boolean sameColorTargetTrophyCrate(Crate c) {
    return true;
  }

}

// the target which to place the trophies on
class Target extends GroundContent {
  // the color of this target
  Color color;

  // the constructor
  Target(Posn pos, String color) {
    super(pos);
    if (color.equals("R")) {
      this.color = Color.RED;
    }
    else if (color.equals("B")) {
      this.color = Color.BLUE;
    }
    else if (color.equals("Y")) {
      this.color = Color.YELLOW;
    }
    else if (color.equals("G")) {
      this.color = Color.GREEN;
    }
    else {
      throw new IllegalArgumentException("Target color does not exist.");
    }
  }
  /*
   * TEMPLATE: Fields: ... this.position ... -- Posn ... this.color ... -- String
   * 
   * Methods: ... draw() ... -- WorldImage ... isEmptyCell() ... -- boolean
   */

  // draws a this target
  public WorldImage draw() {
    if (this.color.equals(Color.RED)) {
      return new FromFileImage("src/red_target.png");
    }
    else if (this.color.equals(Color.BLUE)) {
      return new FromFileImage("src/blue_target.png");
    }
    else if (this.color.equals(Color.YELLOW)) {
      return new FromFileImage("src/yellow_target.png");
    }
    return new FromFileImage("src/green_target.png");
  }

  // checks if the content are the same target and trophy color
  // as the target
  public boolean sameColorTargetTrophy(Content c) {
    return c.sameColorTargetTrophyTarget(this);
  }

  // checks if the target are the same target and trophy color
  // as the target
  public boolean sameColorTargetTrophyTarget(Target c) {
    return true;
  }

  // checks if the trophy are the same target and trophy color
  // as the target
  public boolean sameColorTargetTrophyTrophy(Trophy c) {
    return this.color.equals(c.color);
  }

  // checks if the blank ground are the same target and trophy color
  // as the target
  public boolean sameColorTargetTrophyBG(BlankGround c) {
    return true;
  }

  // checks if the blank cell are the same target and trophy color
  // as the target
  public boolean sameColorTargetTrophyBC(BlankCell c) {
    return false;
  }

  // checks if the brick wall are the same target and trophy color
  // as the target
  public boolean sameColorTargetTrophyBW(BrickWall c) {
    return false;
  }

  // checks if the player are the same target and trophy color
  // as the target
  public boolean sameColorTargetTrophyPlayer(Player c) {
    return false;
  }

  // checks if the crate are the same target and trophy color
  // as the target
  public boolean sameColorTargetTrophyCrate(Crate c) {
    return false;
  }
}

// represents a blank cell
class BlankCell extends CellContent {
  // the constructor
  BlankCell(Posn pos) {
    super(pos);
  }
  /*
   * TEMPLATE: Fields: ... this.position ... -- Posn
   * 
   * Methods: ... draw() ... -- WorldImage ... isEmptyCell() ... -- boolean
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

  // checks if the content are the same target and trophy color
  // as the blank cell
  public boolean sameColorTargetTrophy(Content c) {
    return c.sameColorTargetTrophyBC(this);
  }

  // checks if the target are the same target and trophy color
  // as the blank cell
  public boolean sameColorTargetTrophyTarget(Target c) {
    return false;
  }

  // checks if the trophy are the same target and trophy color
  // as the blank cell
  public boolean sameColorTargetTrophyTrophy(Trophy c) {
    return true;
  }

  // checks if the blank ground are the same target and trophy color
  // as the blank cell
  public boolean sameColorTargetTrophyBG(BlankGround c) {
    return true;
  }

  // checks if the blank cell are the same target and trophy color
  // as the blank cell
  public boolean sameColorTargetTrophyBC(BlankCell c) {
    return true;
  }

  // checks if the brick wall are the same target and trophy color
  // as the blank cell
  public boolean sameColorTargetTrophyBW(BrickWall c) {
    return true;
  }

  // checks if the player are the same target and trophy color
  // as the blank cell
  public boolean sameColorTargetTrophyPlayer(Player c) {
    return true;
  }

  // checks if the crate are the same target and trophy color
  // as the blank cell
  public boolean sameColorTargetTrophyCrate(Crate c) {
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
   * TEMPLATE: Fields: ... this.position ... -- Posn
   * 
   * Methods: ... draw() ... -- WorldImage ... isEmptyCell() ... -- boolean
   */

  // draws this wall
  public WorldImage draw() {
    return new FromFileImage("src/brickwall.png");
  }

  // checks if the content are the same target and trophy color
  // as the brick wall
  public boolean sameColorTargetTrophy(Content c) {
    return c.sameColorTargetTrophyBW(this);
  }

  // checks if the target are the same target and trophy color
  // as the brick wall
  public boolean sameColorTargetTrophyTarget(Target c) {
    return false;
  }

  // checks if the trophy are the same target and trophy color
  // as the brick wall
  public boolean sameColorTargetTrophyTrophy(Trophy c) {
    return true;
  }

  // checks if the blank ground are the same target and trophy color
  // as the brick wall
  public boolean sameColorTargetTrophyBG(BlankGround c) {
    return true;
  }

  // checks if the blank cell are the same target and trophy color
  // as the brick wall
  public boolean sameColorTargetTrophyBC(BlankCell c) {
    return true;
  }

  // checks if the brick wall are the same target and trophy color
  // as the brick wall
  public boolean sameColorTargetTrophyBW(BrickWall c) {
    return true;
  }

  // checks if the player are the same target and trophy color
  // as the brick wall
  public boolean sameColorTargetTrophyPlayer(Player c) {
    return true;
  }

  // checks if the crate are the same target and trophy color
  // as the brick wall
  public boolean sameColorTargetTrophyCrate(Crate c) {
    return true;
  }

}

// represents a movable object within the game
abstract class Movable extends CellContent {
  // the constructor
  Movable(Posn pos) {
    super(pos);
  }

  // gets the position of this movable object
  Posn getPosition() {
    return this.position;
  }
  // move()
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
  /*
   * TEMPLATE: Fields: ... this.position ... -- Posn ... this.direction ... --
   * String
   * 
   * Methods: ... draw() ... -- WorldImage ... getPosition() ... -- Posn ...
   * isEmptyCell() ... -- boolean
   */

  // gets the player's position
  Posn getPosition() {
    return this.position;
  }

  void updatePosition(Posn p) {
    this.position = p;
  }

  // checks if the content are the same target and trophy color
  // as the player
  public boolean sameColorTargetTrophy(Content c) {
    return c.sameColorTargetTrophyPlayer(this);
  }

  // checks if the target are the same target and trophy color
  // as the player
  public boolean sameColorTargetTrophyTarget(Target c) {
    return false;
  }

  // checks if the trophy are the same target and trophy color
  // as the player
  public boolean sameColorTargetTrophyTrophy(Trophy c) {
    return true;
  }

  // checks if the blank ground are the same target and trophy color
  // as the player
  public boolean sameColorTargetTrophyBG(BlankGround c) {
    return true;
  }

  // checks if the blank cell are the same target and trophy color
  // as the player
  public boolean sameColorTargetTrophyBC(BlankCell c) {
    return true;
  }

  // checks if the brick wall are the same target and trophy color
  // as the player
  public boolean sameColorTargetTrophyBW(BrickWall c) {
    return true;
  }

  // checks if the player are the same target and trophy color
  // as the player
  public boolean sameColorTargetTrophyPlayer(Player c) {
    return true;
  }

  // checks if the crate are the same target and trophy color
  // as the player
  public boolean sameColorTargetTrophyCrate(Crate c) {
    return true;
  }

}

// represents a crate that can be pushed
class Crate extends Movable {
  // the constructor
  Crate(Posn pos) {
    super(pos);
  }
  /*
   * TEMPLATE: Fields: ... this.position ... -- Posn
   * 
   * Methods: ... draw() ... -- WorldImage ... getPosition() ... -- Posn ...
   * isEmptyCell() ... -- boolean
   */

  // draws this crate
  public WorldImage draw() {
    return new FromFileImage("src/crate.png");
  }

  // checks if the content are the same target and trophy color
  // as the crate
  public boolean sameColorTargetTrophy(Content c) {
    return c.sameColorTargetTrophyCrate(this);
  }

  // checks if the target are the same target and trophy color
  // as the crate
  public boolean sameColorTargetTrophyTarget(Target c) {
    return false;
  }

  // checks if the trophy are the same target and trophy color
  // as the crate
  public boolean sameColorTargetTrophyTrophy(Trophy c) {
    return true;
  }

  // checks if the blank ground are the same target and trophy color
  // as the crate
  public boolean sameColorTargetTrophyBG(BlankGround c) {
    return true;
  }

  // checks if the blank cell are the same target and trophy color
  // as the crate
  public boolean sameColorTargetTrophyBC(BlankCell c) {
    return true;
  }

  // checks if the brick wall are the same target and trophy color
  // as the crate
  public boolean sameColorTargetTrophyBW(BrickWall c) {
    return true;
  }

  // checks if the player are the same target and trophy color
  // as the crate
  public boolean sameColorTargetTrophyPlayer(Player c) {
    return true;
  }

  // checks if the crate are the same target and trophy color
  // as the crate
  public boolean sameColorTargetTrophyCrate(Crate c) {
    return true;
  }

}

// represents a trophy in which the player should match with a target
class Trophy extends Movable {
  // the color of this trophy
  Color color;

  // the constructor
  Trophy(Posn pos, String color) {
    super(pos);
    if (color.equals("r")) {
      this.color = Color.RED;
    }
    else if (color.equals("b")) {
      this.color = Color.BLUE;
    }
    else if (color.equals("y")) {
      this.color = Color.YELLOW;
    }
    else if (color.equals("g")) {
      this.color = Color.GREEN;
    }
    else {
      throw new IllegalArgumentException("Trophy color does not exist.");
    }
  }
  /*
   * TEMPLATE: Fields: ... this.position ... -- Posn ... this.color ... -- Color
   * 
   * Methods: ... draw() ... -- WorldImage ... getPosition() ... -- Posn ...
   * isEmptyCell() ... -- boolean
   */

  // draws this trophy
  public WorldImage draw() {
    if (this.color.equals(Color.RED)) {
      return new FromFileImage("src/red_trophy.png");
    }
    else if (this.color.equals(Color.BLUE)) {
      return new FromFileImage("src/blue_trophy.png");
    }
    else if (this.color.equals(Color.YELLOW)) {
      return new FromFileImage("src/yellow_trophy.png");
    }
    else if (this.color.equals(Color.GREEN)) {
      return new FromFileImage("src/green_trophy.png");
    }
    else {
      throw new RuntimeException("Trophy color does not exist.");
    }
  }

  // checks if the content are the same target and trophy color
  // as the trophy
  public boolean sameColorTargetTrophy(Content c) {
    return c.sameColorTargetTrophyTrophy(this);
  }

  // checks if the target are the same target and trophy color
  // as the trophy
  public boolean sameColorTargetTrophyTarget(Target c) {
    return this.color.equals(c.color);
  }

  // checks if the trophy are the same target and trophy color
  // as the trophy
  public boolean sameColorTargetTrophyTrophy(Trophy c) {
    return true;
  }

  // checks if the blank ground are the same target and trophy color
  // as the trophy
  public boolean sameColorTargetTrophyBG(BlankGround c) {
    return true;
  }

  // checks if the blank cell are the same target and trophy color
  // as the trophy
  public boolean sameColorTargetTrophyBC(BlankCell c) {
    return true;
  }

  // checks if the brick wall are the same target and trophy color
  // as the trophy
  public boolean sameColorTargetTrophyBW(BrickWall c) {
    return true;
  }

  // checks if the player are the same target and trophy color
  // as the trophy
  public boolean sameColorTargetTrophyPlayer(Player c) {
    return true;
  }

  // checks if the crate are the same target and trophy color
  // as the trophy
  public boolean sameColorTargetTrophyCrate(Crate c) {
    return true;
  }
}

class ExamplesContent {
  //
  Content target = new Target(new Posn(1, 2), "R");
  Content player = new Player(new Posn(1, 2), "v");
  Content trophy = new Trophy(new Posn(1, 2), "r");
  Content blankGround = new BlankGround(new Posn(0, 0));

  // tests sameColorTargetTrophy
  boolean testSameColorTargetTrophy(Tester t) {
    return t.checkExpect(trophy.sameColorTargetTrophy(blankGround), false)
        && t.checkExpect(trophy.sameColorTargetTrophy(player), true)
        && t.checkExpect(trophy.sameColorTargetTrophy(target), true);
  }

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
}