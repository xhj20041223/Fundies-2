//import java.util.ArrayList;
//import java.util.HashMap;
//import tester.*;
//import javalib.impworld.*;
//import javalib.worldimages.*;
//import java.awt.Color;
//
//// represent a process to squeeze a picture
//class SqueezingPictures extends World {
//  Image image;
//
//  SqueezingPictures(Image image) {
//    this.image = image;
//  }
//
//  // return the scene of the image
//  public WorldScene makeScene() {
//    WorldScene s = new WorldScene(this.image.width(), this.image.height());
//    s.placeImageXY(this.image.draw(), this.image.width() / 2, this.image.height() / 2);
//    return s;
//  }
//
//  // EFFECT: squeeze the picture by delete a seam which have the lowest energy
//  public void onTick() {
//    this.image.removeLowestSeam();
//  }
//}
//
//class ExamplesSqueezingPictures {
//  
//  
//  Image image;
//  SqueezingPictures squeezingPictures;
//
//  void init() {
//     this.image = new Image("src/Nature.jpg");
//     this.squeezingPictures = new SqueezingPictures(image);
//     System.out.println(this.image.topLeft.color);
//  }
//  
////  void testImageDimensions(Tester t) {
////    this.init();
////    t.checkExpect(this.image.width(), 630);
////    t.checkExpect(this.image.height(), 120);
////  }
//  void testBigBang(Tester t) {
//    this.init();
//    squeezingPictures.bigBang(image.width, image.height, 1);
//    
//  }
//}