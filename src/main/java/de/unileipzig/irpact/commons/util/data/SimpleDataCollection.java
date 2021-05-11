package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.Rnd;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Daniel Abitz
 */
public class SimpleDataCollection<E> implements DataCollection<E> {

    protected SimpleView<E> thisView;
    protected Lock lock;
    protected boolean useLock;
    protected Collection<E> elements;

    public SimpleDataCollection() {
        this(new ArrayList<>());
    }

    public SimpleDataCollection(Collection<E> elements) {
        this.elements = Objects.requireNonNull(elements, "elements");
    }

    public void changeType(Collection<E> newElements) {
        newElements.addAll(elements);
        elements = newElements;
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
    }
    public void clear(Filter<? super E> filter) {
        if(filter == null) {
            clear();
        } else {
            List<E> list = toList(filter);
            elements.removeAll(list);
        }
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }
    public boolean isEmpty(Filter<? super E> filter) {
        if(filter == null) {
            return isEmpty();
        } else {
            return size(filter) == 0;
        }
    }

    @Override
    public int size() {
        return elements.size();
    }
    public int size(Filter<? super E> filter) {
        if(filter == null) {
            return size();
        } else {
            int size = 0;
            for(E e: elements) {
                if(filter.test(e)) {
                    size++;
                }
            }
            return size;
        }
    }

    @Override
    public boolean contains(E element) {
        return elements.contains(element);
    }
    public boolean contains(Filter<? super E> filter, E element) {
        if(filter == null) {
            return contains(element);
        } else {
            return filter.test(element) && elements.contains(element);
        }
    }

