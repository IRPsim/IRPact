package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.Rnd;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Daniel Abitz
 */
public class DataCollection<E> implements Iterable<E> {

    protected Lock lock;
    protected boolean useLock;
    protected Collection<E> elements;

    public DataCollection() {
        this(new ArrayList<>());
    }

    public DataCollection(Collection<E> elements) {
        this.elements = Objects.requireNonNull(elements);
    }

    public void lock() {
        if(useLock && lock != null) {
            lock.lock();
        }
    }

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

    public void changeType(Collection<E> newElements) {
        newElements.addAll(elements);
        elements = newElements;
    }

    public Collection<E> getElements() {
        return elements;
    }

    @SuppressWarnings("unchecked")
    public <R extends Collection<E>> R getElementsAs() {
        return (R) elements;
    }

    public boolean isRandomAccess() {
        return elements instanceof RandomAccess;
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public int size() {
        return elements.size();
    }

    public boolean contains(E element) {
        return elements.contains(element);
    }

    public boolean add(E element) {
        return elements.add(element);
    }

    @SuppressWarnings("unchecked")
    public boolean addAll(E... elements) {
        return Collections.addAll(this.elements, elements);
    }

    public boolean remove(E element) {
        return elements.remove(element);
    }

    public E remove(int index) {
        if(elements instanceof List) {
            return ((List<E>) elements).remove(index);
        } else {
            return CollectionUtil.remove(elements, index);
        }
    }

    public E removeRandom(Rnd rnd) {
        int index = rnd.nextInt(size());
        return remove(index);
    }

    public E get(int index) {
        if(elements instanceof List) {
            return ((List<E>) elements).get(index);
        } else {
            return CollectionUtil.get(elements, index);
        }
    }

    public E getRandom(Rnd rnd) {
        int index = rnd.nextInt(size());
        return get(index);
    }

    public boolean removeAll(Collection<? extends E> coll) {
        return elements.removeAll(coll);
    }

    public void clear() {
        elements.clear();
    }

    public boolean collect(Collection<? super E> target) {
        return target.addAll(elements);
    }

    public <R extends Collection<? super E>> R collectTo(R target) {
        collect(target);
        return target;
    }

    public List<E> toList() {
        return collectTo(new ArrayList<>());
    }

    public E[] toArray(IntFunction<? extends E[]> func) {
        E[] arr = func.apply(size());
        return elements.toArray(arr);
    }

    public View<E> createView(Predicate<? super E> filter) {
        return new View<E>(this, filter);
    }

    @Override
    public Iterator<E> iterator() {
        return iterator(null);
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(), size(), 0);
    }

    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    protected Iterator<E> iterator(Predicate<? super E> filter) {
        return new Iterator<E>() {

            private final Iterator<E> ITER = elements.iterator();
            private final Predicate<? super E> FILTER = filter;
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

    /**
     * @author Daniel Abitz
     */
    public static class View<E> implements Iterable<E> {

        protected final DataCollection<E> COLL;
        protected final Predicate<? super E> FILTER;

        protected View(DataCollection<E> coll, Predicate<? super E> filter) {
            this.COLL = coll;
            this.FILTER = filter;
        }

        public void lock() {
            COLL.lock();
        }

        public void unlock() {
            COLL.unlock();
        }

        public void enableLock() {
            COLL.enableLock();
        }

        public void disableLock() {
            COLL.disableLock();
        }

        public boolean hasLock() {
            return COLL.hasLock();
        }

        public DataCollection<E> getCollection() {
            return COLL;
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        @SuppressWarnings("unused")
        public int size() {
            int s = 0;
            for(E e: this) {
                s++;
            }
            return s;
        }

        public void clear() {
            List<E> list = toList();
            COLL.removeAll(list);
        }

        private boolean isValid(E element) {
            return FILTER == null || FILTER.test(element);
        }

        public boolean contains(E element) {
            return isValid(element) && COLL.contains(element);
        }

        public boolean add(E element) {
            return isValid(element) && COLL.add(element);
        }

        public boolean remove(E element) {
            return isValid(element) && COLL.remove(element);
        }

        public E remove(int index) {
            E e = get(index);
            remove(e);
            return e;
        }

        public E removeRandom(Rnd rnd) {
            int index = rnd.nextInt(size());
            return remove(index);
        }

        public E get(int index) {
            int i = 0;
            for(E e: this) {
                if(i == index) {
                    return e;
                }
                i++;
            }
            throw new IndexOutOfBoundsException("index: " + index);
        }

        public E getRandom(Rnd rnd) {
            int index = rnd.nextInt(size());
            return get(index);
        }

        public boolean removeAll(Collection<? extends E> coll) {
            boolean changed = false;
            for(E e: coll) {
                changed |= remove(e);
            }
            return changed;
        }

        public boolean collect(Collection<? super E> target) {
            boolean changed = false;
            for(E e: this) {
                changed |= target.add(e);
            }
            return changed;
        }

        public <R extends Collection<? super E>> R collectTo(R target) {
            collect(target);
            return target;
        }

        public List<E> toList() {
            return collectTo(new ArrayList<>());
        }

        public E[] toArray(IntFunction<? extends E[]> func) {
            E[] arr = func.apply(size());
            int i = 0;
            for(E e: this) {
                arr[i++] = e;
            }
            return arr;
        }

        @Override
        public Iterator<E> iterator() {
            return COLL.iterator(FILTER);
        }

        @Override
        public Spliterator<E> spliterator() {
            return Spliterators.spliterator(iterator(), size(), 0);
        }

        public Stream<E> stream() {
            return StreamSupport.stream(spliterator(), false);
        }
    }
}
