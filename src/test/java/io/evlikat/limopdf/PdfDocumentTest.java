package io.evlikat.limopdf;

import io.evlikat.limopdf.page.PageSpecifications;
import io.evlikat.limopdf.paragraph.*;
import io.evlikat.limopdf.util.Box;
import io.evlikat.limopdf.util.color.Color;
import io.evlikat.limopdf.util.font.PdfFontProperties;
import one.util.streamex.EntryStream;
import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.evlikat.limopdf.paragraph.HorizontalTextAlignment.*;
import static io.evlikat.limopdf.paragraph.PdfParagraphProperties.builder;
import static io.evlikat.limopdf.paragraph.TextUtils.splitByWords;
import static io.evlikat.limopdf.util.TextGenerator.loremIpsum;
import static io.evlikat.limopdf.util.color.Color.BLACK;
import static io.evlikat.limopdf.util.color.Color.RED;

public class PdfDocumentTest extends AbstractPdfDocumentTest {

    @Test(timeout = 3000L)
    public void shouldAddSomeParagraphs() {
        doc.add(new PdfParagraph(loremIpsum()));
        doc.add(new PdfParagraph(loremIpsum().replaceAll("\\s+", "")));
    }

    @Test(timeout = 3000L)
    public void shouldAddSomeParagraphsWithHyphens() {
        Pattern pattern = Pattern.compile("([A-z])\\s+([A-z])");
        String text = pattern.matcher(loremIpsum()).replaceAll("$1-$2");
        doc.add(new PdfParagraph(text));
    }

    @Test(timeout = 3000L)
    public void shouldAddStyledParagraphs() {
        List<PdfParagraphChunk> chunks1 = EntryStream.of(splitByWords(loremIpsum(2)))
            .mapKeyValue((index, word) -> new PdfParagraphChunk(word, differentPropertiesByIndex(index, BLACK)))
            .collect(Collectors.toList());

        PdfParagraph paragraph1 = new PdfParagraph(chunks1);

        List<PdfParagraphChunk> chunks2 = EntryStream.of(splitByWords(loremIpsum(2)))
            .mapKeyValue((index, word) -> new PdfParagraphChunk(word, differentPropertiesByIndex(index, RED)))
            .collect(Collectors.toList());
        PdfParagraph paragraph2 = new PdfParagraph(chunks2);

        doc.add(paragraph1);
        doc.add(paragraph2);
    }

    @Test(timeout = 3000L)
    public void shouldPrintParagraphsWithDifferentLineSpacing() {
        Box margin = Box.topBottom(25f);
        for (int i = 0; i <= 10; i++) {
            float lineSpacing = 1f + i * 0.1f;
            doc.add(
                new PdfParagraph(
                    loremIpsum(i % 3 + 1),
                    builder().lineSpacing(lineSpacing).margin(margin).build()
                )
            );
        }
    }

    @Test(timeout = 3000L)
    public void shouldAddAlignedParagraphs() {
        doc.add(new PdfParagraph("Left: " + loremIpsum(), boxed(LEFT, Box.of(15f, 0f, 15f, 45f))));
        doc.add(new PdfParagraph("Center: " + loremIpsum(), boxed(CENTER, Box.of(15f, 45f))));
        doc.add(new PdfParagraph("Right: " + loremIpsum(), boxed(RIGHT, Box.of(15f, 45f, 15f, 0f))));
    }

    @Test(timeout = 3000L)
    public void shouldAddMarginParagraphs() {
        doc.add(new PdfParagraph("1: " + loremIpsum(), boxed(LEFT, Box.bottom(15f))));
        doc.add(new PdfParagraph("2: " + loremIpsum(), boxed(LEFT, Box.topBottom(45f))));
        doc.add(new PdfParagraph("3: " + loremIpsum(), boxed(LEFT, Box.top(5f))));
    }

    @Test(timeout = 3000L)
    public void shouldAddParagraphsMultiplePages() {
        for (int i = 1; i <= 50; i++) {
            doc.add(new PdfParagraph(i + ": " + loremIpsum(i % 3 + 1), boxed(LEFT, Box.topBottom(10f))));
        }
    }

    @Test(timeout = 3000L)
    public void shouldAddParagraphsWithDifferentIndents() {
        doc.setPageSpecification(PageSpecifications.A5);
        doc.add(new PdfParagraph(loremIpsum(1),
            builder().margin(Box.topBottom(10f)).firstLineIndent(30f).build()));
        doc.add(new PdfParagraph(loremIpsum(2),
            builder().margin(Box.topBottom(10f)).firstLineIndent(60f).build()));
        doc.add(new PdfParagraph(loremIpsum(2),
            builder().margin(Box.topBottom(10f)).firstLineIndent(150f).build()));
        doc.add(new PdfParagraph(loremIpsum(3),
            builder().margin(Box.topBottom(10f)).firstLineIndent(-30f).build()));
    }

