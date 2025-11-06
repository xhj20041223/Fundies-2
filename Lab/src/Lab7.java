import java.util.ArrayList;
import tester.Tester;
import java.util.function.*;

class ArrayUtils{
  <T> ArrayList<T> filter(ArrayList<T> arr, Predicate<T> pred){
    ArrayList<T> result = new ArrayList<T>();
    for (int i = 0 ; i < arr.size(); i ++) {
      if (pred.test(arr.get(i))) {
        result.add(arr.get(i));
      }
    }
    return result;
  }
  

  <T> ArrayList<T> customFilter(ArrayList<T> arr, Predicate<T> pred, boolean keepPassing){
    ArrayList<T> result = new ArrayList<T>();
    for (int i = 0 ; i < arr.size(); i ++) {
      if (pred.test(arr.get(i)) == keepPassing) {
        result.add(arr.get(i));
      }
    }
    return result;
  }
}