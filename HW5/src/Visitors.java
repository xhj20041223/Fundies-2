import tester.Tester;
import java.util.function.*;

// represents an arithmetic expression
interface IArith {
  // accepts a visitor
  <R> R accept(IArithVisitor<R> visitor);
}

// represents a constant number
class Const implements IArith {
  // the constant
  double num;

  // the constructor
  Const(double num) {
    this.num = num;
  }

  // accepts a visitor
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitConst(this);
  }
}

// represents a unary formula
class UnaryFormula implements IArith {
  // the function to be applied to this operand
  Function<Double, Double> func;
  // the name of this unary operation
  String name;
  // the operand
  IArith child;

  // the constructor
  UnaryFormula(Function<Double, Double> func, String name, IArith child) {
    // must check that name matches with operation?
    this.func = func;
    this.name = name;
    this.child = child;
  }

  // accepts a visitor
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitUnary(this);
  }
}

// represents a binary formula 
class BinaryFormula implements IArith {
  // the function to be applied to these operands
  BiFunction<Double, Double, Double> func;
  // the name of this operation
  String name;
  // the first operand
  IArith left;
  // the second operand
  IArith right;

  // the constructor
  BinaryFormula(BiFunction<Double, Double, Double> func, String name, IArith left, IArith right) {
    this.func = func;
    this.name = name;
    this.left = left;
    this.right = right;
  }

  // accepts a visitor
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitBinary(this);
  }
}

// represents the addition function
class Plus implements BiFunction<Double, Double, Double> {
  public Double apply(Double op1, Double op2) {
    return op1 + op2;
  }
}

// represents the subtraction function
class Minus implements BiFunction<Double, Double, Double> {
  public Double apply(Double op1, Double op2) {
    return op1 - op2;
  }
}

// represents the multiplication function
class Mul implements BiFunction<Double, Double, Double> {
  public Double apply(Double op1, Double op2) {
    return op1 * op2;
  }
}

// represents the division function
class Div implements BiFunction<Double, Double, Double> {
  public Double apply(Double op1, Double op2) {
    return op1 / op2;
  }
}

// represents negation of a number
class Neg implements Function<Double, Double> {
  public Double apply(Double op1) {
    return -1 * op1;
  }
}

// represents the square of a number
class Sqr implements Function<Double, Double> {
  public Double apply(Double op1) {
    return op1 * op1;
  }
}

// An IArithVisitor is a function over IArith's
interface IArithVisitor<R> extends Function<IArith, R> {
  // visits Const
  R visitConst(Const constant);

  // visits UnaryFormula
  R visitUnary(UnaryFormula unary);

  // visits BinaryFormula
  R visitBinary(BinaryFormula binary);
}

// a function over IArith's that evaluates the tree to a Double answer
class EvalVisitor implements IArithVisitor<Double> {
  // applies the function to the IArith
  public Double apply(IArith arith) {
    return arith.accept(this);
  }

  // returns the given constants number
  public Double visitConst(Const constant) {
    return constant.num;
  }

  // applies the function on the given UnaryFormula
  public Double visitUnary(UnaryFormula unary) {
    return unary.func.apply(this.apply(unary.child));
  }

  // applies the function on the given BinaryFormula
  public Double visitBinary(BinaryFormula binary) {
    return binary.func.apply(this.apply(binary.left), this.apply(binary.right));
  }
}

// a function over IArith's that produces a String showing the fully-parenthesized expression 
// in Racket-like prefix notation
class PrintVisitor implements IArithVisitor<String> {
  // applies the the function to the IArith
  public String apply(IArith arith) {
    return arith.accept(this);
  }

  // turns the constant into a String
  public String visitConst(Const constant) {
    return Double.toString(constant.num);
  }

  // turns the given UnaryFormula into String
  public String visitUnary(UnaryFormula unary) {
    return "(" + unary.name + " " + this.apply(unary.child) + ")";
  }

  // turns the given BinaryFormula into a String
  public String visitBinary(BinaryFormula binary) {
    return "(" + binary.name + " " + this.apply(binary.left) + " " + this.apply(binary.right) + ")";
  }
}

// a function over IArith's that produces new Arith where every Const in the tree has been doubled
class DoublerVisitor implements IArithVisitor<IArith> {
  // applies the the function to the IArith
  public IArith apply(IArith arith) {
    return arith.accept(this);
  }

