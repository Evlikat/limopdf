package io.evlikat.limopdf.util;

import com.testautomationguru.utility.CompareMode;
import com.testautomationguru.utility.PDFUtil;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;

public class PdfComparator {

    private static final PDFUtil PDF_COMPARATOR = new PDFUtil();

    static {
        PDF_COMPARATOR.setCompareMode(CompareMode.VISUAL_MODE);
//        PDF_COMPARATOR.enableLog();
    }

    @SneakyThrows
    public static Boolean pdfAreEqual(String expected, String tempFilePath) {
        String expectedResourceName = "/sample/" + expected;
        URL resource = PdfComparator.class.getResource(expectedResourceName);
        if (resource == null) {
            return null;
        }
        File expectedFile = new File(resource.toURI());
        return PDF_COMPARATOR.compare(
            expectedFile.getAbsolutePath(),
            tempFilePath
        );
    }

}
