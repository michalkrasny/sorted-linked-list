# Sorted Linked List
This is a library providing `SortedLinkedList.java` (linked list that keeps values sorted). It is able to hold string or
int values, but not both. 

# Analysis
SortedLinkedList resembles the java.util.LinkedList and it would be logical to make it as similar to LinkedList as possible.
The main difference is that it keeps the values sorted, so it doesn't make sense to implement Deque<E>.

## Implemented interfaces

### java.util.List<E>
This interface should be implemented, because `SortedLinkedList.java` is a list by nature. However, there is a pitfall in
the contract of methods `add`, `addAll` and `set`. The `List` interface says, that `add` and `addAll` put the elements at the end of the
list, which doesn't make sense if the list is sorted. Method set doesn't make sense in a sorted list too.
These operations are optional, so we will throw an `UnsupportedOperationException` and implement our own methods put and putAll
that will enable us to insert new elements and won't break the contract.

### java.util.Deque<E>
This interface **should not** be implemented. SortedLinkedList.java is sorted, adding a value to the beginning or the end
of the list would be confusing, because the added value may end up somewhere else after it is sorted.  

### java.lang.Cloneable, java.io.Serializable
This interface should be implemented. It is beneficial and LinkedList implements it too, and we want to keep the similarity by design. 

## Design

To implement `SortedLinkedList` we can 
1. write it from scratch,
2. take advantage of `java.util.AbstractSequentialList<E>`, 
3. use `java.util.LinkedList<E>` to implement the required linked list features using Delegation pattern https://en.wikipedia.org/wiki/Delegation_pattern
(aggregation) and write just sorting ourselves. We just need to implement sorting, all other methods like size, iterator() and will work the same way.
To meet the List contract we won't support `add`, `addAll` and `set` and use `put` and `putAll` instead. See chapter Implemented interfaces/java.util.List<E>.

I chose option 3. - delegation to LinkedList, because it is the most cost-effective solution and I agree with the chapter in
Effective Java by Joshua Bloch (Item 47) that you should prefer standard libraries over a custom code.

### Third party libraries
If this was an internal library, I would consider using libraries like Lombok to implement the delegate. If this was
a library made for public usage, I wouldn't use third party libraries if possible. Because I don't know what the target environment
will look like, I won't be using any third party libraries.
