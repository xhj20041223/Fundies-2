import tester.*; // The tester library
import javalib.worldimages.*;
import javalib.funworld.*;
import javalib.worldcanvas.WorldCanvas;

// represents a Sokoban level
public class Level {
  // ground grid
  GridGround groundGrid;

  // content grid
  GridContent contentGrid;

  // the game
  Game game;

  // the constructor
  Level() {
    this.groundGrid = new GridGround();
    this.contentGrid = new GridContent();
  }

  // constructor that takes in two lists of lists
  Level(GridGround groundGrid, GridContent contentGrid) {
    this.groundGrid = groundGrid;
    this.contentGrid = contentGrid;
  }

  // constructor that takes in String and generates grid for level
  Level(String groundContent, String cellContent) {
    this.groundGrid = new GridGround(groundContent);
    this.contentGrid = new GridContent(cellContent);
  }

  // renders level into an image
  WorldImage render() {
    return new OverlayImage(contentGrid.drawGrid(), groundGrid.drawGrid());
  }

  // checks whether the player has passed this level
  boolean levelWon() {
    return this.groundGrid.allTargetsFilled(this.contentGrid);
  }
  
  boolean levelFail() {
    return this.groundGrid.noEnoughtTrophies(this.contentGrid) || this.contentGrid.playerDied(groundGrid);
  }

  // updates grid with player moved in the specified direction if possible 
  void movePlayer(String direction) {
    contentGrid.movePlayer(direction);
    groundGrid.removeObjectsOnHoles(this.contentGrid);
  }
}

class ExamplesLevel {
  
  //
  // draws level board
  boolean testDrawGridGround(Tester t) {
    WorldCanvas c = new WorldCanvas(1250, 750);
    WorldScene s = new WorldScene(1250, 750);
    String descriptionGround = "B_Y\n" + "___\n" + "_G_";
    GridGround gridGround = new GridGround(descriptionGround);

    String descriptionContent = "_W_\n" + "b>y\n" + "_gB";
    GridContent gridContent = new GridContent(descriptionContent);

    Level exampleLevel = new Level(gridGround, gridContent);

    return c.drawScene(s.placeImageXY(exampleLevel.render(), 625, 375)) && c.show();
  }
}