    @Test(timeout = 3000L)
    public void shouldAddKeepTogetherParagraphMovingNextPage() {
        doc.setPageSpecification(PageSpecifications.A5);
        doc.add(new PdfParagraph(loremIpsum(1), builder().keepTogether(true).build()));
        doc.add(new PdfParagraph(loremIpsum(2), builder().keepTogether(true).build()));
        doc.add(new PdfParagraph(loremIpsum(3), builder().keepTogether(true).build()));
    }

    @Test(timeout = 3000L)
    public void shouldAddKeepTogetherParagraphBiggerThanPageMovingNextPageStartingCleanPage() {
        doc.setPageSpecification(PageSpecifications.A5);
        doc.add(new PdfParagraph(loremIpsum(1, 3), builder().keepTogether(true).build()));
    }

    @Test(timeout = 3000L)
    public void shouldAddKeepTogetherParagraphBiggerThanPageMovingNextPage() {
        doc.setPageSpecification(PageSpecifications.A5);
        doc.add(new PdfParagraph("Title", boxed(CENTER, Box.of(0f))));
        doc.add(new PdfParagraph(loremIpsum(1, 3), builder().keepTogether(true).build()));
    }

    @Test(timeout = 3000L)
    public void shouldKeepTitleWithNextParagraphAtStartPage() {
        doc.setPageSpecification(PageSpecifications.A5);
        doc.add(new PdfParagraph("Title", title()));
        doc.add(new PdfParagraph(loremIpsum(1, 3), builder().keepTogether(true).build()));
    }

    @Test(timeout = 3000L)
    public void shouldKeepTitleWithNextParagraph() {
        doc.setPageSpecification(PageSpecifications.A5);
        doc.add(new PdfParagraph("Chapter I", title()));
        doc.add(new PdfParagraph(loremIpsum(1, 2), builder().keepTogether(true).build()));
        doc.add(new PdfParagraph("Chapter II", title()));
        doc.add(new PdfParagraph(loremIpsum(3), builder().keepTogether(true).build()));
    }

    @Test(timeout = 3000L)
    public void shouldHandlePageBreakWithDifferentPageSize() {
        doc.setPageSpecification(PageSpecifications.A4);
        doc.add(new PdfParagraph(loremIpsum(1),
            PdfParagraphProperties.builder().nextPageSpecification(PageSpecifications.A5).build()));
        doc.add(new PdfParagraph(loremIpsum(2)));
    }

    @Test(timeout = 3000L)
    public void shouldHandleTextRise() {
        List<PdfParagraphChunk> chunks = EntryStream.of(splitByWords(loremIpsum(2)))
            .mapKeyValue((index, word) -> new PdfParagraphChunk(word, differentTextRiseByIndex(index)))
            .collect(Collectors.toList());
        doc.setPageSpecification(PageSpecifications.A5);
        doc.add(new PdfParagraph(chunks));
    }

    private PdfParagraphProperties boxed(HorizontalTextAlignment horizontalTextAlignment, Box margin) {
        return builder()
            .horizontalTextAlignment(horizontalTextAlignment)
            .margin(margin)
            .build();
    }

    private PdfParagraphProperties title() {
        return builder()
            .horizontalTextAlignment(CENTER)
            .margin(Box.topBottom(15f))
            .keepWithNext(true)
            .build();
    }

    private PdfCharacterProperties differentPropertiesByIndex(int index, Color color) {
        return PdfCharacterProperties.builder()
            .fontProperties(PdfFontProperties.builder()
                .isBold((index & 0x01) == 0x01)
                .isItalic((index & 0x02) == 0x02)
                .build())
            .isUnderline((index & 0x04) == 0x04)
            .isStrikethrough((index & 0x08) == 0x08)
            .color(color)
            .build();
    }

    private PdfCharacterProperties differentTextRiseByIndex(int index) {
        // 0, 1, 2, -2, -1, 0, 1, 2, ...
        int shift = (index + 2) % 5 - 2;
        boolean isShifted = Math.abs(shift) == 1;
        return PdfCharacterProperties.builder()
            .textRise(isShifted ? (shift < 0 ? -2f : 6f) : 0f)
            .fontSize(isShifted ? 6f : 12f)
            .isUnderline(isShifted)
            .isStrikethrough(isShifted)
            .isOverline(isShifted)
            .build();
    }
}