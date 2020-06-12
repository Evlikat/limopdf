package io.evlikat.limopdf;

import io.evlikat.limopdf.paragraph.PdfParagraph;
import org.junit.Test;

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
}