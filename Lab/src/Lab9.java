import java.util.ArrayList;
import tester.Tester;

class ExampleExcise {

  void testExcise(Tester t) {
    ArrayList<Integer> list = new ArrayList<Integer>();
    list.add(1);
    list.add(5);
    list.add(4);
    list.add(2);
    list.add(3);
    this.excise(list, 5, 2);
    t.checkExpect(list.get(0), 1);
    t.checkExpect(list.get(1), 3);
  }

  <T> void excise(ArrayList<T> values, T val1, T val2) {
    boolean shouldRemove = false;
    for (int i = 0 ; i < values.size() ; i ++) {
      if (values.get(i) == val1) {
        shouldRemove = true;
      }
      if (values.get(i) == val2) {
        shouldRemove = false;
        values.remove(values.get(i));
      }
      if (shouldRemove) {
        values.remove(values.get(i));
        i--;
      }
    }
  }
}
