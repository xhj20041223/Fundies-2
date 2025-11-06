package student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import SortingAlgorithms.SortAlgorithm;

/**
 * Base class that implements the SortingAlgorithm interface.
 */
public abstract class SortingHeapSort<T> implements SortAlgorithm<T> {
  List<T> data;

  @Override
  public void init(List<T> list) {
    data = new ArrayList<>();
    data.addAll(list);
  }

  @Override
  public List<T> sort(Comparator<T> comp) {return heapsort(data, comp);
  }

  /**
   * Heapsort algorithm that sorts by building up a heap and then removing items from it
   * @param list the list to sort
   * @param comp the comparator to determine the sorted ordering
   * @return the sorted list
   */
  public abstract List<T> heapsort(List<T> list, Comparator<T> comp);
}
