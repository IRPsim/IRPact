package de.unileipzig.irpact.commons.util.data.grouping;

import de.unileipzig.irpact.commons.util.CollectionUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public class Grouping4<A, B, C, D, X> implements Grouping<X> {

    protected Map<A, Map<B, Map<C, Map<D, List<X>>>>> grouping = new LinkedHashMap<>();

    protected Function<? super X, ? extends A> groupASelector;
    protected Function<? super X, ? extends B> groupBSelector;
    protected Function<? super X, ? extends C> groupCSelector;
    protected Function<? super X, ? extends D> groupDSelector;

    public Grouping4(
            Function<? super X, ? extends A> groupASelector,
            Function<? super X, ? extends B> groupBSelector,
            Function<? super X, ? extends C> groupCSelector,
            Function<? super X, ? extends D> groupDSelector) {
        this.groupASelector = groupASelector;
        this.groupBSelector = groupBSelector;
        this.groupCSelector = groupCSelector;
        this.groupDSelector = groupDSelector;
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
        D d = groupDSelector.apply(element);
        add(a, b, c, d, element);
    }

    public void add(A a, B b, C c, D d, X element) {
        Map<B, Map<C, Map<D, List<X>>>> bMap = grouping.computeIfAbsent(a, _a -> new LinkedHashMap<>());
        Map<C, Map<D, List<X>>> cMap = bMap.computeIfAbsent(b, _b -> new LinkedHashMap<>());
        Map<D, List<X>> dMap = cMap.computeIfAbsent(c, _c -> new LinkedHashMap<>());
        List<X> list = dMap.computeIfAbsent(d, _d -> new ArrayList<>());
        list.add(element);
    }

    public Map<A, Map<B, Map<C, Map<D, List<X>>>>> getGrouping() {
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

    public Collection<D> getForthComponents() {
        return grouping.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .flatMap(m -> m.values().stream())
                .flatMap(m -> m.keySet().stream())
                .collect(Collectors.toSet());
    }

    public List<D> listForthComponents() {
        return new ArrayList<>(getForthComponents());
    }

    public Collection<X> getElements() {
        return grouping.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .flatMap(m -> m.values().stream())
                .flatMap(m -> m.values().stream())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public List<X> listElements() {
        return new ArrayList<>(getElements());
    }

    public Map<B, Map<C, Map<D, List<X>>>> get(A a) {
        return grouping.get(a);
    }

    public Map<C, Map<D, List<X>>> get(A a, B b) {
        Map<B, Map<C, Map<D, List<X>>>> bMap = grouping.get(a);
        if(bMap == null) return null;
        return bMap.get(b);
    }

    public void sortAKeys(Comparator<? super A> comparator) {
        CollectionUtil.sortMapAfterKey(grouping, comparator);
    }

    public void sortBKeys(Comparator<? super B> comparator) {
        for(Map<B, Map<C, Map<D, List<X>>>> bMap: grouping.values()) {
            CollectionUtil.sortMapAfterKey(bMap, comparator);
        }
    }

    public void sortCKeys(Comparator<? super C> comparator) {
        for(Map<B, Map<C, Map<D, List<X>>>> bMap: grouping.values()) {
            for(Map<C, Map<D, List<X>>> cMap: bMap.values()) {
                CollectionUtil.sortMapAfterKey(cMap, comparator);
            }
        }
    }

    public void sortDKeys(Comparator<? super D> comparator) {
        for(Map<B, Map<C, Map<D, List<X>>>> bMap: grouping.values()) {
            for(Map<C, Map<D, List<X>>> cMap: bMap.values()) {
                for(Map<D, List<X>> dMap: cMap.values()) {
                    CollectionUtil.sortMapAfterKey(dMap, comparator);
                }
            }
        }
    }

    public void sortElements(Comparator<? super X> comparator) {
        for(Map<B, Map<C, Map<D, List<X>>>> bMap: grouping.values()) {
            for(Map<C, Map<D, List<X>>> cMap: bMap.values()) {
                for(Map<D, List<X>> dMap: cMap.values()) {
                    for(List<X> list: dMap.values())
                    list.sort(comparator);
                }
            }
        }
    }
}