    @Override
    public boolean add(E element) {
        return elements.add(element);
    }
    public boolean add(Filter<? super E> filter, E element) {
        if(filter == null) {
            return add(element);
        } else {
            return filter.test(element) && elements.add(element);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(E... elements) {
        return addAll(Arrays.asList(elements));
    }
    @SuppressWarnings("unchecked")
    public boolean addAll(Filter<? super E> filter, E... elements) {
        return addAll(filter, Arrays.asList(elements));
    }

    @Override
    public boolean addAll(Collection<? extends E> elements) {
        return this.elements.addAll(elements);
    }
    public boolean addAll(Filter<? super E> filter, Collection<? extends E> elements) {
        if(filter == null) {
            return addAll(elements);
        } else {
            boolean changed = false;
            for(E e: elements) {
                if(filter.test(e)) {
                    changed |= this.elements.add(e);
                }
            }
            return changed;
        }
    }

    @Override
    public boolean remove(E element) {
        return elements.remove(element);
    }
    public boolean remove(Filter<? super E> filter, E element) {
        if(filter == null) {
            return remove(element);
        } else {
            return filter.test(element) && elements.remove(element);
        }
    }

    @Override
    public E remove(int index) {
        return CollectionUtil.remove(elements, index);
    }
    public E remove(Filter<? super E> filter, int index) {
        if(filter == null) {
            return remove(index);
        } else {
            E element = get(filter, index);
            elements.remove(element);
            return element;
        }
    }

    @Override
    public E removeRandom(Rnd rnd) {
        int index = rnd.nextInt(size());
        return remove(index);
    }
    public E removeRandom(Filter<? super E> filter, Rnd rnd) {
        if(filter == null) {
            return removeRandom(rnd);
        } else {
            int index = rnd.nextInt(size(filter));
            return remove(filter, index);
        }
    }

    @Override
    public E get(int index) {
        return CollectionUtil.get(elements, index);
    }
    public E get(Filter<? super E> filter, int index) {
        if(filter == null) {
            return get(index);
        } else {
            int i = 0;
            for(E e: elements) {
                if(filter.test(e)) {
                    if(i == index) {
                        return e;
                    }
                    i++;
                }
            }
            throw new IndexOutOfBoundsException("index: " + index);
        }
    }

    @Override
    public E getRandom(Rnd rnd) {
        return CollectionUtil.getRandom(elements, rnd);
    }
    public E getRandom(Filter<? super E> filter, Rnd rnd) {
        if(rnd == null) {
            return getRandom(rnd);
        } else {
            int index = rnd.nextInt(size(filter));
            return get(filter, index);
        }
    }

    @Override
    public boolean collect(Collection<? super E> target) {
        return target.addAll(elements);
    }
    public boolean collect(Filter<? super E> filter, Collection<? super E> target) {
        if(filter == null) {
            return collect(target);
        } else {
            boolean changed = false;
            for(E e: elements) {
                if(filter.test(e)) {
                    changed |= target.add(e);
                }
            }
            return changed;
        }
    }

    @Override
    public List<E> toList() {
        return toList(null);
    }
    public List<E> toList(Filter<? super E> filter) {
        List<E> target = new ArrayList<>();
        collect(filter, target);
        return target;
    }

    @Override
    public E[] toArray(IntFunction<? extends E[]> arrCreator) {
        E[] arr = arrCreator.apply(size());
        return elements.toArray(arr);
    }
    public E[] toArray(Filter<? super E> filter, IntFunction<? extends E[]> arrCreator) {
        if(filter == null) {
            return toArray(arrCreator);
        } else {
            E[] arr = arrCreator.apply(size(filter));
            int i = 0;
            for(E e: elements) {
                if(filter.test(e)) {
                    arr[i++] = e;
                }
            }
            return arr;
        }
    }

    @Override
    public SimpleView<E> asView() {
        return createView(null);
    }

    @Override
    public SimpleView<E> createView(Filter<? super E> filter) {
        if(filter == null) {
            if(thisView == null) {
                thisView = new SimpleView<>(this, null);
            }
            return thisView;
        } else {
            return new SimpleView<>(this, filter);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return iterator(null);
    }
    public Iterator<E> iterator(Filter<? super E> filter) {
        return new Iterator<E>() {

            private final Iterator<E> ITER = elements.iterator();
            private final Filter<? super E> FILTER = filter;
            private boolean hasNext = false;
            private E next;

            @Override
            public boolean hasNext() {
                if(hasNext) {
                    return true;
                } else {
                    E temp;
                    while(ITER.hasNext()) {
                        temp = ITER.next();
                        if(FILTER == null || FILTER.test(temp)) {
                            hasNext = true;
                            next = temp;
                            break;
                        }
                    }
                    return hasNext;
                }
            }

            @Override
            public E next() {
                if(hasNext()) {
                    E temp = next;
                    next = null;
                    hasNext = false;
                    return temp;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(), size(), 0);
    }

    @Override
    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * @author Daniel Abitz
     */
    protected static class SimpleView<E> implements View<E> {

        protected final SimpleDataCollection<E> COLL;
        protected final Filter<? super E> FILTER;

        protected SimpleView(SimpleDataCollection<E> coll, Filter<? super E> filter) {
            COLL = coll;
            FILTER = filter;
        }

        @Override
        public SimpleDataCollection<E> getCollection() {
            return COLL;
        }

        protected Filter<? super E> getFilter() {
            return FILTER;
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
            getCollection().clear(getFilter());
        }

        @Override
        public boolean isEmpty() {
            return getCollection().isEmpty(getFilter());
        }

        @Override
        public int size() {
            return getCollection().size(getFilter());
        }

        @Override
        public boolean contains(E element) {
            return getCollection().contains(getFilter(), element);
        }

        @Override
        public boolean add(E element) {
            return getCollection().add(getFilter(), element);
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean addAll(E... elements) {
            return getCollection().addAll(getFilter(), elements);
        }

        @Override
        public boolean addAll(Collection<? extends E> elements) {
            return getCollection().addAll(getFilter(), elements);
        }

        @Override
        public boolean remove(E element) {
            return getCollection().remove(getFilter(), element);
        }

        @Override
        public E remove(int index) {
            return getCollection().remove(getFilter(), index);
        }

        @Override
        public E removeRandom(Rnd rnd) {
            return getCollection().removeRandom(getFilter(), rnd);
        }

        @Override
        public E get(int index) {
            return getCollection().get(getFilter(), index);
        }

        @Override
        public E getRandom(Rnd rnd) {
            return getCollection().getRandom(getFilter(), rnd);
        }

        @Override
        public boolean collect(Collection<? super E> target) {
            return getCollection().collect(getFilter(), target);
        }

        @Override
        public List<E> toList() {
            return getCollection().toList(getFilter());
        }

        @Override
        public E[] toArray(IntFunction<? extends E[]> arrCreator) {
            return getCollection().toArray(getFilter(), arrCreator);
        }

        @Override
        public SimpleView<E> asView() {
            return this;
        }

        @Override
        public SimpleView<E> createView(Filter<? super E> filter) {
            return new SimpleView<>(
                    COLL,
                    element -> FILTER.test(element) && filter.test(element)
            );
        }

        @Override
        public Iterator<E> iterator() {
            return getCollection().iterator(getFilter());
        }

        @Override
        public Spliterator<E> spliterator() {
            return Spliterators.spliterator(iterator(), size(), 0);
        }

        @Override
        public Stream<E> stream() {
            return StreamSupport.stream(spliterator(), false);
        }
    }
}
