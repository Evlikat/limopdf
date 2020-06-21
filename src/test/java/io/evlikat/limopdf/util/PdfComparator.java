package io.evlikat.limopdf.util;

import com.testautomationguru.utility.CompareMode;
import com.testautomationguru.utility.PDFUtil;
import io.evlikat.limopdf.PdfDocument;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PdfComparator {

    private static final PDFUtil PDF_COMPARATOR = new PDFUtil();

    static {
        PDF_COMPARATOR.setCompareMode(CompareMode.VISUAL_MODE);
//        PDF_COMPARATOR.enableLog();
    }

    @SneakyThrows
    public static void pdfAreEqual(String expected, PdfDocument actual) {
        File tempFile = File.createTempFile("sample", "pdf");
        actual.save(tempFile);
        String expectedResourceName = "/sample/" + expected;
        URL resource = PdfComparator.class.getResource(expectedResourceName);
        if (resource == null) {
            fail("No resource for test: " + expectedResourceName);
        }
        File expectedFile = new File(resource.toURI());
        assertTrue(PDF_COMPARATOR.compare(
            expectedFile.getAbsolutePath(),
            tempFile.getAbsolutePath()
        ));
    }
}
