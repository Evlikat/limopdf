package io.evlikat.limopdf;

import io.evlikat.limopdf.paragraph.HorizontalTextAlignment;
import io.evlikat.limopdf.paragraph.PdfParagraph;
import io.evlikat.limopdf.paragraph.PdfParagraphProperties;
import io.evlikat.limopdf.util.Box;
import org.junit.Test;

import static io.evlikat.limopdf.paragraph.HorizontalTextAlignment.*;
import static io.evlikat.limopdf.paragraph.PdfParagraphProperties.builder;
import static io.evlikat.limopdf.util.TextGenerator.loremIpsum;

public class PdfDocumentTest {

    private static final String SOME_PDF = "some.pdf";

    @Test(timeout = 3000L)
    public void shouldAddSomeParagraphs() {
        PdfDocument doc = new PdfDocument();
        doc.addParagraph(new PdfParagraph(loremIpsum()));
        doc.addParagraph(new PdfParagraph(loremIpsum().replaceAll("\\s+", "")));
        doc.save(SOME_PDF);
    }

    @Test(timeout = 3000L)
    public void shouldAddAlignedParagraphs() {
        PdfDocument doc = new PdfDocument();
        doc.addParagraph(new PdfParagraph("Left: " + loremIpsum(), boxed(LEFT, Box.left(45f))));
        doc.addParagraph(new PdfParagraph(""));
        doc.addParagraph(new PdfParagraph("Center: " + loremIpsum(), boxed(CENTER, Box.rightLeft(45f))));
        doc.addParagraph(new PdfParagraph(""));
        doc.addParagraph(new PdfParagraph("Right: " + loremIpsum(), boxed(RIGHT, Box.right(45f))));
        doc.save(SOME_PDF);
    }

    @Test(timeout = 3000L)
    public void shouldAddMarginParagraphs() {
        PdfDocument doc = new PdfDocument();
        doc.addParagraph(new PdfParagraph("1: " + loremIpsum(), boxed(LEFT, Box.bottom(15f))));
        doc.addParagraph(new PdfParagraph("2: " + loremIpsum(), boxed(LEFT, Box.topBottom(45f))));
        doc.addParagraph(new PdfParagraph("3: " + loremIpsum(), boxed(LEFT, Box.top(5f))));
        doc.save(SOME_PDF);
    }

    @Test(timeout = 3000L)
    public void shouldAddParagraphsMultiplePages() {
        PdfDocument doc = new PdfDocument();
        for (int i = 1; i <= 50; i++) {
            doc.addParagraph(new PdfParagraph(i + ": " + loremIpsum(), boxed(LEFT, Box.topBottom(10f))));
        }
        doc.save(SOME_PDF);
    }

    private PdfParagraphProperties boxed(HorizontalTextAlignment horizontalTextAlignment, Box margin) {
        return builder()
            .horizontalTextAlignment(horizontalTextAlignment)
            .margin(margin)
            .build();
    }
}