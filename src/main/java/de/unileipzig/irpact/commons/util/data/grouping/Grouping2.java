package de.unileipzig.irpact.commons.util.data.grouping;

import de.unileipzig.irpact.commons.util.CollectionUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public boolean isEmpty() {
        return grouping.isEmpty();
    }

    @Override
    public void add(X element) {
        A a = groupASelector.apply(element);
        add(a, element);
    }

    public void add(A a, X element) {
        B b = groupBSelector.apply(element);
        add(a, b, element);
    }

    public void add(A a, B b, X element) {
        Map<B, List<X>> bMap = grouping.computeIfAbsent(a, _a -> new LinkedHashMap<>());
        List<X> list = bMap.computeIfAbsent(b, _b -> new ArrayList<>());
        list.add(element);
    }

    public Map<A, Map<B, List<X>>> getGrouping() {
        return grouping;
    }

    public Collection<A> getFirstComponents() {
        return grouping.keySet();
    }

    public List<A> listFirstComponents() {
        return new ArrayList<>(getFirstComponents());
    }

    public Collection<B> getSecondComponents() {
        return grouping.values()
                .stream()
                .flatMap(m -> m.keySet().stream())
                .collect(Collectors.toSet());
    }

    public List<B> listSecondComponents() {
        return new ArrayList<>(getSecondComponents());
    }

    public Map<B, List<X>> get(A a) {
        return grouping.get(a);
    }

    public void sortAKeys(Comparator<? super A> comparator) {
        CollectionUtil.sortMapAfterKey(grouping, comparator);
    }

    public void sortBKeys(Comparator<? super B> comparator) {
        for(Map<B, List<X>> bMap: grouping.values()) {
            CollectionUtil.sortMapAfterKey(bMap, comparator);
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
