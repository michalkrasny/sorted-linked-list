package com.mk.sll;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Class similar to {@link LinkedList}, but keeps elements ordered.
 *
 * @param <E>
 */
@SuppressWarnings("NullableProblems")
public class SortedLinkedList<E> implements List<E> {

    private final LinkedList<E> delegate;

    private final Comparator<E> comparator;

    public SortedLinkedList(Comparator<E> comparator) {
        this.comparator = comparator;
        delegate = new LinkedList<>();
    }

    public SortedLinkedList(Comparator<E> comparator, Collection<? extends E> collection) {
        this.comparator = comparator;
        delegate = new LinkedList<>(collection);
        delegate.sort(comparator);
    }

    /**
     * Puts a new element into correct place of already sorted list. Iterates through the list until it finds
     * correct position to insert the new element.
     *
     * @param newElement may be null if comparator allows nulls. Otherwise, throws the same exception as comparator called
     *                   by List#sort would.
     * @return true if this list changed as a result of the call.
     */
    public boolean put(E newElement) {

        // we don't want to depend on comparator exception later in the code, because that would allow first null to be
        // inserted
        validateNullability(newElement);

        ListIterator<E> listIterator = delegate.listIterator();
        moveListIteratorToPositionToAddNewElement(newElement, listIterator);
        listIterator.add(newElement);
        return true;
    }

    /**
     * Puts a content of the newElements collection into the list.
     *
     * @param newElements may be null. May contain null if comparator allows nulls. Otherwise, throws the same exception as comparator called
     *                    by List#sort would.
     * @return true if this list changed as a result of the call.
     */
    public boolean putAll(Collection<? extends E> newElements) {
        if (newElements == null) {
            return false;
        }

        //defensive copy, because we'll depend on sorting
        List<E> newElementsCopy = new ArrayList<>(newElements);
        //validates nullability
        newElementsCopy.sort(comparator);

        ListIterator<E> listIterator = delegate.listIterator();
        for (E newElement : newElementsCopy) {
            moveListIteratorToPositionToAddNewElement(newElement, listIterator);
            listIterator.add(newElement);
        }
        return !newElements.isEmpty();
    }


    /**
     * The main reason why null values wouldn't be allowed is that comparator couldn't cope with it. If it can, then we'll
     * allow them.
     *
     * @param newElement - nullable
     */
    private void validateNullability(E newElement) {
        if (newElement == null) {
            // I don't want to over-engineer this. If comparator can compare nulls, then we'll allow it, otherwise not.
            //noinspection EqualsWithItself,ResultOfMethodCallIgnored,ConstantValue
            comparator.compare(newElement, newElement);
        }
    }

    private boolean elementInListIsSameOrAfterNewElement(E elementInList, E newElement) {
        //compare("B","A") = 1
        return comparator.compare(elementInList, newElement) >= 0;
    }


    /**
     * Method that moves iterator to the position, where add can be called ti insert new element. Method has a side effect -
     * it changes listIterator.
     *
     * @param newElement   will be compared to existing elements in the list to find the correct position of the iterator.
     * @param listIterator may be changed by method
     */
    private void moveListIteratorToPositionToAddNewElement(E newElement, ListIterator<E> listIterator) {
        while (listIterator.hasNext()) {
            E elementInList = listIterator.next();
            if (elementInListIsSameOrAfterNewElement(elementInList, newElement)) {
                // The new element is already after the element in the list. We need get one step back by calling
                // listIterator.previous() to use listIterator.add(newElement)
                // we know that there is previous, because we called next() earlier
                listIterator.previous();
                break;
            }
        }
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        delegate.replaceAll(operator);
        delegate.sort(comparator);
    }


    //Unsupported methods - see README.md

    /**
     * Sorting by different comparator is not supported.
     */
    @Override
    public void sort(Comparator<? super E> c) {
        throw new UnsupportedOperationException("Sorting by different comparator is not supported.");
    }


    /**
     * Unsupported operation, because it would break List contract that says that added element must be added to the
     * end of the list. Position in {@link SortedLinkedList} is defined by comparator.
     */
    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("Use put instead.");
    }

    /**
     * Unsupported operation, because it would break List contract that says that added element must be added to the
     * end of the list. Position in {@link SortedLinkedList} is defined by comparator.
     */
    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("Use put instead.");
    }

    /**
     * Unsupported operation, because it would break List contract that says that added element must be added to the
     * end of the list. Position in {@link SortedLinkedList} is defined by comparator.
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Use putAll instead.");
    }


    /**
     * Unsupported operation, because it would break List contract that says that added element must be added to the
     * end of the list. Position in {@link SortedLinkedList} is defined by comparator.
     *
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Use putAll instead.");
    }

    /**
     * Unsupported operation. Set is supposed to change element on a particular position but change of the position
     * is not expected. Position in {@link SortedLinkedList} is defined by comparator.
     *
     */
    @Override
    public E set(int index, E element) {
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
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return delegate.toArray(generator);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
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
    public void forEach(Consumer<? super E> action) {
        delegate.forEach(action);
    }

}
