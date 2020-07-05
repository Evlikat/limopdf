package io.evlikat.limopdf;

import io.evlikat.limopdf.page.PageSpecifications;
import io.evlikat.limopdf.paragraph.*;
import io.evlikat.limopdf.util.Box;
import io.evlikat.limopdf.util.color.Color;
import io.evlikat.limopdf.util.devtools.PrintMode;
import io.evlikat.limopdf.util.font.PdfFontProperties;
import lombok.SneakyThrows;
import one.util.streamex.EntryStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static io.evlikat.limopdf.paragraph.HorizontalTextAlignment.*;
import static io.evlikat.limopdf.paragraph.PdfParagraphProperties.builder;
import static io.evlikat.limopdf.paragraph.TextUtils.splitByWords;
import static io.evlikat.limopdf.util.PdfComparator.pdfAreEqual;
import static io.evlikat.limopdf.util.TextGenerator.loremIpsum;
import static io.evlikat.limopdf.util.color.Color.BLACK;
import static io.evlikat.limopdf.util.color.Color.RED;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PdfDocumentTest {

    @Rule
    public TestName testName = new TestName();

    private final boolean saveDocAfterTest = Boolean.parseBoolean(System.getProperty("saveDocAfterTest", "true"));

    private PdfDocument doc;

    @Before
    public void setUp() {
        doc = new PdfDocument();
        PrintMode.INSTANCE.setDebug(false);
    }

    @After
    @SneakyThrows
    public void tearDown() {
        String fileName = testName.getMethodName() + ".pdf";

        File tempFile = File.createTempFile("sample", "pdf");
        doc.save(tempFile);

        Boolean pdfAreEqual = pdfAreEqual(fileName, tempFile.getAbsolutePath());

        if (pdfAreEqual == null || saveDocAfterTest) {
            Files.copy(tempFile.toPath(), Path.of("test-out/", fileName), REPLACE_EXISTING);
        }

        if (pdfAreEqual == null) {
            fail("No resource for test: " + testName.getMethodName() + ". "
                + "Copy test-out/" + fileName + " to test resources (sample/)");
        }

        assertTrue(pdfAreEqual);

        doc.close();
    }

    @Test(timeout = 3000L)
    public void shouldAddSomeParagraphs() {
        doc.addParagraph(new PdfParagraph(loremIpsum()));
        doc.addParagraph(new PdfParagraph(loremIpsum().replaceAll("\\s+", "")));
    }

    @Test(timeout = 3000L)
    public void shouldAddStyledParagraphs() {
        List<PdfParagraphChunk> chunks1 = EntryStream.of(splitByWords(loremIpsum(2)))
            .mapKeyValue((index, word) -> new PdfParagraphChunk(word, byIndex(index, BLACK)))
            .collect(Collectors.toList());

        PdfParagraph paragraph1 = new PdfParagraph(chunks1);

        List<PdfParagraphChunk> chunks2 = EntryStream.of(splitByWords(loremIpsum(2)))
            .mapKeyValue((index, word) -> new PdfParagraphChunk(word, byIndex(index, RED)))
            .collect(Collectors.toList());
        PdfParagraph paragraph2 = new PdfParagraph(chunks2);

        doc.addParagraph(paragraph1);
        doc.addParagraph(paragraph2);
    }

    @Test(timeout = 3000L)
    public void shouldPrintParagraphsWithDifferentLineSpacing() {
        Box margin = Box.topBottom(25f);
        for (int i = 0; i <= 10; i++) {
            float lineSpacing = 1f + i * 0.1f;
            doc.addParagraph(
                new PdfParagraph(
                    loremIpsum(i % 3 + 1),
                    builder().lineSpacing(lineSpacing).margin(margin).build()
                )
            );
        }
    }

    @Test(timeout = 3000L)
    public void shouldAddAlignedParagraphs() {
        doc.addParagraph(new PdfParagraph("Left: " + loremIpsum(), boxed(LEFT, Box.of(15f, 0f, 15f, 45f))));
        doc.addParagraph(new PdfParagraph("Center: " + loremIpsum(), boxed(CENTER, Box.of(15f, 45f))));
        doc.addParagraph(new PdfParagraph("Right: " + loremIpsum(), boxed(RIGHT, Box.of(15f, 45f, 15f, 0f))));
    }

    @Test(timeout = 3000L)
    public void shouldAddMarginParagraphs() {
        doc.addParagraph(new PdfParagraph("1: " + loremIpsum(), boxed(LEFT, Box.bottom(15f))));
        doc.addParagraph(new PdfParagraph("2: " + loremIpsum(), boxed(LEFT, Box.topBottom(45f))));
        doc.addParagraph(new PdfParagraph("3: " + loremIpsum(), boxed(LEFT, Box.top(5f))));
    }

    @Test(timeout = 3000L)
    public void shouldAddParagraphsMultiplePages() {
        for (int i = 1; i <= 50; i++) {
            doc.addParagraph(new PdfParagraph(i + ": " + loremIpsum(i % 3 + 1), boxed(LEFT, Box.topBottom(10f))));
        }
    }

    @Test(timeout = 3000L)
    public void shouldAddKeepTogetherParagraphMovingNextPage() {
        doc.setPageSpecification(PageSpecifications.A5);
        doc.addParagraph(new PdfParagraph(loremIpsum(1), builder().keepTogether(true).build()));
        doc.addParagraph(new PdfParagraph(loremIpsum(2), builder().keepTogether(true).build()));
        doc.addParagraph(new PdfParagraph(loremIpsum(3), builder().keepTogether(true).build()));
    }

    @Test(timeout = 3000L)
    public void shouldAddKeepTogetherParagraphBiggerThanPageMovingNextPageStartingCleanPage() {
        doc.setPageSpecification(PageSpecifications.A5);
        doc.addParagraph(new PdfParagraph(loremIpsum(1, 3), builder().keepTogether(true).build()));
    }

    @Test(timeout = 3000L)
    public void shouldAddKeepTogetherParagraphBiggerThanPageMovingNextPage() {
        doc.setPageSpecification(PageSpecifications.A5);
        doc.addParagraph(new PdfParagraph("Title", boxed(CENTER, Box.of(0f))));
        doc.addParagraph(new PdfParagraph(loremIpsum(1, 3), builder().keepTogether(true).build()));
    }

    @Test(timeout = 3000L)
    public void shouldKeepTitleWithNextParagraphAtStartPage() {
        doc.setPageSpecification(PageSpecifications.A5);
        doc.addParagraph(new PdfParagraph("Title", title()));
        doc.addParagraph(new PdfParagraph(loremIpsum(1, 3), builder().keepTogether(true).build()));
    }

    @Test(timeout = 3000L)
    public void shouldKeepTitleWithNextParagraph() {
        doc.setPageSpecification(PageSpecifications.A5);
        doc.addParagraph(new PdfParagraph("Chapter I", title()));
        doc.addParagraph(new PdfParagraph(loremIpsum(1, 2), builder().keepTogether(true).build()));
        doc.addParagraph(new PdfParagraph("Chapter II", title()));
        doc.addParagraph(new PdfParagraph(loremIpsum(3), builder().keepTogether(true).build()));
    }

    @Test(timeout = 3000L)
    public void shouldHandlePageBreakWithDifferentPageSize() {
        doc.setPageSpecification(PageSpecifications.A4);
        doc.addParagraph(new PdfParagraph(loremIpsum(1),
            PdfParagraphProperties.builder().nextPageSpecification(PageSpecifications.A5).build()));
        doc.addParagraph(new PdfParagraph(loremIpsum(2)));
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

    private PdfCharacterProperties byIndex(int index, Color color) {
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
}