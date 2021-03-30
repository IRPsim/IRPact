package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.CollectionUtil;

import java.util.*;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class Grouping2<A, B, X> implements Grouping<X> {

    protected Map<A, Map<B, List<X>>> grouping = new LinkedHashMap<>();

    protected Function<? super X, ? extends A> groupASelector;
    protected Function<? super X, ? extends B> groupBSelector;

    public Grouping2(
            Function<? super X, ? extends A> groupASelector,
            Function<? super X, ? extends B> groupBSelector) {
        this.groupASelector = groupASelector;
        this.groupBSelector = groupBSelector;
    }

    @Override
    public void add(X element) {
        A a = groupASelector.apply(element);
        B b = groupBSelector.apply(element);

        Map<B, List<X>> bMap = grouping.computeIfAbsent(a, _a -> new LinkedHashMap<>());
        List<X> list = bMap.computeIfAbsent(b, _b -> new ArrayList<>());
        list.add(element);
    }

    public Map<A, Map<B, List<X>>> getGrouping() {
        return grouping;
    }

    public Map<B, List<X>> get(A a) {
        return grouping.get(a);
    }

    public void sortAKeys(Comparator<? super A> comparator) {
        CollectionUtil.sortMap(grouping, comparator);
    }

    public void sortBKeys(Comparator<? super B> comparator) {
        for(Map<B, List<X>> bMap: grouping.values()) {
            CollectionUtil.sortMap(bMap, comparator);
        }
    }

    public void sortElements(Comparator<? super X> comparator) {
        for(Map<B, List<X>> bMap: grouping.values()) {
            for(List<X> list: bMap.values()) {
                list.sort(comparator);
            }
        }
    }
}
