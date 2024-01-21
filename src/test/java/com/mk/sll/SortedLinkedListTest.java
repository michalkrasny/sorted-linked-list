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
        tested.put("A");
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
        tested.putAll(null);
        assertThat(tested).isEmpty();
    }

    @Test
    public void putAll_emptyInputIntoEmptyList() {
        tested.putAll(new ArrayList<>());
        assertThat(tested).isEmpty();
    }

    @Test
    public void putAll_nullInputIntoNonEmptyList() {
        tested.put("A");
        tested.putAll(null);
        assertThat(tested).containsExactly("A");
    }

    @Test
    public void putAll_emptyInputIntoNonEmptyList() {
        tested.put("A");
        tested.putAll(new ArrayList<>());
        assertThat(tested).containsExactly("A");
    }


    @Test
    public void putAll_oneNewElementIntoEmptyList() {
        tested.putAll(List.of("B"));
        assertThat(tested).containsExactly("B");
    }


    @Test
    public void putAll_oneNewElementIntoExistingList() {
        tested.put("A");
        tested.put("C");

        tested.putAll(List.of("B"));

        assertThat(tested).containsExactly("A", "B", "C");
    }

    @Test
    public void putAll_oneNewElementToTheEndOfAnExistingList() {
        tested.put("A");
        tested.put("B");

        tested.putAll(List.of("C"));

        assertThat(tested).containsExactly("A", "B", "C");
    }


    @Test
    public void putAll_elementsAreAlreadyPresent() {
        tested.put("A");
        tested.put("B");

        tested.putAll(List.of("A","B"));

        assertThat(tested).containsExactly("A","A","B", "B");
    }
}