package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.Collection;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface DataCollection<E> extends Iterable<E> {

    void lock();

    void unlock();

    void clear();

    boolean isEmpty();

    int size();

    boolean contains(E element);

    boolean add(E element);

    @SuppressWarnings("unchecked")
    boolean addAll(E... elements);

    boolean addAll(Collection<? extends E> elements);

    boolean remove(E element);

    E removeFirst(Predicate<? super E> filter);

    E remove(int index);

    E removeRandom(Rnd rnd);

    E get(int index);

    E getRandom(Rnd rnd);

    boolean collect(Collection<? super E> target);

    List<E> toList();

    E[] toArray(IntFunction<? extends E[]> arrCreator);

    View<E> asView();

    View<E> createView(Filter<? super E> filter);

    Stream<E> stream();

    /**
     * @author Daniel Abitz
     */
    interface View<E> extends DataCollection<E> {

        DataCollection<E> getCollection();
    }

    /**
     * @author Daniel Abitz
     */
    interface Filter<E> extends Predicate<E> {
    }
}
