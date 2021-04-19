package de.unileipzig.irpact.commons.util.data.grouping;

import de.unileipzig.irpact.commons.util.CollectionUtil;

import java.util.*;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class Grouping1<A, X> implements Grouping<X> {

    protected Map<A, List<X>> grouping = new LinkedHashMap<>();

    protected Function<? super X, ? extends A> groupASelector;

    public Grouping1(Function<? super X, ? extends A> groupASelector) {
        this.groupASelector = groupASelector;
    }

    @Override
    public boolean isEmpty() {
        return grouping.isEmpty();
    }

    @Override
    public void add(X element) {
        A key = groupASelector.apply(element);

        List<X> list = grouping.computeIfAbsent(key, _key -> new ArrayList<>());
        list.add(element);
    }

    public Map<A, List<X>> getGrouping() {
        return grouping;
    }

    public void sortAKeys(Comparator<? super A> comparator) {
        CollectionUtil.sortMap(grouping, comparator);
    }

    public void sortElements(Comparator<? super X> comparator) {
        for(List<X> list: grouping.values()) {
            list.sort(comparator);
        }
    }
}