  // multiplies the given constant by two
  public IArith visitConst(Const constant) {
    return new Const(constant.num * 2);
  }

  // applies the the function to the given UnaryFormula
  public IArith visitUnary(UnaryFormula unary) {
    return new UnaryFormula(unary.func, unary.name, this.apply(unary.child));
  }

  // applies the the function to the given BinaryFormula
  public IArith visitBinary(BinaryFormula binary) {
    return new BinaryFormula(binary.func, binary.name, this.apply(binary.left),
        this.apply(binary.right));
  }
}

// a function over IArith's that returns true if every constant in the tree is less than 10
class AllSmallVisitor implements IArithVisitor<Boolean> {
  // applies the the function to the IArith
  public Boolean apply(IArith arith) {
    return arith.accept(this);
  }

  // checks if the given constant is less than 10
  public Boolean visitConst(Const constant) {
    return constant.num < 10;
  }

  // applies the the function to the given UnaryFormula
  public Boolean visitUnary(UnaryFormula unary) {
    return this.apply(unary.child);
  }

  // applies the the function to the given BinaryFormula
  public Boolean visitBinary(BinaryFormula binary) {
    return this.apply(binary.left) && this.apply(binary.right);
  }
}

// a function over IArith's that returns true if anywhere there is a Formula named "div", 
// the right argument does not evaluate to roughly zero
class NoDivBy0 implements IArithVisitor<Boolean> {
  // applies the the function to the IArith
  public Boolean apply(IArith arith) {
    return arith.accept(this);
  }

  // returns true since a constant isn't being divided by 0
  public Boolean visitConst(Const constant) {
    return true;
  }

  // returns true since a UnaryFormula isn't being divided by 0
  public Boolean visitUnary(UnaryFormula unary) {
    return true;
  }

  // applies the the function to the given BinaryFormula
  public Boolean visitBinary(BinaryFormula binary) {
    // check if it's div
    if (binary.name.equals("div")) {
      // if right side is roughly 0, return false
      if (Math.abs(new EvalVisitor().apply(binary.right)) < 0.0001) {
        return false;
      }
    }
    return this.apply(binary.left) && this.apply(binary.right);
  }
}

// a function over IArith's that returns true if a negative number is never encountered 
// during its evaluation
class NoNegativeResults implements IArithVisitor<Boolean> {
  // applies the the function to the IArith
  public Boolean apply(IArith arith) {
    return arith.accept(this);
  }

  // checks if the given constant is a positive number
  public Boolean visitConst(Const constant) {
    return constant.num >= 0;
  }

  // applies the the function to the given UnaryFormula
  public Boolean visitUnary(UnaryFormula unary) {
    return new EvalVisitor().apply(unary.child) >= 0 && this.apply(unary.child);
  }

  // applies the the function to the given BinaryFormula
  public Boolean visitBinary(BinaryFormula binary) {
    if (new NoDivBy0().apply(binary)) {
      return new EvalVisitor().apply(binary.left) >= 0 && new EvalVisitor().apply(binary.right) >= 0
          && this.apply(binary.left) && this.apply(binary.right);
    }
    else {
      return true;
    }
  }
}

class ExampleVisitors {
  //
  BiFunction<Double, Double, Double> plus = new Plus();
  BiFunction<Double, Double, Double> minus = new Minus();
  BiFunction<Double, Double, Double> mul = new Mul();
  BiFunction<Double, Double, Double> div = new Div();
  Function<Double, Double> neg = new Neg();
  Function<Double, Double> sqr = new Sqr();

  IArith b1 = new BinaryFormula(minus, "minus",
      new BinaryFormula(div, "div",
          new BinaryFormula(minus, "minus", new Const(1.0), new Const(2.0)), new Const(0.0)),
      new Const(3.0));
  IArith b2 = new BinaryFormula(minus, "minus",
      new BinaryFormula(div, "div",
          new BinaryFormula(minus, "minus", new Const(1.0), new Const(2.0)), new Const(5.0)),
      new Const(3.0));
  IArith b3 = new BinaryFormula(div, "div",
      new BinaryFormula(plus, "plus", new Const(1.0), new Const(2.0)), new Const(-1.5));
  IArith b4 = new BinaryFormula(plus, "plus",
      new BinaryFormula(plus, "plus", new Const(3.0), new Const(4.0)),
      new UnaryFormula(neg, "neg", new Const(1.0)));

