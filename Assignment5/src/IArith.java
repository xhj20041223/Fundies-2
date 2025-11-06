import tester.Tester;
import java.util.function.*;

// an arithmetic expression
interface IArith {

  // returns the result of applying the given visitor to this arithmetic
  // expression
  <R> R accept(IArithVisitor<R> visitor);
}

// a constant
class Const implements IArith {
  double num;

  public Const(double num) {
    this.num = num;
  }

  // returns the result of applying the given visitor to this constant
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitConst(this);
  }
}

// a unary formula
class UnaryFormula implements IArith {
  Function<Double, Double> func;
  String name;
  IArith child;

  public UnaryFormula(Function<Double, Double> func, String name, IArith child) {
    this.func = func;
    this.name = name;
    this.child = child;
  }

  // returns the result of applying the given visitor to this unary formula
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitUnaryFormula(this);
  }
}

// a binary formula
class BinaryFormula implements IArith {
  BiFunction<Double, Double, Double> func;
  String name;
  IArith left;
  IArith right;

  public BinaryFormula(BiFunction<Double, Double, Double> func, String name, IArith left,
      IArith right) {
    this.func = func;
    this.name = name;
    this.left = left;
    this.right = right;
  }

  // returns the result of applying the given visitor to this binary formula
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitBinaryFormula(this);
  }
}

// a function object defined over arithmetic expressions that returns a type of R
interface IArithVisitor<R> extends Function<IArith, R> {

  // applies this visitor to the given constant
  R visitConst(Const constant);

  // applies this visitor to the given unary formula
  R visitUnaryFormula(UnaryFormula unaryFormula);

  // applies this visitor to the given binary formula
  R visitBinaryFormula(BinaryFormula binaryFormula);
}

// a function object over arithmetic expressions that evaluates the tree to a Double answer
class EvalVisitor implements IArithVisitor<Double> {

  // applies this visitor to the given arithmetic expression
  public Double apply(IArith t) {
    return t.accept(this);
  }

  // applies this visitor to the given constant
  public Double visitConst(Const constant) {
    return constant.num;
  }

  // applies this visitor to the given unary formula
  public Double visitUnaryFormula(UnaryFormula unaryFormula) {
    double child = this.apply(unaryFormula.child);
    return unaryFormula.func.apply(child);
  }

  // applies this visitor to the given binary formula
  public Double visitBinaryFormula(BinaryFormula binaryFormula) {
    double left = this.apply(binaryFormula.left);
    double right = this.apply(binaryFormula.right);

    return binaryFormula.func.apply(left, right);
  }
}

// a function object over arithmetic expressions that produces a String
// showing the fully-parenthesized expression in Racket-like prefix notation
class PrintVisitor implements IArithVisitor<String> {

  // applies this visitor to the given arithmetic expression
  public String apply(IArith t) {
    return t.accept(this);
  }

  // applies this visitor to the given constant
  public String visitConst(Const constant) {
    return Double.toString(constant.num);
  }

  // applies this visitor to the given unary formula
  public String visitUnaryFormula(UnaryFormula unaryFormula) {
    String child = this.apply(unaryFormula.child);
    return "(" + unaryFormula.name + " " + child + ")";
  }

  // applies this visitor to the given binary formula
  public String visitBinaryFormula(BinaryFormula binaryFormula) {
    String left = this.apply(binaryFormula.left);
    String right = this.apply(binaryFormula.right);

    return "(" + binaryFormula.name + " " + left + " " + right + ")";
  }
}

// a function object over arithmetic expressions that produces another IArith,
// where every constant in the tree has been doubled
class DoublerVisitor implements IArithVisitor<IArith> {

  // applies this visitor to the given arithmetic expression
  public IArith apply(IArith t) {
    return t.accept(this);
  }

  // applies this visitor to the given constant
  public IArith visitConst(Const constant) {
    return new Const(2 * constant.num);
  }

