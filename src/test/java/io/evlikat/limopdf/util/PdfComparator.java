package io.evlikat.limopdf.util;

import com.testautomationguru.utility.PDFUtil;
import io.evlikat.limopdf.PdfDocument;
import lombok.SneakyThrows;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class PdfComparator {

    private static final PDFUtil PDF_COMPARATOR = new PDFUtil();

    static {
//        PDF_COMPARATOR.enableLog();
    }

    @SneakyThrows
    public static void pdfAreEqual(String expected, PdfDocument actual) {
        File tempFile = File.createTempFile("sample", "pdf");
        actual.save(tempFile);
        File expectedFile = new File(PdfComparator.class.getResource("/sample/" + expected).toURI());
        assertTrue(PDF_COMPARATOR.compare(
            expectedFile.getAbsolutePath(),
            tempFile.getAbsolutePath()
        ));
    }
}
