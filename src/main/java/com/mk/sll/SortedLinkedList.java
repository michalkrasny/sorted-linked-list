package com.mk.sll;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Class similar to @{@link LinkedList}, but keeps
 *
 * @param <E>
 */
public class SortedLinkedList<E> implements List<E> {

    private final LinkedList<E> delegate = new LinkedList<>();

    private final Comparator<E> comparator;

    public SortedLinkedList(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Puts a new element into correct place of already sorted list. Iterates through the list until it finds
     * correct position to insert the new element.
     *
     * @param newElement may be null if comparator allows nulls. Otherwise, throws the same exception as comparator called
     *                   by List#sort would.
     */
    public void put(E newElement) {

        // we don't want to depend on comparator exception later in the code, because that would allow first null to be
        // inserted
        validateNullability(newElement);

        ListIterator<E> listIterator = delegate.listIterator();
        while (listIterator.hasNext()) {
            E elementInList = listIterator.next();
            if (newElementIsSameOrAfterElementInList(newElement, elementInList)) {
                // The new element is already after the element in the list. We need get one step back by calling
                // listIterator.previous() to use listIterator.add(newElement)
                // we know that there is previous, because we called next() earlier
                listIterator.previous();
                listIterator.add(newElement);
                return;
            }
        }
        listIterator.add(newElement);
    }


    /**
     * The main reason why null values wouldn't be allowed is that comparator couldn't cope with it. If it can, then we'll
     * allow them.
     *
     * @param newElement - nullable
     */
    private void validateNullability(final E newElement) {
        if (newElement == null) {
            // I don't want to over-engineer this. If comparator can compare nulls, then we'll allow it, otherwise not.
            //noinspection EqualsWithItself,ResultOfMethodCallIgnored
            comparator.compare(newElement, newElement);
        }
    }

    private boolean newElementIsSameOrAfterElementInList(E newElement, E elementInList) {
        //compare("B","A") = 1
        return comparator.compare(elementInList, newElement) >= 0;
    }


    public boolean putAll(Collection<? extends E> c) {
        throw new RuntimeException("Not implemented");
    }


    //Unsupported methods - see README.md

    /**
     * Sorting by different comparator is not supported yet.
     */
    @Override
    public void sort(final Comparator<? super E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("Use put instead");
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Use putAll instead");
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(final UnaryOperator<E> operator) {
        throw new UnsupportedOperationException();
    }


    //Delegated methods.

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return delegate.iterator();
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return delegate.toArray(a);
    }


    @Override
    public boolean remove(Object o) {
        return delegate.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return delegate.retainAll(c);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public E get(int index) {
        return delegate.get(index);
    }


    @Override
    public E remove(int index) {
        return delegate.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return delegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return delegate.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return delegate.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return delegate.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return delegate.subList(fromIndex, toIndex);
    }


    @Override
    public Spliterator<E> spliterator() {
        return delegate.spliterator();
    }

    @Override
    public <T> T[] toArray(final IntFunction<T[]> generator) {
        return delegate.toArray(generator);
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        return delegate.removeIf(filter);
    }

    @Override
    public Stream<E> stream() {
        return delegate.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return delegate.parallelStream();
    }

    @Override
    public void forEach(final Consumer<? super E> action) {
        delegate.forEach(action);
    }

}
