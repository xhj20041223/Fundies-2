import tester.*; // The tester library
import javalib.worldimages.*;
import javalib.funworld.*;
import javalib.worldcanvas.WorldCanvas;
import java.util.ArrayList;

// represents a Sokoban level
class Level {
  // ground grid
  GridGround groundGrid;

  // content grid
  GridContent contentGrid;

  // the game
  Game game;

  // the scene before the move happened
  ArrayList<Level> lastScene;
  
  // count the total steps the player used
  int steps = 0;
  
  // the constructor
  Level() {
    this.groundGrid = new GridGround();
    this.contentGrid = new GridContent();
    this.contentGrid.ices = this.groundGrid.ices;
    this.lastScene = new ArrayList<Level>();
  }

  // constructor that takes in two lists of lists
  Level(GridGround groundGrid, GridContent contentGrid) {
    this.groundGrid = groundGrid;
    this.contentGrid = contentGrid;
    this.contentGrid.ices = this.groundGrid.ices;
    this.lastScene = new ArrayList<Level>();
  }
  
  // constructor that takes in all fields
  Level(GridGround groundGrid, GridContent contentGrid, ArrayList<Level> lastScene) {
    this.groundGrid = groundGrid;
    this.contentGrid = contentGrid;
    this.contentGrid.ices = this.groundGrid.ices;
    this.lastScene = lastScene;
  }

  // constructor that takes in String and generates grid for level
  Level(String groundContent, String cellContent) {
    this.groundGrid = new GridGround(groundContent);
    this.contentGrid = new GridContent(cellContent);
    this.contentGrid.ices = this.groundGrid.ices;
    lastScene = new ArrayList<Level>();
  }
  
  // clone the Level
  Level(Level other) {
    this.groundGrid = new GridGround(other.groundGrid);
    this.contentGrid = new GridContent(other.contentGrid);
    this.contentGrid.ices = this.groundGrid.ices;
    this.lastScene = new ArrayList<>(other.lastScene);
    this.steps = other.steps;
  }


  /* TEMPLATE: 
  * Fields:
  * ... this.groundGrid ...  -- GridGround 
  * ... this.contentGrid ... -- GridContent
  * ... this.game ...        -- Game
  * ... this.steps ...       -- int
  * 
  * Methods:
  * ... render() ... -- WorldScene 
  * ... levelWon() ... -- boolean 
  * ... levelFail() ... -- boolean 
  * ... movePlayer(String direction)... -- void
  * 
  * Methods for fields: 
  * ... this.game.onKeyEvent(String key) ... -- void 
  * ... this.game.makeScene() ... -- WorldScene 
  * ... this.game.lastScene() ... -- WorldScene 
  * ... this.groundGrid.drawGrid() ... -- WorldImage 
  * ... this.groundGrid.createRows(ILoString, CellFactory) ... -- ArrayList<Row>
  * ... this.groundGrid.allTargetsFilled(GridContent) ... -- boolean 
  * ... this.groundGrid.noEnoughtTrophies(GridContent contents) ... -- boolean 
  * ... this.groundGrid.isOnHole(CellContent c) ... -- boolean 
  * ... this.groundGrid.removeObjectsOnHoles(GridContent content) ... -- void 
  * ... this.contentGrid.drawGrid() ... -- WorldImage 
  * ... this.contentGrid.createRows(ILoString, CellFactory) ... -- ArrayList<Row> 
  * ... this.contentGrid.movePlayer(String direction) ... -- void 
  * ... this.contentGrid.moveContent(Movable content, String direction) ... -- void 
  * ... this.contentGrid.swapContents(Posn pos1, Posn pos2) ... -- void 
  * ... this.contentGrid.removeObjectsOnHoles(GridContent content) ... -- void 
  * ... this.contentGrid.canMove(Posn objPos, String direction) ... -- boolean 
  * ... this.contentGrid.existTargetTrophy(Posn posn, String color) ... -- boolean 
  * ... this.contentGrid.noEnoughtTrophiesHelp(ArrayList<Target> targets) ... -- boolean 
  * ... this.contentGrid.existSuchTrophy(String color) ... -- boolean 
  * ... this.contentGrid.playerDied(GridGround ground) ... -- boolean 
  * ... this.contentGrid.isNotEmptyAt(Posn pos) ... -- boolean 
  * ... this.contentGrid.findPlayer() ... -- Player
  */

  // renders level into an image
  WorldImage render() {
    return new OverlayImage(contentGrid.drawGrid(), groundGrid.drawGrid());
  }

  // checks whether the player has passed this level
  boolean levelWon() {
    return this.groundGrid.allTargetsFilled(this.contentGrid);
  }
  
  // checks whether the player failed this level
  boolean levelFail() {
    return !this.groundGrid.playerAlive;
  }

  // updates grid with player moved in the specified direction if possible
  void movePlayer(String direction) {
    this.lastScene.add(new Level(this));
    if (this.contentGrid.moveDriver(direction)) {
      this.steps++;
      for (Level levels : this.lastScene) {
        levels.steps += 1;
      }
    }
    this.groundGrid.removeObjectsOnHoles(this.contentGrid);
  }
  
  // undo a move and increase the steps count
  void undo() {
    if (!lastScene.isEmpty()) {
      Level previousState = this.lastScene.remove(this.lastScene.size() - 1);
      
      this.groundGrid = new GridGround(previousState.groundGrid);
      this.contentGrid = new GridContent(previousState.contentGrid);
      this.steps = previousState.steps;
      this.lastScene = new ArrayList<>(previousState.lastScene);
      this.steps++;
      for (Level levels : this.lastScene) {
        levels.steps += 1;
      }
    }
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