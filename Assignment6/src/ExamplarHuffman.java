//import huffman.*;
//
//import java.util.ArrayList;
//import tester.Tester;
//
//class ExamplesHuffman {
//  ArrayList<String> MtLoStr1;
//  ArrayList<Integer> MtLoInt1;
//
//  ArrayList<String> LoStr1;
//  ArrayList<Integer> LoInt1;
//
//  ArrayList<String> LoStr2;
//  ArrayList<Integer> LoInt2;
//
//  ArrayList<String> LoStr3;
//  ArrayList<Integer> LoInt3;
//
//  Huffman h1;
//  Huffman h2;
//
//  void InitiHuffman() {
//    MtLoStr1 = new ArrayList<String>();
//    MtLoInt1 = new ArrayList<Integer>();
//
//    LoStr1 = new ArrayList<String>();
//    LoInt1 = new ArrayList<Integer>();
//
//    LoStr2 = new ArrayList<String>();
//    LoInt2 = new ArrayList<Integer>();
//
//    LoStr3 = new ArrayList<String>();
//    LoInt3 = new ArrayList<Integer>();
//    
//    LoStr1.add("a");
//    LoInt1.add(0);
//
//    LoStr2.add("a");
//    LoInt2.add(5);
//    LoStr2.add("b");
//    LoInt2.add(6);
//    LoStr2.add("c");
//    LoInt2.add(11);
//
//    LoStr3.add("a");
//    LoInt3.add(30);
//    LoStr3.add("b");
//    LoInt3.add(6);
//    LoStr3.add("c");
//    LoInt3.add(6);
//    LoStr3.add("d");
//    LoInt3.add(20);
//    LoStr3.add("e");
//    LoInt3.add(20);
//    
//    h1 = new Huffman(LoStr2, LoInt2);
//    h2 = new Huffman(LoStr3, LoInt3);
//  }
//
////  void testConstructor(Tester t) {
////    this.InitiHuffman();
////    t.checkConstructorExceptionType(IllegalArgumentException.class, "Huffman", MtLoStr1, MtLoInt1);
////    t.checkConstructorExceptionType(IllegalArgumentException.class, "Huffman", LoStr1, LoInt1);
////    t.checkConstructorExceptionType(IllegalArgumentException.class, "Huffman", LoStr1, LoInt2);
////    t.checkConstructorExceptionType(IllegalArgumentException.class, "Huffman", LoStr2, LoInt3);
////    t.checkConstructorExceptionType(IllegalArgumentException.class, "Huffman", LoStr3, LoInt2);
////    t.checkConstructorExceptionType(IllegalArgumentException.class, "Huffman", LoStr3, MtLoInt1);
////  }
////
////  void testEncodeException(Tester t) {
////    this.InitiHuffman();
////    t.checkException(
////        new IllegalArgumentException("Tried to encode f but that is not part of the language."), h2,
////        "encode", "fabc!");
////    t.checkException(
////        new IllegalArgumentException("Tried to encode @ but that is not part of the language."), h2,
////        "encode", "@afc!");
////  }
//  
//  void testEncodeSame(Tester t) {
//    this.InitiHuffman();
//    ArrayList<Boolean> encode2 = new ArrayList<Boolean>();
//    
//    encode2.add(true);
//    encode2.add(true);
//    encode2.add(true);
//    encode2.add(true);
//    encode2.add(true);
//    encode2.add(true);
//    t.checkExpect(h2.encode("aaa"), encode2);
//  }
//  
////  void testEncodeSimple(Tester t) {
////    this.InitiHuffman();
////    ArrayList<Boolean> encode1 = new ArrayList<Boolean>();
////    encode1.add(true);
////    encode1.add(false);
////    encode1.add(true);
////    encode1.add(true);
////    t.checkExpect(h1.encode("ab"), encode1);
////  }
//  
//  void testEncode(Tester t) {
//    this.InitiHuffman();
//    ArrayList<Boolean> encode1 = new ArrayList<Boolean>();
//    encode1.add(true);
//    encode1.add(true);
//    encode1.add(false);
//    encode1.add(false);
//    encode1.add(false);
//    encode1.add(false);
//    encode1.add(false);
//    encode1.add(true);
//    encode1.add(false);
//    encode1.add(true);
//    
//    t.checkExpect(h2.encode("abcd"), encode1);
//  }
//
//  void testDecodeIncomplete(Tester t) {
//    this.InitiHuffman();
//    ArrayList<Boolean> encode1 = new ArrayList<Boolean>();
//    encode1.add(true);
//    encode1.add(true);
//    encode1.add(false);
//    encode1.add(false);
//    encode1.add(false);
//    encode1.add(false);
//    encode1.add(false);
//    encode1.add(true);
//    encode1.add(false);
//    
//    t.checkExpect(h2.decode(encode1), "abc?");
//  }
//  
//  void testOnlyQuestion(Tester t){
//    this.InitiHuffman();
//    ArrayList<Boolean> encode1 = new ArrayList<Boolean>();
//    encode1.add(false);
//    encode1.add(true);
//    encode1.add(true);
//    t.checkExpect(h1.decode(encode1), "cb");
//  }
//
////  void testDecodeSimple(Tester t) {
////    this.InitiHuffman();
////    ArrayList<Boolean> encode1 = new ArrayList<Boolean>();
////    encode1.add(true);
////    t.checkExpect(h1.decode(encode1), "?");
////  }
//
//  
//  void testDecode(Tester t) {
//    this.InitiHuffman();
//    ArrayList<Boolean> encode1 = new ArrayList<Boolean>();
//    encode1.add(true);
//    encode1.add(true);
//    encode1.add(false);
//    encode1.add(false);
//    encode1.add(false);
//    encode1.add(false);
//    encode1.add(false);
//    encode1.add(true);
//    encode1.add(false);
//    encode1.add(true);
//    
//    t.checkExpect(h2.decode(encode1), "abcd");
//  }
//}