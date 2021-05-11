package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.Rnd;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class LinkedDataCollection<E> implements DataCollection<E> {

    protected static final Filter<?> ACCEPT_ALL = e -> true;
    @SuppressWarnings("unchecked")
    protected static <E> Filter<? super E> acceptAll() {
        return (Filter<? super E>) ACCEPT_ALL;
    }

    protected Supplier<? extends Collection<E>> collectionSupplier;
    protected Lock lock;
    protected boolean useLock;
    protected Collection<E> elements;
    protected Map<Filter<? super E>, LinkedView<E>> views = new HashMap<>();

    public LinkedDataCollection() {
        this(ArrayList::new);
    }

    public LinkedDataCollection(Supplier<? extends Collection<E>> collectionSupplier) {
        this.collectionSupplier = collectionSupplier;
        elements = collectionSupplier.get();
    }

    @Override
    public void lock() {
        if(useLock && lock != null) {
            lock.lock();
        }
    }

    @Override
    public void unlock() {
        if(useLock && lock != null) {
            lock.unlock();
        }
    }

    public void enableLock() {
        if(lock == null) {
            lock = new ReentrantLock();
        }
        useLock = true;
    }

    public void disableLock() {
        useLock = false;
    }

    public boolean hasLock() {
        return useLock && lock != null;
    }

    @Override
    public void clear() {
        elements.clear();
        for(LinkedView<E> view: views.values()) {
            view.clear0();
        }
    }
    protected void clear0(LinkedView<E> view) {
        elements.removeAll(view.COLL);
        for(LinkedView<E> v: views.values()) {
            v.clear0(view);
        }
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean contains(E element) {
        return elements.contains(element);
    }

    @Override
    public boolean add(E element) {
        if(elements.add(element)) {
            for(LinkedView<E> v: views.values()) {
                v.add0(element);
            }
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(E... elements) {
        return addAll(Arrays.asList(elements));
    }

    @Override
    public boolean addAll(Collection<? extends E> elements) {
        if(this.elements.addAll(elements)) {
            for(LinkedView<E> v: views.values()) {
                v.addAll0(elements);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(E element) {
        System.out.println("HAS: " + element + " ? " + elements.contains(element));
        if(elements.remove(element)) {
            for(LinkedView<E> v: views.values()) {
                v.remove0(element);
            }
            return true;
        }
        return false;
    }
    protected void remove0(View<E> view, E element) {
        elements.remove(element);
        for(LinkedView<E> v: views.values()) {
            if(v != view) {
                v.remove0(element);
            }
        }
    }

    @Override
    public E remove(int index) {
        E e = CollectionUtil.remove(elements, index);
        for(LinkedView<E> v: views.values()) {
            v.remove0(e);
        }
        return e;
    }

    @Override
    public E removeRandom(Rnd rnd) {
        E e = rnd.removeRandom(elements);
        for(LinkedView<E> v: views.values()) {
            v.remove0(e);
        }
        return e;
    }

    @Override
    public E get(int index) {
        return CollectionUtil.get(elements, index);
    }

    @Override
    public E getRandom(Rnd rnd) {
        return rnd.getRandom(elements);
    }

    @Override
    public boolean collect(Collection<? super E> target) {
        return target.addAll(elements);
    }

    @Override
    public List<E> toList() {
        return new ArrayList<>(elements);
    }

    @Override
    public E[] toArray(IntFunction<? extends E[]> arrCreator) {
        return elements.toArray(arrCreator.apply(size()));
    }

    @Override
    public LinkedView<E> asView() {
        return createView(null);
    }

    @Override
    public LinkedView<E> createView(Filter<? super E> filter) {
        Filter<? super E> f = filter == null
                ? acceptAll()
                : filter;

        LinkedView<E> view = views.get(f);
        if(view == null) {
            Collection<E> viewColl = collectionSupplier.get();
            for(E e: elements) {
                if(f.test(e)) {
                    viewColl.add(e);
                }
            }
            view = new LinkedView<>(this, f, viewColl);
            views.put(f, view);
        }
        return view;
    }

    @Override
    public Iterator<E> iterator() {
        return elements.iterator();
    }

    @Override
    public Stream<E> stream() {
        return elements.stream();
    }

    /**
     * @author Daniel Abitz
     */
    protected static class LinkedView<E> implements View<E> {

        protected final LinkedDataCollection<E> PARENT;
        protected final Filter<? super E> FILTER;
        protected final Collection<E> COLL;

        public LinkedView(LinkedDataCollection<E> parent, Filter<? super E> filter, Collection<E> coll) {
            PARENT = parent;
            FILTER = filter;
            COLL = coll;
        }

        @Override
        public LinkedDataCollection<E> getCollection() {
            return PARENT;
        }

        @Override
        public void lock() {
            getCollection().lock();
        }

        @Override
        public void unlock() {
            getCollection().unlock();
        }

        @Override
        public void clear() {
            getCollection().clear0(this);
            COLL.clear();
        }
        protected void clear0() {
            COLL.clear();
        }
        protected void clear0(LinkedView<E> view) {
            if(view == this) {
                COLL.clear();
            } else {
                COLL.removeAll(view.COLL);
            }
        }

        @Override
        public boolean isEmpty() {
            return COLL.isEmpty();
        }

        @Override
        public int size() {
            return COLL.size();
        }

        @Override
        public boolean contains(E element) {
            return COLL.contains(element);
        }

        @Override
        public boolean add(E element) {
            return FILTER.test(element) && getCollection().add(element);
        }
        protected void add0(E element) {
            if(FILTER.test(element)) {
                COLL.add(element);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean addAll(E... elements) {
            return getCollection().addAll(elements);
        }

        @Override
        public boolean addAll(Collection<? extends E> elements) {
            for(E e: elements) {
                if(FILTER.test(e)) {
                    return getCollection().addAll(elements);
                }
            }
            return false;
        }
        protected void addAll0(Collection<? extends E> elements) {
            for(E e: elements) {
                add0(e);
            }
        }

        @Override
        public boolean remove(E element) {
            return FILTER.test(element) && getCollection().remove(element);
        }
        protected void remove0(E element) {
            if(FILTER.test(element)) {
                COLL.remove(element);
            }
        }

        @Override
        public E remove(int index) {
            E e = CollectionUtil.remove(COLL, index);
            getCollection().remove0(this, e);
            return e;
        }

        @Override
        public E removeRandom(Rnd rnd) {
            E e = rnd.removeRandom(COLL);
            getCollection().remove0(this, e);
            return e;
        }

        @Override
        public E get(int index) {
            return CollectionUtil.get(COLL, index);
        }

        @Override
        public E getRandom(Rnd rnd) {
            return rnd.getRandom(COLL);
        }

        @Override
        public boolean collect(Collection<? super E> target) {
            return target.addAll(COLL);
        }

        @Override
        public List<E> toList() {
            return new ArrayList<>(COLL);
        }

        @Override
        public E[] toArray(IntFunction<? extends E[]> arrCreator) {
            return COLL.toArray(arrCreator.apply(size()));
        }

        @Override
        public LinkedView<E> asView() {
            return this;
        }

        @Override
        public LinkedView<E> createView(Filter<? super E> filter) {
            return getCollection().createView(
                    e -> FILTER.test(e) && filter.test(e)
            );
        }

        @Override
        public Iterator<E> iterator() {
            return COLL.iterator();
        }

        @Override
        public Stream<E> stream() {
            return COLL.stream();
        }
    }
}
