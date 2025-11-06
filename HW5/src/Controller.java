import tester.*; // The tester library
import javalib.funworld.*;

// handles all the key press inputs and updates grid data
class Game extends World {
  Level lvl;

  Game(Level lvl) {
    this.lvl = lvl;
  }
  /*
   * TEMPLATE
   * FIELDS:
   * ... this.lvl ...  -- Level
   * METHODS:
   * ... this.onKeyEvent(String key) -- World
   * ... this.makeScene() ... -- WorldScene
   * METHODS ON PARAMETERS:
   * ... this.lvl.movePlayerUp ... -- Level
   * ... this.lvl.movePlayerDown ... -- Level
   * ... this.lvl.movePlayerLeft ... -- Level
   * ... this.lvl.movePlayerRight ... -- Level
   */

  // handles all the key inputs
  public World onKeyEvent(String key) {
    if (key.equals("up")) {
      return new Game(this.lvl.movePlayerUp());
    }
    else if (key.equals("down")) {
      return new Game(this.lvl.movePlayerDown());
    }
    else if (key.equals("left")) {
      return new Game(this.lvl.movePlayerLeft());
    }
    else if (key.equals("right")) {
      return new Game(this.lvl.movePlayerRight());
    }
    else {
      return this;
    }
  }

  // draws the level
  public WorldScene makeScene() {
    return new WorldScene(1000, 1000).placeImageXY(this.lvl.render(), 500, 500);
  }
}

class ExamplesGame {
  boolean testBigBang(Tester t) {
    String exampleLevelGround = "________\n" + "___R____\n" + "________\n" + "_B____Y_\n"
        + "________\n" + "___G____\n" + "________";
    String exampleLevelContent = "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "W_b>yB_W\n"
        + "WW_gWWWW\n" + "_WW_W___\n" + "__WWW___";
    World sokobanLevel = new Game(new Level(exampleLevelGround, exampleLevelContent));
    int worldWidth = 1000;
    int worldHeight = 1000;
    double tickRate = 1.0;
    return sokobanLevel.bigBang(worldWidth, worldHeight, tickRate);
  }
}