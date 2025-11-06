import tester.*; // The tester library
import javalib.impworld.*;
import javalib.worldimages.*;
import java.awt.Color;

// handles all the key press inputs and updates grid data
class Game extends World {
  // the width of the canvas
  static final int WIDTH = 1200;
  // the height of the canvas
  static final int HEIGHT = 800;
  
  // the total count of the steps
  int steps;

  // the level of this game
  Level lvl;

  // the constructor
  Game(Level lvl) {
    this.lvl = lvl;
    this.steps = lvl.steps;
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.WIDTH ...         -- int 
   * ... this.HEIGHT ...        -- int
   * ... this.lvl ...           -- Level
   * ... this.steps ...         -- int
   * 
   * Methods: 
   * ... onKeyEvent(String key) ...        -- void
   * ... makeScene() ...                   -- WorldScene
   * ... lastScene() ...                   -- WorldScene
   * 
   * Methods for fields: 
   * ... this.lvl.render() ...                       -- WorldScene
   * ... this.lvl.levelWon() ...                     -- boolean
   * ... this.lvl.levelFail() ...                    -- boolean
   * ... this.lvl.movePlayer(String direction)...    -- void
   */

  // moves the player based on the key pressed
  public void onKeyEvent(String key) {
    // check that key is valid to move player
    if (key.equals("up") || key.equals("down") || key.equals("left") || key.equals("right")) {
      this.lvl.movePlayer(key);
    }
    else if (key.equals("u")) {
      this.lvl.undo();
    }
    if (this.lvl.levelWon()) {
      this.endOfWorld("You Win");
    }
    else if (this.lvl.levelFail()) {
      this.endOfWorld("Game Over");
    }
  }

  // draw the game
  public WorldScene makeScene() {
    WorldScene s = new WorldScene(WIDTH, HEIGHT);
    s.placeImageXY(this.lvl.render(), WIDTH / 2, HEIGHT / 2);
    s.placeImageXY(new TextImage("Steps: " + this.lvl.steps, 24, Color.BLACK), 50, 25);
    return s;
  }

  public WorldScene lastScene(String msg) {
    WorldScene s = new WorldScene(WIDTH, HEIGHT);
    s.placeImageXY(new TextImage(msg, 80, Color.BLACK), WIDTH / 2, HEIGHT / 2);
    s.placeImageXY(new TextImage("Steps: " + 
        this.lvl.steps, 24, Color.BLACK), WIDTH / 2, HEIGHT / 2 + 50);
    return s;
  }
}

class ExamplesGame {
  // the width of the canvas
  static final int WIDTH = 1200;
  // the height of the canvas
  static final int HEIGHT = 800;

  String descriptionGround = "________\n" + "____B___\n" + "____Y___\n" + "__i_i___\n"
      + "____i___\n" + "_ii_____\n" + "______h_\n" + "________";
  GridGround gridGround = new GridGround(descriptionGround);

  String descriptionContent = "WWWWWWWW\n" + "W______W\n" + "W___>__W\n" + "W___by_W\n" 
      + "W______W\n" + "W____B_W\n" + "W______W\n"  + "WWWWWWWW";
  GridContent gridContent = new GridContent(descriptionContent);

  Level exampleLevel = new Level(gridGround, gridContent);
  Game game = new Game(exampleLevel);

  void testBigBang(Tester t) {
    game.bigBang(WIDTH, HEIGHT, 1);
  }
}
