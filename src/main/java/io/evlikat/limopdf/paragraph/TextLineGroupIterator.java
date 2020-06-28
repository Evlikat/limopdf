package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.draw.TextLine;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class TextLineGroupIterator {

    private final List<ParagraphTextLineGroup> source;

    private int currentGroupIndex = 0;
    private int currentLineIndex = 0;

    private final Deque<TextLineGroup> preparedGroups = new ArrayDeque<>();

    public TextLineGroupIterator(List<ParagraphTextLineGroup> source) {
        this.source = source;
    }

    public boolean hasNext() {
        return !preparedGroups.isEmpty()
            || currentGroupIndex < source.size()
            && currentLineIndex < source.get(currentGroupIndex).getTextLines().size();
    }

    public TextLineGroup peek() {
        if (!preparedGroups.isEmpty()) {
            return preparedGroups.peek();
        }
        if (currentLineIndex >= source.get(currentGroupIndex).getTextLines().size()) {
            currentGroupIndex++;
            currentLineIndex = 0;
        }

        // TODO: replace recursion
        TextLineGroup preparedGroup = getNextTextLineGroup(currentGroupIndex, currentLineIndex)
            .orElseThrow(() -> new IllegalArgumentException("The iterator has no elements"));
        preparedGroups.add(preparedGroup);
        return preparedGroup;
    }

    public void confirm(TextLineGroup group) {
        currentLineIndex = group.getConfirmLineIndex();
        currentGroupIndex = group.getConfirmGroupIndex();

        if (group == preparedGroups.peek()) {
            preparedGroups.poll();
        } else {
            throw new IllegalStateException("Expected another group to confirm");
        }
    }

    public void ignoreKeepTogether() {
        if (preparedGroups.isEmpty()) {
            return;
        }
        TextLineGroup preparedTextLineGroup = preparedGroups.poll();
        preparedTextLineGroup.getTextLines().forEach(textLine -> preparedGroups.add(
            new TextLineGroup(
                textLine,
                preparedTextLineGroup.getConfirmGroupIndex(),
                preparedTextLineGroup.getConfirmLineIndex())
            )
        );
    }

    private Optional<TextLineGroup> getNextTextLineGroup(int groupIndex, int lineIndex) {
        if (groupIndex >= source.size()) {
            return empty();
        }

        ParagraphTextLineGroup currentGroup = source.get(groupIndex);
        PdfParagraphProperties paragraphProperties = currentGroup.getParagraphProperties();

        if (paragraphProperties.isKeepWithNext()) {
            return of(getNextTextLineGroup(groupIndex + 1, 0).map(tailTextLineGroup ->
                new TextLineGroup(
                    Stream.concat(
                        currentGroup.getTextLines().stream(),
                        tailTextLineGroup.getTextLines().stream()
                    ).collect(Collectors.toList()),
                    tailTextLineGroup.getConfirmGroupIndex(),
                    tailTextLineGroup.getConfirmLineIndex()
                )
            ).orElseGet(() -> new TextLineGroup(
                currentGroup.getTextLines(),
                groupIndex,
                currentGroup.size()
            )));

        } else if (paragraphProperties.isKeepTogether()) {
            return of(new TextLineGroup(currentGroup.getTextLines(), groupIndex, currentGroup.size()));

        } else {
            int maxOrphanLinesAllowed = paragraphProperties.getMaxOrphanLinesAllowed();
            if (maxOrphanLinesAllowed > 1) {

                if (currentGroup.size() < maxOrphanLinesAllowed * 2) {
                    return of(new TextLineGroup(currentGroup.getTextLines(), groupIndex, currentGroup.size()));

                } else if (isEdge(currentGroup, lineIndex, maxOrphanLinesAllowed)) {
                    List<TextLine> textLines = currentGroup.getTextLines().subList(lineIndex, Math.min(currentGroup.size(), lineIndex + maxOrphanLinesAllowed));
                    return of(new TextLineGroup(
                        textLines,
                        groupIndex,
                        lineIndex + textLines.size()
                    ));
                } else {
                    TextLine textLine = currentGroup.getTextLines().get(lineIndex);
                    return of(new TextLineGroup(
                        textLine,
                        groupIndex,
                        lineIndex + 1
                    ));
                }
            }
        }
        return of(new TextLineGroup(
            currentGroup.getTextLines().get(lineIndex),
            groupIndex,
            lineIndex + 1
        ));
    }

    private static boolean isEdge(ParagraphTextLineGroup group, int index, int maxOrphanLinesAllowed) {
        return index < maxOrphanLinesAllowed
            || index >= group.size() - maxOrphanLinesAllowed;
    }
}
