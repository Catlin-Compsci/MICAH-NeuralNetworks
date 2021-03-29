package utils;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListUtils {
    public static <E> Iterator<E> reverserator(List<E> list) {
        return new Iterator<E>() {
            ListIterator<E> internalIterator = list.listIterator(list.size());

            @Override
            public boolean hasNext() {
                return internalIterator.hasPrevious();
            }

            @Override
            public E next() {
                return internalIterator.previous();
            }
        };
    }
    public static <E> Iterator<E> reverserator(List<E> list, int numFromEnd) {
        return new Iterator<E>() {
            ListIterator<E> internalIterator = list.listIterator(list.size()-numFromEnd);

            @Override
            public boolean hasNext() {
                return internalIterator.hasPrevious();
            }

            @Override
            public E next() {
                return internalIterator.previous();
            }
        };
    }
}
