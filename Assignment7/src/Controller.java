import tester.*; // The tester library
import javalib.impworld.*;
import javalib.worldcanvas.*;
import javalib.worldimages.*;
import java.awt.Color;

// handles all the key press inputs and updates grid data
class Game extends World {
  // the width of the canvas
  static final int WIDTH = 1000;
  // the height of the canvas
  static final int HEIGHT = 1000;

  // the level of this game
  Level lvl;

  // the constructor
  Game(Level lvl) {
    this.lvl = lvl;
  }

  // moves the player based on the key pressed
  public void onKeyEvent(String key) {
    // check that key is valid to move player
    if (key.equals("up") || key.equals("down") || key.equals("left") || key.equals("right")) {
      this.lvl.movePlayer(key);
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
    return s;
  }
  
  public WorldScene lastScene(String msg) {
    WorldScene s = new WorldScene(WIDTH, HEIGHT);
    s.placeImageXY(new TextImage(msg, 80, Color.BLACK), WIDTH / 2, HEIGHT / 2);
    return s;
  }
}

class ExamplesGame{
  // the width of the canvas
  static final int WIDTH = 1000;
  // the height of the canvas
  static final int HEIGHT = 1000;
  String descriptionGround = "_______\n" + "__B____\n" + "____Y__\n" + "___R___\n" + "____G__\n" + "_____h_\n" + "_______";
  GridGround gridGround = new GridGround(descriptionGround);

  String descriptionContent = "WWWWWWW\n" + "W_____W\n" + "W_b>y_W\n" + "W_g_r_W\n" + "W___B_W\n" + "W_____W\n" + "WWWWWWW";
  GridContent gridContent = new GridContent(descriptionContent);

  Level exampleLevel = new Level(gridGround, gridContent);
  Game game = new Game(exampleLevel);

  void testBigBang(Tester t) {
    game.bigBang(WIDTH, HEIGHT, 1);
  }
  
}