  boolean testNoDivBy0(Tester t) {
    return t.checkExpect(new NoDivBy0().apply(b1), false)
        && t.checkExpect(new NoDivBy0().apply(b2), true)
        && t.checkExpect(new NoDivBy0().apply(b3), true)
        && t.checkExpect(new Const(0.0).accept(new NoDivBy0()), true)
        && t.checkExpect(new UnaryFormula(sqr, "sqr", new Const(1.0)).accept(new NoDivBy0()), true)
        && t.checkExpect(b1.accept(new NoDivBy0()), false);
  }

  boolean testNoNegativeResults(Tester t) {
    return t.checkExpect(new NoNegativeResults().apply(b1), true)
        && t.checkExpect(new NoNegativeResults().apply(b2), false)
        && t.checkExpect(new NoNegativeResults().apply(b4), false)
        && t.checkExpect(new Const(-3.0).accept(new NoNegativeResults()), false) && t.checkExpect(
            new UnaryFormula(sqr, "sqr", new Const(3.0)).accept(new NoNegativeResults()), true);
  }

  boolean testEvalVisitor(Tester t) {
    return t.checkExpect(new EvalVisitor().apply(b4), 6.0)
        && t.checkExpect(new EvalVisitor().apply(b3), -2.0)
        && t.checkExpect(new Const(10.0).accept(new EvalVisitor()), 10.0)
        && t.checkExpect(new UnaryFormula(sqr, "sqr", new UnaryFormula(neg, "neg", new Const(5.0)))
            .accept(new EvalVisitor()), 25.0)
        && t.checkExpect(b2.accept(new EvalVisitor()), -3.2);
  }

  boolean testPrintVisitor(Tester t) {
    return t.checkExpect(new PrintVisitor().apply(b3), "(div (plus 1.0 2.0) -1.5)")
        && t.checkExpect(new PrintVisitor().apply(b1), "(minus (div (minus 1.0 2.0) 0.0) 3.0)")
        && t.checkExpect(b3.accept(new PrintVisitor()), "(div (plus 1.0 2.0) -1.5)")
        && t.checkExpect(b1.accept(new PrintVisitor()), "(minus (div (minus 1.0 2.0) 0.0) 3.0)")
        && t.checkExpect(new PrintVisitor().apply(b4), "(plus (plus 3.0 4.0) (neg 1.0))");
  }

  boolean testAllSmallVisitor(Tester t) {
    return t.checkExpect(new AllSmallVisitor().apply(b2), true)
        && t.checkExpect(new AllSmallVisitor().apply(new DoublerVisitor().apply(b2)), false)
        && t.checkExpect(new Const(10).accept(new AllSmallVisitor()), false)
        && t.checkExpect(new UnaryFormula(sqr, "sqr", new Const(3.0)).accept(new AllSmallVisitor()),
            true)
        && t.checkExpect(b3.accept(new AllSmallVisitor()), true);
  }

  boolean testDoublerVisitor(Tester t) {
    return t.checkExpect(new DoublerVisitor().apply(b2),
        new BinaryFormula(minus, "minus",
            new BinaryFormula(div, "div",
                new BinaryFormula(minus, "minus", new Const(2.0), new Const(4.0)), new Const(10.0)),
            new Const(6.0)))
        && t.checkExpect(new Const(10).accept(new DoublerVisitor()), new Const(20.0))
        && t.checkExpect(new UnaryFormula(sqr, "sqr", new Const(3.0)).accept(new DoublerVisitor()),
            new UnaryFormula(sqr, "sqr", new Const(6.0)))
        && t.checkExpect(b1.accept(new DoublerVisitor()),
            new BinaryFormula(minus, "minus", new BinaryFormula(div, "div",
                new BinaryFormula(minus, "minus", new Const(2.0), new Const(4.0)), new Const(0.0)),
                new Const(6.0)));
  }

  boolean testArithmetic(Tester t) {
    return t.checkExpect(new Plus().apply(2.0, 3.0), 5.0)
        && t.checkExpect(new Minus().apply(1.0, 2.0), -1.0)
        && t.checkExpect(new Mul().apply(10.0, 7.2), 72.0)
        && t.checkInexact(new Div().apply(2.0, 9.0), 0.2222, 0.001)
        && t.checkExpect(new Neg().apply(-8.0), 8.0) && t.checkExpect(new Sqr().apply(6.5), 42.25);
  }
}