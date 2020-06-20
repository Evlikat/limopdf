package io.evlikat.limopdf;

import io.evlikat.limopdf.paragraph.HorizontalTextAlignment;
import io.evlikat.limopdf.paragraph.PdfParagraph;
import io.evlikat.limopdf.paragraph.PdfParagraphProperties;
import io.evlikat.limopdf.util.Box;
import io.evlikat.limopdf.util.devtools.PrintMode;
import org.junit.Before;
import org.junit.Test;

import static io.evlikat.limopdf.paragraph.HorizontalTextAlignment.*;
import static io.evlikat.limopdf.paragraph.PdfParagraphProperties.builder;
import static io.evlikat.limopdf.util.TextGenerator.loremIpsum;

public class PdfDocumentTest {

    private static final String SOME_PDF = "some.pdf";

    @Before
    public void setUp() {
        PrintMode.INSTANCE.setDebug(false);
    }

    @Test(timeout = 3000L)
    public void shouldAddSomeParagraphs() {
        PdfDocument doc = new PdfDocument();
        doc.addParagraph(new PdfParagraph(loremIpsum()));
        doc.addParagraph(new PdfParagraph(loremIpsum().replaceAll("\\s+", "")));
        doc.save(SOME_PDF);
    }

    @Test(timeout = 3000L)
    public void shouldPrintParagraphsWithDifferentLineSpacing() {
        Box margin = Box.topBottom(25f);
        PdfDocument doc = new PdfDocument();
        for (int i = 0; i <= 10; i++) {
            float lineSpacing = 1f + i * 0.1f;
            doc.addParagraph(
                new PdfParagraph(
                    loremIpsum(i % 3 + 1),
                    builder().lineSpacing(lineSpacing).margin(margin).build()
                )
            );
        }
        doc.save(SOME_PDF);
    }

    @Test(timeout = 3000L)
    public void shouldAddAlignedParagraphs() {
        PdfDocument doc = new PdfDocument();
        doc.addParagraph(new PdfParagraph("Left: " + loremIpsum(), boxed(LEFT, Box.of(15f, 0f, 15f, 45f))));
        doc.addParagraph(new PdfParagraph("Center: " + loremIpsum(), boxed(CENTER, Box.of(15f, 45f))));
        doc.addParagraph(new PdfParagraph("Right: " + loremIpsum(), boxed(RIGHT, Box.of(15f, 45f, 15f, 0f))));
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