package student;

import java.util.Comparator;
import java.util.List;

/**
 * Heap Sort implementation
 */
public class HeapSort<T> extends SortingHeapSort<T> {

  @Override
  /* Given a list and a comparator, sort the list using heap sort.
   *
   * Modify this method to implement the heapsort algorithm.
   * @param list the list to sort
   * @param comp the comparator
   * @return the sorted list
   */
  public List<T> heapsort(List<T> list, Comparator<T> comp) {
    int size = list.size();
    while (! this.isSorted(list, comp)) {
      list = this.heap(list, comp);
      list = this.remove(list);
      size -= 1;
    }
    return list;
  }
  
  boolean isSorted(List<T> list, Comparator<T> comp) {
    for (int i = 0 ; i < list.size() - 1 ; i += 1) {
      if (comp.compare(list.get(i), list.get(i + 1)) > 0) {
        return false;
      }
    }
    return true;
  }
  
  
  List<T> heap (List<T> list, Comparator<T> comp){
    for (int i = (int) Math.floor(list.size() / 2); i >= 0 ; i -= 1) {
      if (comp.compare(list.get(i), list.get(2 * i + 1)) < 0) {
        T current = list.get(i);
        list.set(i, list.get(2 * i + 1));
        list.set(2 * i + 1, current);
      }
      if (comp.compare(list.get(i), list.get(2 * i + 2)) < 0) {
        T current = list.get(i);
        list.set(i, list.get(2 * i + 2));
        list.set(2 * i + 2, current);
      }
    }
    return list;
  }
  List<T> remove (List<T> list){
    T current = list.get(0);
    list.set(0, list.get(list.size() - 1));
    list.set(list.size() - 1, current);
    return list;
  }
}
