package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.CollectionUtil;

import java.util.*;
import java.util.function.Function;

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
    public void add(X element) {
        A a = groupASelector.apply(element);
        B b = groupBSelector.apply(element);
        C c = groupCSelector.apply(element);

        Map<B, Map<C, List<X>>> bMap = grouping.computeIfAbsent(a, _a -> new LinkedHashMap<>());
        Map<C, List<X>> cMap = bMap.computeIfAbsent(b, _b -> new LinkedHashMap<>());
        List<X> list = cMap.computeIfAbsent(c, _c -> new ArrayList<>());
        list.add(element);
    }

    public Map<A, Map<B, Map<C, List<X>>>> getGrouping() {
        return grouping;
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