  // applies this visitor to the given unary formula
  public IArith visitUnaryFormula(UnaryFormula unaryFormula) {
    IArith child = this.apply(unaryFormula.child);
    return new UnaryFormula(unaryFormula.func, unaryFormula.name, child);
  }

  // applies this visitor to the given binary formula
  public IArith visitBinaryFormula(BinaryFormula binaryFormula) {
    IArith left = this.apply(binaryFormula.left);
    IArith right = this.apply(binaryFormula.right);

    return new BinaryFormula(binaryFormula.func, binaryFormula.name, left, right);
  }
}

// a function object over arithmetic expressions that produces a Boolean
// that is true if every constant in the tree is less than 10
class AllSmallVisitor implements IArithVisitor<Boolean> {

  // applies this visitor to the given arithmetic expression
  public Boolean apply(IArith t) {
    return t.accept(this);
  }

  // applies this visitor to the given constant
  public Boolean visitConst(Const constant) {
    return constant.num < 10;
  }

  // applies this visitor to the given unary formula
  public Boolean visitUnaryFormula(UnaryFormula unaryFormula) {
    Boolean child = this.apply(unaryFormula.child);
    return child;
  }

  // applies this visitor to the given binary formula
  public Boolean visitBinaryFormula(BinaryFormula binaryFormula) {
    Boolean left = this.apply(binaryFormula.left);
    Boolean right = this.apply(binaryFormula.right);

    return left && right;
  }
}

// a function object over arithmetic expressions that produces a Boolean
// that is true if anywhere there is a Formula named "div"
// and the right argument does not evaluate to roughly zero
class NoDivBy0 implements IArithVisitor<Boolean> {

  // applies this visitor to the given arithmetic expression
  public Boolean apply(IArith t) {
    return t.accept(this);
  }

  // applies this visitor to the given constant
  public Boolean visitConst(Const constant) {
    return true;
  }

  // applies this visitor to the given unary formula
  public Boolean visitUnaryFormula(UnaryFormula unaryFormula) {
    Boolean child = this.apply(unaryFormula.child);
    return child;
  }

  // applies this visitor to the given binary formula
  public Boolean visitBinaryFormula(BinaryFormula binaryFormula) {
    Boolean left = this.apply(binaryFormula.left);
    Boolean right = this.apply(binaryFormula.right);

    if (!binaryFormula.name.equals("div")) {
      return left && right;
    }

    Double evalRight = new EvalVisitor().apply(binaryFormula.right);
    Boolean notRoughly0Right = Math.abs(evalRight) >= 0.0001;

    return left && right && notRoughly0Right;
  }
}

// a function object over arithmetic expressions that produces a Boolean
// that is true, if a negative number is never encountered at any point 
class NoNegativeResults implements IArithVisitor<Boolean> {

  // applies this visitor to the given arithmetic expression
  public Boolean apply(IArith t) {
    return t.accept(this);
  }

  // applies this visitor to the given constant
  public Boolean visitConst(Const constant) {
    return true;
  }

  // applies this visitor to the given unary formula
  public Boolean visitUnaryFormula(UnaryFormula unaryFormula) {
    if (unaryFormula.name.equals("neg")) {
      return false;
    }
    Boolean child = this.apply(unaryFormula.child);
    return child;
  }

  // applies this visitor to the given binary formula
  public Boolean visitBinaryFormula(BinaryFormula binaryFormula) {
    if (!(new NoDivBy0().apply(binaryFormula))) {
      return true;
    }

    Double eval = new EvalVisitor().apply(binaryFormula);
    if (eval < 0) {
      return false;
    }

    Boolean left = this.apply(binaryFormula.left);
    Boolean right = this.apply(binaryFormula.right);

    return left && right;
  }
}

// -------------------------

class Addition implements BiFunction<Double, Double, Double> {
  public Double apply(Double first, Double second) {
    return first + second;
  }
}

class Subtraction implements BiFunction<Double, Double, Double> {
  public Double apply(Double first, Double second) {
    return first - second;
  }
}

