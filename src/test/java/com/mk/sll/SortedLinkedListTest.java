package com.mk.sll;

import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

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
    public void setUp() throws Exception {
        tested = new SortedLinkedList(STRING_COMPARATOR_DEFAULT);
    }


    @Test
    public void putOneElement() {
        tested.put("A");
        assertThat(tested).containsExactly("A");
    }

    @Test
    public void putMoreElementsInCorrectOrder() {
        tested.put("A");
        tested.put("B");
        tested.put("C");
        assertThat(tested).containsExactly("A", "B", "C");
    }

    @Test
    public void putMoreElementsInReverseOrder() {
        tested.put("C");
        tested.put("B");
        tested.put("A");
        assertThat(tested).containsExactly("A", "B", "C");
    }

    @Test
    public void putMoreSameElements() {
        tested.put("C");
        tested.put("B");
        tested.put("A");
        assertThat(tested).containsExactly("A", "B", "C");
    }


    /**
     * You don't need comparator to insert first element. The algorithm may have permitted first element to be null
     * if we'd depend on comparator exception. This test tests wo don't.
     */
    @Test
    public void putNullComparatorDoesntSupportNulls() {
        assertThatThrownBy(() -> tested.put(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void putNullNotFirstComparatorDoesntSupportNulls() {
        tested.put("A");
        assertThatThrownBy(() -> tested.put(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void putNullComparatorSupportsNulls() {
        tested = new SortedLinkedList<>(STRING_COMPARATOR_SUPPORTING_NULL);

        tested.put(null);

        //assertj doesn't support containsExactly(null)
        assertThat(tested.get(0)).isNull();
    }

    @Test
    public void putNullNotFirstComparatorSupportsNulls() {
        tested = new SortedLinkedList<>(STRING_COMPARATOR_SUPPORTING_NULL);

        tested.put("A");
        tested.put(null);

        //assertj doesn't support containsExactly(null)
        assertThat(tested.get(0)).isNull();
        assertThat(tested.get(1)).isEqualTo("A");
    }
}
