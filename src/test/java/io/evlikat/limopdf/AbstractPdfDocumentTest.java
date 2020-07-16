package io.evlikat.limopdf;

import io.evlikat.limopdf.util.devtools.PrintMode;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.evlikat.limopdf.util.PdfComparator.pdfAreEqual;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AbstractPdfDocumentTest {

    @Rule
    public TestName testName = new TestName();

    private final boolean saveDocAfterTest = Boolean.parseBoolean(System.getProperty("saveDocAfterTest", "true"));

    protected PdfDocument doc;

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
}