class Multiplication implements BiFunction<Double, Double, Double> {
  public Double apply(Double first, Double second) {
    return first * second;
  }
}

class Division implements BiFunction<Double, Double, Double> {
  public Double apply(Double first, Double second) {
    return first / second;
  }
}

class Negation implements Function<Double, Double> {
  public Double apply(Double num) {
    return -num;
  }
}

class Squaring implements Function<Double, Double> {
  public Double apply(Double num) {
    return num * num;
  }
}

// ------------ EXAMPLES ----------------
class IArithExamples {
  Addition plus = new Addition();
  Subtraction sub = new Subtraction();
  Multiplication mul = new Multiplication();
  Division div = new Division();
  Negation neg = new Negation();
  Squaring sqr = new Squaring();

  EvalVisitor ev = new EvalVisitor();
  PrintVisitor pv = new PrintVisitor();
  DoublerVisitor dv = new DoublerVisitor();
  AllSmallVisitor asv = new AllSmallVisitor();
  NoDivBy0 ndb0 = new NoDivBy0();
  NoNegativeResults nnr = new NoNegativeResults();

  IArith c0 = new Const(0);
  IArith c5 = new Const(5);
  IArith c10 = new Const(10);
  IArith neg5 = new UnaryFormula(neg, "neg", c5);
  IArith square5 = new UnaryFormula(sqr, "sqr", c5);
  IArith add = new BinaryFormula(plus, "add", c5, c10);
  IArith sub10 = new BinaryFormula(sub, "sub", c10, c5);
  IArith mul50 = new BinaryFormula(mul, "mul", c5, c10);
  IArith div2 = new BinaryFormula(div, "div", c10, c5);
  IArith divByZero = new BinaryFormula(div, "div", c10, c0);

  boolean testEval(Tester t) {
    return t.checkExpect(c0.accept(ev), 0.0) && t.checkExpect(c5.accept(ev), 5.0)
        && t.checkExpect(neg5.accept(ev), -5.0) && t.checkExpect(square5.accept(ev), 25.0)
        && t.checkExpect(add.accept(ev), 15.0) && t.checkExpect(sub10.accept(ev), 5.0)
        && t.checkExpect(mul50.accept(ev), 50.0) && t.checkExpect(div2.accept(ev), 2.0);
  }

  boolean testPrint(Tester t) {
    return t.checkExpect(c5.accept(pv), "5.0") && t.checkExpect(neg5.accept(pv), "(neg 5.0)")
        && t.checkExpect(square5.accept(pv), "(sqr 5.0)")
        && t.checkExpect(add.accept(pv), "(add 5.0 10.0)")
        && t.checkExpect(sub10.accept(pv), "(sub 10.0 5.0)")
        && t.checkExpect(mul50.accept(pv), "(mul 5.0 10.0)")
        && t.checkExpect(div2.accept(pv), "(div 10.0 5.0)");
  }

  boolean testDoubler(Tester t) {
    return t.checkExpect(c5.accept(dv), new Const(10)) && t.checkExpect(add.accept(dv),
        new BinaryFormula(plus, "add", new Const(10), new Const(20)));
  }

  boolean testAllSmall(Tester t) {
    return t.checkExpect(c5.accept(asv), true) && t.checkExpect(c10.accept(asv), false)
        && t.checkExpect(add.accept(asv), false)
        && t.checkExpect(new BinaryFormula(plus, "add", c5, c5).accept(asv), true);
  }

  boolean testNoDivByZero(Tester t) {
    return t.checkExpect(div2.accept(ndb0), true) && t.checkExpect(divByZero.accept(ndb0), false)
        && t.checkExpect(add.accept(ndb0), true);
  }

  boolean testNoNegativeResults(Tester t) {
    return t.checkExpect(sub10.accept(nnr), true) && t.checkExpect(neg5.accept(nnr), false)
        && t.checkExpect(mul50.accept(nnr), true);
  }
}
