package de.unileipzig.irpact.commons.util.data;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class SortedList<T> implements List<T> {

    protected final List<T> list = new ArrayList<>();
    protected Comparator<? super T> c;

    public SortedList() {
        this((Comparator<T>) null);
    }

    public SortedList(Comparator<? super T> c) {
        setComparator(c);
    }

    public SortedList(Collection<? extends T> elements) {
        this(null, elements);
    }

    public SortedList(Comparator<? super T> c, Collection<? extends T> elements) {
        setComparator(c);
        addAll(elements);
    }

    @SuppressWarnings("unchecked")
    protected static <R> R forceCast(Object input) {
        return (R) input;
    }

    public void setComparator(Comparator<? super T> c) {
        if(c == null) {
            c = forceCast(Comparator.naturalOrder());
        }
        if(this.c != c) {
            sortIntern(c);
        }
        this.c = c;
    }

    @Override
    public void sort(Comparator<? super T> c) {
        setComparator(c);
    }

    public Comparator<? super T> getComparator() {
        return c;
    }

    public void reverse() {
        setComparator(c.reversed());
    }

    protected void sortIntern(Comparator<? super T> c) {
        if(list.size() > 1) {
            list.sort(c);
        }
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    protected int search(T element) {
        return Collections.binarySearch(list, element, c);
    }

    @Override
    public boolean addAll(Collection<? extends T> elements) {
        for(T element: elements) {
            add(element);
        }
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T element) {
        int insertionIndex = search(element);
        if(insertionIndex < 0) {
            insertionIndex = -(insertionIndex + 1);
        } else {
            insertionIndex++;
        }
        list.add(insertionIndex, element);
        return true;
    }

    @Override
    public boolean contains(Object element) {
        try {
            T e = forceCast(element);
            return search(e) >= 0;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public T remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        try {
            T element = forceCast(o);
            int index = search(element);
            if(index < 0) {
                return -1;
            } else {
                while(index > 0 && c.compare(element, get(index - 1)) == 0) {
                    index--;
                }
                return index;
            }
        } catch (ClassCastException e) {
            return -1;
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        try {
            T element = forceCast(o);
            int index = search(element);
            if(index < 0) {
                return -1;
            } else {
                while(index < size() - 1 && c.compare(element, get(index + 1)) == 0) {
                    index++;
                }
                return index;
            }
        } catch (ClassCastException e) {
            return -1;
        }
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListIterator<T>() {

            private final ListIterator<T> iter = list.listIterator();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public T next() {
                return iter.next();
            }

            @Override
            public boolean hasPrevious() {
                return iter.hasPrevious();
            }

            @Override
            public T previous() {
                return iter.previous();
            }

            @Override
            public int nextIndex() {
                return iter.nextIndex();
            }

            @Override
            public int previousIndex() {
                return iter.previousIndex();
            }

            @Override
            public void remove() {
                iter.remove();
            }

            @Override
            public void set(T t) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void add(T t) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object element) {
        try {
            T e = forceCast(element);
            int index = search(e);
            if(index < 0) {
                return false;
            } else {
                remove(index);
                return true;
            }
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object element: c) {
            if(!contains(element)) {
                return false;
            }
        }
        return true;
    }

    public int removeAll(Object element) {
        int count = 0;
        while(remove(element)) {
            count++;
        }
        return count;
    }

    @Override
    public boolean removeAll(Collection<?> elements) {
        boolean changed = false;
        for(Object element: elements) {
            if(remove(element)) {
                changed = true;
            }
        }
        return changed;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean retainAll(Collection<?> c) {
        List<T> temp = new ArrayList<>();
        for(T element: list) {
            if(!c.contains(element)) {
                temp.add(element);
            }
        }
        return removeAll(temp);
    }

    public boolean removeAllWithDublicates(Collection<?> elements) {
        int count = 0;
        for(Object element: elements) {
            count += removeAll(element);
        }
        return count > 0;
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stream<T> stream() {
        return list.stream();
    }

    public boolean isSorted() {
        for(int i = 0; i < size() - 1; i++) {
            if(c.compare(get(i), get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private final Iterator<T> iter = list.iterator();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public T next() {
                return iter.next();
            }
        };
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @SuppressWarnings({"NullableProblems", "SuspiciousToArrayCall"})
    @Override
    public <T1> T1[] toArray(T1[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SortedList)) return false;
        SortedList<?> that = (SortedList<?>) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(list);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
