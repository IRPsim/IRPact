package de.unileipzig.irpact.commons.util.data.grouping;

import de.unileipzig.irpact.commons.util.CollectionUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public class Grouping3<A, B, C, X> implements Grouping<X> {

    protected Map<A, Map<B, Map<C, List<X>>>> grouping = new LinkedHashMap<>();

    protected Function<? super X, ? extends A> groupASelector;
    protected Function<? super X, ? extends B> groupBSelector;
    protected Function<? super X, ? extends C> groupCSelector;

    public Grouping3(
            Function<? super X, ? extends A> groupASelector,
            Function<? super X, ? extends B> groupBSelector,
            Function<? super X, ? extends C> groupCSelector) {
        this.groupASelector = groupASelector;
        this.groupBSelector = groupBSelector;
        this.groupCSelector = groupCSelector;
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
        C c = groupCSelector.apply(element);
        add(a, b, c, element);
    }

    public void add(A a, B b, C c, X element) {
        Map<B, Map<C, List<X>>> bMap = grouping.computeIfAbsent(a, _a -> new LinkedHashMap<>());
        Map<C, List<X>> cMap = bMap.computeIfAbsent(b, _b -> new LinkedHashMap<>());
        List<X> list = cMap.computeIfAbsent(c, _c -> new ArrayList<>());
        list.add(element);
    }

    public Map<A, Map<B, Map<C, List<X>>>> getGrouping() {
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

    public Collection<C> getThirdComponents() {
        return grouping.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .flatMap(m -> m.keySet().stream())
                .collect(Collectors.toSet());
    }

    public List<C> listThirdComponents() {
        return new ArrayList<>(getThirdComponents());
    }

    public Map<B, Map<C, List<X>>> get(A a) {
        return grouping.get(a);
    }

    public Map<C, List<X>> get(A a, B b) {
        Map<B, Map<C, List<X>>> bMap = grouping.get(a);
        if(bMap == null) return null;
        return bMap.get(b);
    }

    public void sortAKeys(Comparator<? super A> comparator) {
        CollectionUtil.sortMap(grouping, comparator);
    }

    public void sortBKeys(Comparator<? super B> comparator) {
        for(Map<B, Map<C, List<X>>> bMap: grouping.values()) {
            CollectionUtil.sortMap(bMap, comparator);
        }
    }

    public void sortCKeys(Comparator<? super C> comparator) {
        for(Map<B, Map<C, List<X>>> bMap: grouping.values()) {
            for(Map<C, List<X>> cMap: bMap.values()) {
                CollectionUtil.sortMap(cMap, comparator);
            }
        }
    }

    public void sortElements(Comparator<? super X> comparator) {
        for(Map<B, Map<C, List<X>>> bMap: grouping.values()) {
            for(Map<C, List<X>> cMap: bMap.values()) {
                for(List<X> list: cMap.values()) {
                    list.sort(comparator);
                }
            }
        }
    }
}
