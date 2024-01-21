package com.mk.sll;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
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


    //smoke tests for delegated methods

}