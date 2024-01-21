package com.mk.sll;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class SortedLinkedListTest {

    public static final Comparator<String> STRING_COMPARATOR_DEFAULT = Comparator.comparing((String x) -> x);

    public static final Comparator<String> STRING_COMPARATOR_SUPPORTING_NULL = (o1, o2) -> {

        //same null or non-null instance
        //noinspection StringEquality
        if (o1 == o2) {
            return 0;
        }

        if (o1 == null) {
            return -1;
        }

        if (o2 == null) {
            return 1;
        }

        return o1.compareTo(o2);
    };

    private SortedLinkedList<String> tested;

    @Before
    public void setUp() {
        tested = new SortedLinkedList<>(STRING_COMPARATOR_DEFAULT);
    }


    @Test
    public void put_oneElement() {
        boolean result = tested.put("A");
        assertThat(result).isTrue();
        assertThat(tested).containsExactly("A");
    }

    @Test
    public void put_moreElementsInCorrectOrder() {
        tested.put("A");
        tested.put("B");
        tested.put("C");
        assertThat(tested).containsExactly("A", "B", "C");
    }

    @Test
    public void put_moreElementsInReverseOrder() {
        tested.put("C");
        tested.put("B");
        tested.put("A");
        assertThat(tested).containsExactly("A", "B", "C");
    }

    @Test
    public void put_moreSameElements() {
        tested.put("C");
        tested.put("B");
        tested.put("A");
        assertThat(tested).containsExactly("A", "B", "C");
    }

    @Test
    public void put_insertAnElementBetweenTwoElements() {
        tested.put("A");
        tested.put("C");
        tested.put("B");
        assertThat(tested).containsExactly("A", "B", "C");
    }


    /**
     * You don't need comparator to insert first element. The algorithm may have permitted first element to be null
     * if we'd depend on comparator exception. This test tests wo don't.
     */
    @Test
    public void put_nullComparatorDoesntSupportNulls() {
        assertThatThrownBy(() -> tested.put(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void put_nullNotFirstComparatorDoesntSupportNulls() {
        tested.put("A");
        assertThatThrownBy(() -> tested.put(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void put_nullComparatorSupportsNulls() {
        tested = new SortedLinkedList<>(STRING_COMPARATOR_SUPPORTING_NULL);

        tested.put(null);

        //assertj doesn't support containsExactly(null)
        assertThat(tested.get(0)).isNull();
    }

    @Test
    public void put_nullNotFirstComparatorSupportsNulls() {
        tested = new SortedLinkedList<>(STRING_COMPARATOR_SUPPORTING_NULL);

        tested.put("A");
        tested.put(null);

        //assertj doesn't support containsExactly(null)
        assertThat(tested.get(0)).isNull();
        assertThat(tested.get(1)).isEqualTo("A");
    }


    @Test
    public void putAll_nullInputIntoEmptyList() {
        boolean result = tested.putAll(null);
        assertThat(result).isFalse();
        assertThat(tested).isEmpty();
    }

    @Test
    public void putAll_emptyInputIntoEmptyList() {
        boolean result = tested.putAll(new ArrayList<>());
        assertThat(result).isFalse();
        assertThat(tested).isEmpty();
    }

    @Test
    public void putAll_nullInputIntoNonEmptyList() {
        tested.put("A");
        boolean result = tested.putAll(null);
        assertThat(result).isFalse();
        assertThat(tested).containsExactly("A");
    }

    @Test
    public void putAll_emptyInputIntoNonEmptyList() {
        tested.put("A");
        boolean result = tested.putAll(new ArrayList<>());
        assertThat(result).isFalse();
        assertThat(tested).containsExactly("A");
    }


    @Test
    public void putAll_oneNewElementIntoEmptyList() {
        boolean result = tested.putAll(List.of("B"));
        assertThat(result).isTrue();
        assertThat(tested).containsExactly("B");
    }

    @Test
    public void putAll_argumentNotModified() {
        List<String> insertedList = List.of("B", "D", "C");

        tested.putAll(insertedList);

        assertThat(tested).containsExactly("B", "C", "D");
        // insertedList is unmodifiable, so it would fail before this line anyway, but this assertion makes the test
        // more readable
        assertThat(insertedList).containsExactly("B", "D", "C");
    }


    @Test
    public void putAll_oneNewElementIntoExistingList() {
        tested.put("A");
        tested.put("C");

        boolean result = tested.putAll(List.of("B"));
        assertThat(result).isTrue();

        assertThat(tested).containsExactly("A", "B", "C");
    }

    @Test
    public void putAll_oneNewElementToTheEndOfAnExistingList() {
        tested.put("A");
        tested.put("B");

        boolean result = tested.putAll(List.of("C"));
        assertThat(result).isTrue();

        assertThat(tested).containsExactly("A", "B", "C");
    }


    @Test
    public void putAll_elementsAreAlreadyPresent() {
        tested.put("A");
        tested.put("B");

        boolean result = tested.putAll(List.of("A", "B"));
        assertThat(result).isTrue();

        assertThat(tested).containsExactly("A", "A", "B", "B");
    }

    //tests for unsupported methods

    @Test
    public void sort_unsupported() {
        assertThatThrownBy(() -> tested.sort(STRING_COMPARATOR_DEFAULT)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void add_unsupported() {
        assertThatThrownBy(() -> tested.add("A")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void addAtIndex_unsupported() {
        assertThatThrownBy(() -> tested.add(0, "A")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void addAll_unsupported() {
        List<String> list = List.of("A");
        assertThatThrownBy(() -> tested.addAll(list)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void addAllAtIndex_unsupported() {
        List<String> list = List.of("A");
        assertThatThrownBy(() -> tested.addAll(0, list)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void set_unsupported() {
        assertThatThrownBy(() -> tested.set(0, "A")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void replaceAll_unsupported() {
        assertThatThrownBy(() -> tested.replaceAll(null)).isInstanceOf(UnsupportedOperationException.class);
    }


    //Smoke tests for delegated methods. We test that they are delegated, but otherwise we trust delegated implementation.  

    @Test
    public void size() {
        tested.put("A");
        int result = tested.size();
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void isEmpty() {
        tested.put("A");
        boolean result = tested.isEmpty();
        assertThat(result).isFalse();
    }

    @Test
    public void contains() {
        tested.put("A");
        boolean result = tested.contains("A");
        assertThat(result).isTrue();
    }

    @Test
    public void iterator() {
        tested.put("A");
        Iterator<String> result = tested.iterator();
        assertThat(result.next()).isEqualTo("A");
    }

    @Test
    public void toArray() {
        tested.put("A");
        Object[] result = tested.toArray();
        assertThat(result[0]).isEqualTo("A");
    }

    @Test
    public void toArray_generic() {
        tested.put("A");
        String[] result = tested.toArray(new String[0]);
        assertThat(result[0]).isEqualTo("A");
    }


    @Test
    public void remove_object() {
        tested.put("A");
        tested.put("B");
        tested.remove("A");
        assertThat(tested).containsExactly("B");
    }

    @Test
    public void containsAll() {
        tested.put("A");
        tested.put("B");
        boolean result = tested.containsAll(List.of("A", "B"));
        assertThat(result).isTrue();
    }


    @Test
    public void removeAll() {
        tested.put("A");
        tested.put("B");
        tested.removeAll(List.of("A", "B"));
        assertThat(tested).isEmpty();
    }

    @Test
    public void retainAll() {
        tested.put("A");
        tested.put("B");
        tested.retainAll(List.of("A"));
        assertThat(tested).containsExactly("A");
    }

    @Test
    public void clear() {
        tested.put("A");
        tested.put("B");
        tested.clear();
        assertThat(tested).isEmpty();
    }

    @Test
    public void get() {
        tested.put("A");
        tested.put("B");
        assertThat(tested.get(1)).isEqualTo("B");
    }


    @Test
    public void remove_atPosition() {
        tested.put("A");
        tested.put("B");
        tested.put("C");
        tested.remove(1);
        assertThat(tested).containsExactly("A", "C");
    }

    @Test
    public void indexOf() {
        tested.put("A");
        tested.put("B");

        assertThat(tested.indexOf("B")).isEqualTo(1);
    }

    @Test
    public void lastIndexOf() {
        tested.put("A");
        tested.put("B");
        tested.put("B");

        assertThat(tested.lastIndexOf("B")).isEqualTo(2);
    }

    @Test
    public void listIterator() {
        assertThat(tested.listIterator()).isNotNull();
    }

    @Test
    public void listIterator_index() {
        assertThat(tested.listIterator(0)).isNotNull();
    }

    @Test
    public void subList() {
        tested.put("A");
        tested.put("B");
        tested.put("C");
        List<String> result = tested.subList(1, 2);
        assertThat(result).containsExactly("B");
    }


    @Test
    public void spliterator() {
        assertThat(tested.spliterator()).isNotNull();
    }

    @Test
    public void toArray_generator() {
        tested.put("A");
        String[] result = tested.toArray(String[]::new);
        assertThat(result).containsExactly("A");
    }

    @Test
    public void removeIf() {
        tested.put("A");
        tested.put("B");
        tested.removeIf(element -> element.equals("A"));
        assertThat(tested).containsExactly("B");
    }

    @Test
    public void stream() {
        assertThat(tested.stream()).isNotNull();
    }


    @Test
    public void parallelStream() {
        assertThat(tested.parallelStream()).isNotNull();
    }


    @Test
    public void forEach() {
        tested.put("A");
        tested.put("B");
        StringBuilder sb = new StringBuilder();
        tested.forEach(sb::append);
        String result = sb.toString();
        assertThat(result).isEqualTo("AB");
    }

}