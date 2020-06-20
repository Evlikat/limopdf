package io.evlikat.limopdf.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CollectionUtils {

    /**
     * Groups source list elements by the following scheme:
     * <ol>
     *     <li>first N elements as a list</li>
     *     <li>singleton lists for each element from the middle of source</li>
     *     <li>last N elements as a list</li>
     * </ol>
     * <p>
     * If source contains less than groupMinSize * 2 elements then all of them will be returned in a single list.
     */
    public static <T> List<List<T>> groupEdges(List<T> source, int groupMinSize) {
        if (source.isEmpty()) {
            return Collections.emptyList();
        }

        if (source.size() < groupMinSize * 2) {
            return Collections.singletonList(source);
        }

        ArrayList<List<T>> result = new ArrayList<>();

        result.add(
            source.subList(0, groupMinSize)
        );
        result.addAll(
            source.subList(groupMinSize, source.size() - groupMinSize).stream().map(Collections::singletonList).collect(toList())
        );
        result.add(
            source.subList(source.size() - groupMinSize, source.size())
        );

        return result;
    }

    private CollectionUtils() {
    }
}
