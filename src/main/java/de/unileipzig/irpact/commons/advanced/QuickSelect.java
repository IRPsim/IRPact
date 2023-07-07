package de.unileipzig.irpact.commons.advanced;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class QuickSelect {

  public static final QuickSelect INSTANCE = new QuickSelect();

  public static QuickSelect getInstance() {
    return INSTANCE;
  }

  public <T> T select(List<T> list,
                      int n,
                      Comparator<? super T> comparator,
                      Random random) {
    return select(list, 0, list.size() - 1, n, comparator, random);
  }

  public <T> T select(List<T> list,
                      int left,
                      int right,
                      int n,
                      Comparator<? super T> comparator,
                      Random random) {
    while (true) {
      if (left == right) {
        return list.get(left);
      }
      int pivot = pivotIndex(left, right, random);
      pivot = partition(list, left, right, pivot, comparator);
      if (n == pivot) {
        return list.get(n);
      } else if (n < pivot) {
        right = pivot - 1;
      } else {
        left = pivot + 1;
      }
    }
  }

  private <T> int partition(List<T> list,
                            int left,
                            int right,
                            int pivot,
                            Comparator<? super T> comparator) {
    T pivotValue = list.get(pivot);
    Collections.swap(list, pivot, right);
    int store = left;
    for (int i = left; i < right; i++) {
      if (comparator.compare(list.get(i), pivotValue) < 0) {
        Collections.swap(list, store, i);
        store++;
      }
    }
    Collections.swap(list, right, store);
    return store;
  }

  private int pivotIndex(int left, int right, Random random) {
    return left + random.nextInt(right - left + 1);
  }
}
