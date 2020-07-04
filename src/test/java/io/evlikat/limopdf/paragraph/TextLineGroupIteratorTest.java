package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.draw.TextChunk;
import io.evlikat.limopdf.draw.TextLine;
import org.junit.After;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TextLineGroupIteratorTest {

    private TextLineGroupIterator iterator;

    @After
    public void tearDown() {
        expectNoMoreElements();
    }

    @Test
    public void shouldPeekSingleParagraphByLines() {
        PdfParagraphProperties properties = PdfParagraphProperties.builder()
            .maxOrphanLinesAllowed(1)
            .build();
        TextLine line1 = line("A");
        TextLine line2 = line("B");
        TextLine line3 = line("C");
        List<TextLine> lines = List.of(
            line1, line2, line3
        );
        iterator = new TextLineGroupIterator(
            List.of(new ParagraphTextLineGroup(properties, lines))
        );

        for (TextLine line : lines) {
            expectLineGroups(line);
        }
    }

    private void expectNoMoreElements() {
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void shouldPeekSingleParagraphByLinesWithOrphanControl() {
        PdfParagraphProperties properties = PdfParagraphProperties.builder()
            .maxOrphanLinesAllowed(2)
            .build();
        TextLine line1 = line("A");
        TextLine line2 = line("B");
        TextLine line3 = line("C");
        TextLine line4 = line("D");
        List<TextLine> lines = List.of(
            line1, line2, line3, line4
        );
        iterator = new TextLineGroupIterator(
            List.of(new ParagraphTextLineGroup(properties, lines))
        );

        expectLineGroups(line1, line2);
        expectLineGroups(line3, line4);
    }

    @Test
    public void shouldPeekSingleParagraphByLinesWithOrphanControlWhenEffectivelyKeepTogether() {
        PdfParagraphProperties properties = PdfParagraphProperties.builder()
            .maxOrphanLinesAllowed(2)
            .build();
        TextLine line1 = line("A");
        TextLine line2 = line("B");
        TextLine line3 = line("C");
        List<TextLine> lines = List.of(
            line1, line2, line3
        );
        iterator = new TextLineGroupIterator(
            List.of(new ParagraphTextLineGroup(properties, lines))
        );

        expectLineGroups(line1, line2, line3);
    }

    @Test
    public void shouldPeekSingleParagraphKeepTogether() {
        PdfParagraphProperties properties = PdfParagraphProperties.builder()
            .keepTogether(true)
            .build();
        TextLine line1 = line("A");
        TextLine line2 = line("B");
        TextLine line3 = line("C");
        TextLine line4 = line("D");
        List<TextLine> lines = List.of(
            line1, line2, line3, line4
        );
        iterator = new TextLineGroupIterator(
            List.of(new ParagraphTextLineGroup(properties, lines))
        );

        expectLineGroups(line1, line2, line3, line4);
    }

    @Test
    public void shouldTwoParagraphsKeepWithNext() {
        PdfParagraphProperties properties = PdfParagraphProperties.builder()
            .keepWithNext(true)
            .build();
        TextLine line1 = line("A");
        TextLine line2 = line("B");
        TextLine line3 = line("C");
        TextLine line4 = line("D");
        List<TextLine> lines1 = List.of(
            line1, line2
        );
        List<TextLine> lines2 = List.of(
            line3, line4
        );
        iterator = new TextLineGroupIterator(
            List.of(
                new ParagraphTextLineGroup(properties, lines1),
                new ParagraphTextLineGroup(properties, lines2)
            )
        );

        expectLineGroups(line1, line2, line3, line4);
    }

    @Test
    public void shouldTwoParagraphsKeepWithNextFirstLines() {
        PdfParagraphProperties properties1 = PdfParagraphProperties.builder()
            .keepWithNext(true)
            .build();
        PdfParagraphProperties properties2 = PdfParagraphProperties.builder()
            .maxOrphanLinesAllowed(2)
            .build();
        TextLine line1 = line("A");
        TextLine line2 = line("B");
        TextLine line3 = line("M");
        TextLine line4 = line("N");
        TextLine line5 = line("O");
        TextLine line6 = line("P");
        List<TextLine> lines1 = List.of(
            line1, line2
        );
        List<TextLine> lines2 = List.of(
            line3, line4, line5, line6
        );
        iterator = new TextLineGroupIterator(
            List.of(
                new ParagraphTextLineGroup(properties1, lines1),
                new ParagraphTextLineGroup(properties2, lines2)
            )
        );

        expectLineGroups(line1, line2, line3, line4);
        expectLineGroups(line5, line6);
    }

    private void expectLineGroups(TextLine... textLines) {
        assertThat(iterator.hasNext()).isTrue();
        TextLineGroup lineGroup = iterator.peek();
        assertThat(lineGroup.getTextLines()).containsExactly(textLines);
        iterator.confirm(lineGroup);
    }

    private TextLine line(String text) {
        return new TextLine(new TextChunk(text, new PdfCharacterProperties(), 10, 10));
    }
}