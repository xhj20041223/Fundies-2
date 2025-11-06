//import java.util.ArrayList;
//import java.util.HashMap;
//import tester.*;
//import javalib.impworld.*;
//import javalib.worldimages.*;
//import java.awt.Color;
//
//interface IPixel {
//  double brightness();
//
//  void recomputeEnergy();
//
//  double energy();
//
//  Color getColor();
//
//  Pixel pixelAt(int x, int y, int curX, int curY);
//
//  void drawOn(ComputedPixelImage image, int curX, int curY);
//
//  void drawOnCol(ComputedPixelImage image, int curX, int curY);
//
//  void setTopLeft(IPixel pixel);
//
//  void setTop(IPixel pixel);
//
//  void setTopRight(IPixel pixel);
//
//  void setRight(IPixel pixel);
//
//  void setBottomRight(IPixel pixel);
//
//  void setBottom(IPixel pixel);
//
//  void setBottomLeft(IPixel pixel);
//
//  void setLeft(IPixel pixel);
//
//  IPixel getRight();
//
//  IPixel getBottomLeft();
//
//  IPixel getBottomRight();
//
//  IPixel getBottom();
//
//  void remove();
//}
//
//class BorderPixel implements IPixel {
//
//  public void recomputeEnergy() {
//    // does nothing because this pixel will never get updated
//  }
//
//  public double brightness() {
//    return 0;
//  }
//
//  public double energy() {
//    return 0;
//  }
//
//  public Color getColor() {
//    return Color.BLACK;
//  }
//
//  public Pixel pixelAt(int x, int y, int curX, int curY) {
//    throw new IllegalArgumentException();
//  }
//
//  public void drawOn(ComputedPixelImage image, int curX, int curY) {
//    // does nothing as this pixel cannot be drawn
//  }
//
//  public void drawOnCol(ComputedPixelImage image, int curX, int curY) {
//    // does nothing as this pixel cannot be drawn
//  }
//
//  public void setTopLeft(IPixel pixel) {
//    // do nothing since this class doesn't have any references to set
//  }
//
//  public void setTop(IPixel pixel) {
//    // do nothing since this class doesn't have any references to set
//  }
//
//  public void setTopRight(IPixel pixel) {
//    // do nothing since this class doesn't have any references to set
//  }
//
//  public void setRight(IPixel pixel) {
//    // do nothing since this class doesn't have any references to set
//  }
//
//  public void setBottomRight(IPixel pixel) {
//    // do nothing since this class doesn't have any references to set
//  }
//
//  public void setBottom(IPixel pixel) {
//    // do nothing since this class doesn't have any references to set
//  }
//
//  public void setBottomLeft(IPixel pixel) {
//    // do nothing since this class doesn't have any references to set
//  }
//
//  public void setLeft(IPixel pixel) {
//    // do nothing since this class doesn't have any references to set
//  }
//
//  public IPixel getBottom() {
//    return this;
//  }
//
//  public IPixel getBottomLeft() {
//    return this;
//  }
//
//  public IPixel getBottomRight() {
//    return this;
//  }
//
//  public IPixel getRight() {
//    return this;
//  }
//
//  public void remove() {
//    // Does nothing as this pixel cannot be removed
//  }
//
//}
//
//class Pixel implements IPixel {
//  Color color;
//
//  IPixel topLeft;
//  IPixel top;
//  IPixel topRight;
//  IPixel left;
//  IPixel right;
//  IPixel bottomLeft;
//  IPixel bottom;
//  IPixel bottomRight;
//  double energy;
//
//  Pixel(Color color, int x, int y) {
//    this.color = color;
//  }
//
//  public Color getColor() {
//    return this.color;
//  }
//
//  public double verticalEnergy() {
//    return (this.topRight.brightness() + 2 * this.top.brightness() + this.topLeft.brightness())
//        - (this.bottomRight.brightness() + 2 * this.bottom.brightness()
//            + this.bottomLeft.brightness());
//  }
//
//  public double horizontalEnergy() {
//    return (this.topLeft.brightness() + 2 * this.left.brightness() + this.bottomLeft.brightness())
//        - (this.topRight.brightness() + 2 * this.right.brightness()
//            + this.bottomRight.brightness());
//  }
//
//  public double brightness() {
//    return ((double) (this.color.getBlue() + this.color.getGreen() + this.color.getRed()))
//        / (255.0 * 3);
//  }
//
//  public void recomputeEnergy() {
//    this.energy = Math
//        .sqrt(Math.pow(this.horizontalEnergy(), 2) + Math.pow(this.verticalEnergy(), 2));
//  }
//
//  public double energy() {
//    return this.energy;
//  }
//
//  public Pixel pixelAt(int x, int y, int curX, int curY) {
//
//    if (curX > x) {
//      return this.left.pixelAt(x, y, curX - 1, curY);
//    }
//
//    if (curX < x) {
//      return this.right.pixelAt(x, y, curX + 1, curY);
//    }
//
//    if (curY > y) {
//      return this.top.pixelAt(x, y, curX, curY - 1);
//    }
//
//    if (curY < y) {
//      return this.bottom.pixelAt(x, y, curX, curY + 1);
//    }
//
//    return this;
//  }
//
//  public void drawOn(ComputedPixelImage image, int curX, int curY) {
//    image.setPixel(curX, curY, this.color);
//    this.bottom.drawOnCol(image, curX, curY + 1);
//    this.right.drawOn(image, curX + 1, curY);
//
//  }
//
//  public void drawOnCol(ComputedPixelImage image, int curX, int curY) {
//    image.setPixel(curX, curY, this.color);
//    this.bottom.drawOnCol(image, curX, curY + 1);
//  }
//
//  void setEdges(IPixel topLeft, IPixel top, IPixel topRight, IPixel left, IPixel right,
//      IPixel bottomLeft, IPixel bottom, IPixel bottomRight) {
//    this.topLeft = topLeft;
//    this.top = top;
//    this.topRight = topRight;
//    this.left = left;
//    this.right = right;
//    this.bottomLeft = bottomLeft;
//    this.bottom = bottom;
//    this.bottomRight = bottomRight;
//    this.recomputeEnergy();
//  }
//
//  public void setTopLeft(IPixel pixel) {
//    this.topLeft = pixel;
//    this.recomputeEnergy();
//  }
//
//  public void setTop(IPixel pixel) {
//    this.top = pixel;
//    this.recomputeEnergy();
//  }
//
//  public void setTopRight(IPixel pixel) {
//    this.topRight = pixel;
//    this.recomputeEnergy();
//  }
//
//  public void setRight(IPixel pixel) {
//    this.right = pixel;
//    this.recomputeEnergy();
//  }
//
//  public void setBottomRight(IPixel pixel) {
//    this.bottomRight = pixel;
//    this.recomputeEnergy();
//  }
//
//  public void setBottom(IPixel pixel) {
//    this.bottom = pixel;
//    this.recomputeEnergy();
//  }
//
//  public void setBottomLeft(IPixel pixel) {
//    this.bottomLeft = pixel;
//    this.recomputeEnergy();
//  }
//
//  public void setLeft(IPixel pixel) {
//    this.left = pixel;
//    this.recomputeEnergy();
//  }
//
//  public IPixel getBottom() {
//    return this.bottom;
//  }
//
//  public IPixel getBottomLeft() {
//    return this.bottomLeft;
//  }
//
//  public IPixel getBottomRight() {
//    return this.bottomRight;
//  }
//
//  public IPixel getRight() {
//    return this.right;
//  }
//
//  public void remove() {
//    // 当移除一个像素时，我们需要让右边的像素向左移动，保持图像结构
//    
//    // 水平连接 - 左侧和右侧像素直接连接
//    this.left.setRight(this.right);
//    this.right.setLeft(this.left);
//    
//    // 垂直连接 - 将上下连接重定向到当前像素的右侧
//    this.top.setBottom(this.right);
//    this.right.setTop(this.top);
//    
//    this.bottom.setTop(this.right);
//    this.right.setBottom(this.bottom);
//    
//    // 对角线连接
//    // 左上到右下
//    this.topLeft.setBottomRight(this.right);
//    this.right.setTopLeft(this.topLeft);
//    
//    // 左下到右上
//    this.bottomLeft.setTopRight(this.right);
//    this.right.setBottomLeft(this.bottomLeft);
//    
//    // 重新计算受影响像素的能量
//    this.left.recomputeEnergy();
//    this.right.recomputeEnergy();
//    this.top.recomputeEnergy();
//    this.bottom.recomputeEnergy();
//    this.topLeft.recomputeEnergy();
//    this.topRight.recomputeEnergy();
//    this.bottomLeft.recomputeEnergy();
//    this.bottomRight.recomputeEnergy();
//  }
//  
//
//}
//
//class Image {
//  Pixel topLeft;
//  int width;
//  int height;
//
//  Image(String fileName) {
//    FromFileImage baseImage = new FromFileImage(fileName);
//    this.width = (int) baseImage.getWidth();
//    this.height = (int) baseImage.getHeight();
//
//    ArrayList<ArrayList<Pixel>> pixelGrid = this.constructGrid(baseImage, baseImage.getWidth(),
//        baseImage.getHeight());
//    this.topLeft = pixelGrid.get(0).get(0);
//  }
//
//  ArrayList<ArrayList<Pixel>> constructGrid(FromFileImage image, double width, double height) {
//    // pixels.get(xCoord).get(yCoord) to get a pixel
//    ArrayList<ArrayList<Pixel>> pixels = new ArrayList<ArrayList<Pixel>>();
//
//    for (int xPos = 0; xPos < width; xPos += 1) {
//      ArrayList<Pixel> colPixels = new ArrayList<Pixel>();
//
//      for (int yPos = 0; yPos < height; yPos += 1) {
//        colPixels.add(new Pixel(image.getColorAt(xPos, yPos), xPos, yPos));
//      }
//      pixels.add(colPixels);
//    }
//
//    for (int xPos = 0; xPos < width; xPos += 1) {
//
//      for (int yPos = 0; yPos < height; yPos += 1) {
//
//        IPixel topLeft = (xPos > 0 && yPos > 0) ? (pixels.get(xPos - 1).get(yPos - 1))
//            : new BorderPixel();
//
//        IPixel top = (yPos > 0) ? (pixels.get(xPos).get(yPos - 1)) : new BorderPixel();
//
//        IPixel topRight = (xPos < width - 1 && yPos > 0) ? (pixels.get(xPos + 1).get(yPos - 1))
//            : new BorderPixel();
//
//        IPixel right = (xPos < width - 1) ? (pixels.get(xPos + 1).get(yPos)) : new BorderPixel();
//
//        IPixel bottomRight = (xPos < width - 1 && yPos < height - 1)
//            ? (pixels.get(xPos + 1).get(yPos + 1))
//            : new BorderPixel();
//
//        IPixel bottom = (yPos < height - 1) ? (pixels.get(xPos).get(yPos + 1)) : new BorderPixel();
//
//        IPixel bottomLeft = (xPos > 0 && yPos < height - 1) ? (pixels.get(xPos - 1).get(yPos + 1))
//            : new BorderPixel();
//
//        IPixel left = (xPos > 0) ? (pixels.get(xPos - 1).get(yPos)) : new BorderPixel();
//        pixels.get(xPos).get(yPos).setEdges(topLeft, top, topRight, left, right, bottomLeft, bottom,
//            bottomRight);
//      }
//    }
//
//    return pixels;
//
//  }
//
//  int width() {
//    return this.width;
//  }
//
//  int height() {
//    return this.height;
//  }
//
//  WorldImage draw() {
//    ComputedPixelImage result = new ComputedPixelImage(this.width, this.height);
//
//    this.topLeft.drawOn(result, 0, 0);
//    return result;
//  }
//
//  void removeLowestSeam() {
//    // 使用新的SeamInfo.findMinSeam静态方法找到最小能量的seam
//    SeamInfo lowestSeam = SeamInfo.findMinSeam(this.topLeft, this.height, this.width);
//    
//    // 移除找到的seam
//    lowestSeam.removePixels();
//    
//    // 更新图像宽度
//    this.width--;
//  }
//}
//class ExamplesPixels {
//  void testCurvedSeamRemoval(Tester t) {
//    // 创建一个4x3的像素网格
//    /*
//     *   A  B  C  D  (0,0) (1,0) (2,0) (3,0)
//     *   E  F  G  H  (0,1) (1,1) (2,1) (3,1)
//     *   I  J  K  L  (0,2) (1,2) (2,2) (3,2)
//     */
//    Pixel a = new Pixel(Color.RED, 0, 0);
//    Pixel b = new Pixel(Color.RED, 1, 0);
//    Pixel c = new Pixel(Color.RED, 2, 0);
//    Pixel d = new Pixel(Color.RED, 3, 0);
//    
//    Pixel e = new Pixel(Color.RED, 0, 1);
//    Pixel f = new Pixel(Color.RED, 1, 1);
//    Pixel g = new Pixel(Color.RED, 2, 1);
//    Pixel h = new Pixel(Color.RED, 3, 1);
//    
//    Pixel i = new Pixel(Color.RED, 0, 2);
//    Pixel j = new Pixel(Color.RED, 1, 2);
//    Pixel k = new Pixel(Color.RED, 2, 2);
//    Pixel l = new Pixel(Color.RED, 3, 2);
//    
//    // 设置所有像素的连接
//    // 第一行
//    a.setEdges(new BorderPixel(), new BorderPixel(), new BorderPixel(), 
//               new BorderPixel(), b, new BorderPixel(), e, f);
//    
//    b.setEdges(new BorderPixel(), new BorderPixel(), new BorderPixel(), 
//               a, c, e, f, g);
//    
//    c.setEdges(new BorderPixel(), new BorderPixel(), new BorderPixel(), 
//               b, d, f, g, h);
//    
//    d.setEdges(new BorderPixel(), new BorderPixel(), new BorderPixel(), 
//               c, new BorderPixel(), g, h, new BorderPixel());
//    
//    // 第二行
//    e.setEdges(a, b, c, new BorderPixel(), f, new BorderPixel(), i, j);
//    
//    f.setEdges(a, b, c, e, g, i, j, k);
//    
//    g.setEdges(b, c, d, f, h, j, k, l);
//    
//    h.setEdges(c, d, new BorderPixel(), g, new BorderPixel(), k, l, new BorderPixel());
//    
//    // 第三行
//    i.setEdges(e, f, g, new BorderPixel(), j, new BorderPixel(), new BorderPixel(), new BorderPixel());
//    
//    j.setEdges(e, f, g, i, k, new BorderPixel(), new BorderPixel(), new BorderPixel());
//    
//    k.setEdges(f, g, h, j, l, new BorderPixel(), new BorderPixel(), new BorderPixel());
//    
//    l.setEdges(g, h, new BorderPixel(), k, new BorderPixel(), new BorderPixel(), new BorderPixel(), new BorderPixel());
//    
//    // 模拟曲线接缝：移除 B -> G -> J
//    b.remove();
//    g.remove();
//    j.remove();
//    
//    // 验证水平连接
//    // 第一行：A应该直接连接到C
//    t.checkExpect(a.right, c);
//    t.checkExpect(c.left, a);
//    
//    // 第二行：F应该直接连接到H
//    t.checkExpect(f.right, h);
//    t.checkExpect(h.left, f);
//    
//    // 第三行：I应该直接连接到K
//    t.checkExpect(i.right, k);
//    t.checkExpect(k.left, i);
//    
//    // 验证垂直连接
//    // A应该连接到F（而不是E）
//    t.checkExpect(a.bottom, f);
//    t.checkExpect(f.top, a);
//    
//    // C应该连接到H（而不是G）
//    t.checkExpect(c.bottom, h);
//    t.checkExpect(h.top, c);
//    
//    // F应该连接到K（而不是J）
//    t.checkExpect(f.bottom, k);
//    t.checkExpect(k.top, f);
//  }
//  Image ice = new Image("src/Ice.png");
//
//  WorldImage renderedIce = ice.draw();
//}
