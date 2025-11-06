//import java.util.ArrayList;
//import java.util.HashMap;
//import tester.*;
//import javalib.impworld.*;
//import javalib.worldimages.*;
//import java.awt.Color;
//
//// represent the information of a seam
//class SeamInfo {
//  IPixel pixel;
//  double totalWeight;
//  SeamInfo cameFrom;
//
//  // 用于顶层像素的构造函数
//  SeamInfo(IPixel pixel) {
//    this.pixel = pixel;
//    this.totalWeight = pixel.energy();
//    this.cameFrom = null;
//  }
//
//  // 用于连接路径的构造函数
//  SeamInfo(IPixel pixel, double pathWeight, SeamInfo previous) {
//    this.pixel = pixel;
//    this.totalWeight = pathWeight + pixel.energy();
//    this.cameFrom = previous;
//  }
//
//  // 移除整个seam路径上的所有像素
//  void removePixels() {
//    // 在移除像素之前，先递归到最底部
//    if (this.cameFrom != null) {
//      // 递归移除下一个像素
//      this.cameFrom.removePixels();
//    }
//    
//    // 在递归回溯过程中，从底部到顶部移除像素
//    this.pixel.remove();
//  }
//
//  // 计算整个图像中能量最小的seam
//  static SeamInfo findMinSeam(IPixel topLeft, int height, int width) {
//    // 使用动态规划方法计算最小能量路径
//    
//    // 初始化第一行的能量值
//    ArrayList<SeamInfo> currentRow = new ArrayList<>();
//    IPixel current = topLeft;
//    
//    // 填充第一行
//    for (int x = 0; x < width; x++) {
//      currentRow.add(new SeamInfo(current));
//      if (x < width - 1) {
//        current = current.getRight();
//      }
//    }
//    
//    // 处理剩余行
//    for (int y = 1; y < height; y++) {
//      ArrayList<SeamInfo> nextRow = new ArrayList<>();
//      
//      // 获取当前行的第一个像素（最左边）
//      current = currentRow.get(0).pixel.getBottom();
//      
//      for (int x = 0; x < width; x++) {
//        // 计算可能的前一个像素位置
//        int prevLeft = Math.max(0, x - 1);
//        int prevRight = Math.min(width - 1, x + 1);
//        
//        // 找到最小能量的前一个像素
//        double minEnergy = Double.MAX_VALUE;
//        SeamInfo minPrevSeam = null;
//        
//        for (int prev = prevLeft; prev <= prevRight; prev++) {
//          if (currentRow.get(prev).totalWeight < minEnergy) {
//            minEnergy = currentRow.get(prev).totalWeight;
//            minPrevSeam = currentRow.get(prev);
//          }
//        }
//        
//        // 创建新的SeamInfo并连接到最小能量的前一个像素
//        nextRow.add(new SeamInfo(current, minEnergy, minPrevSeam));
//        
//        // 移动到右侧像素
//        if (x < width - 1) {
//          current = current.getRight();
//        }
//      }
//      
//      // 更新当前行
//      currentRow = nextRow;
//    }
//    
//    // 找到最后一行中能量最小的seam
//    SeamInfo minSeam = currentRow.get(0);
//    for (int x = 1; x < width; x++) {
//      if (currentRow.get(x).totalWeight < minSeam.totalWeight) {
//        minSeam = currentRow.get(x);
//      }
//    }
//    
//    return minSeam;
//  }
//}
//
////class ExamplesSeam {
////  void testMinSeam(Tester t) {
////    Pixel p1 = new Pixel(Color.RED, 0, 0);
////    Pixel p2 = new Pixel(Color.BLUE, 1, 0);
////    Pixel p3 = new Pixel(Color.YELLOW, 1, 1);
////    Pixel p4 = new Pixel(Color.GREEN, 0, 1);
////
////    p1.setEdges(new BorderPixel(), // topLeft
////        new BorderPixel(), // top
////        new BorderPixel(), // topRight
////        new BorderPixel(), // left
////        p2, // right
////        new BorderPixel(), // bottomLeft
////        p3, // bottom
////        p4 // bottomRight
////    );
////
////    p2.setEdges(new BorderPixel(), // topLeft
////        new BorderPixel(), // top
////        new BorderPixel(), // topRight
////        p1, // left
////        new BorderPixel(), // right
////        p3, // bottomLeft
////        p4, // bottom
////        new BorderPixel() // bottomRight
////    );
////
////    p3.setEdges(new BorderPixel(), // topLeft
////        p1, // top
////        p2, // topRight
////        new BorderPixel(), // left
////        p4, // right
////        new BorderPixel(), // bottomLeft
////        new BorderPixel(), // bottom
////        new BorderPixel() // bottomRight
////    );
////
////    p4.setEdges(p1, // topLeft
////        p2, // top
////        new BorderPixel(), // topRight
////        p3, // left
////        new BorderPixel(), // right
////        new BorderPixel(), // bottomLeft
////        new BorderPixel(), // bottom
////        new BorderPixel() // bottomRight
////    );
////
////    SeamInfo seam = new SeamInfo(p1);
////    HashMap<IPixel, Double> memo = new HashMap<>();
////    SeamInfo minEnergy = seam.minSeam(2, 2, 0, memo);
////    
////    System.out.println("p1: " + p1.energy());
////    System.out.println("p2: " + p2.energy());
////    System.out.println("p3: " + p3.energy());
////    System.out.println("p4: " + p4.energy());
////
////    double expectedEnergy = p2.energy() + p3.energy();
////    t.checkInexact(minEnergy.totalWeight, expectedEnergy, 0.01);
////  }
////}