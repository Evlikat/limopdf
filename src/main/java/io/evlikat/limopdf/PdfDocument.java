package io.evlikat.limopdf;

import io.evlikat.limopdf.paragraph.PdfParagraph;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class PdfDocument {

    private final PDDocument document = new PDDocument();
    private final PageGuard guard = new PageGuard(document);

    public void addParagraph(PdfParagraph paragraph) {
        guard.addParagraph(paragraph);
    }

    public void save(String fileName) {
        beforeSave();
        try {
            document.save(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(OutputStream outputStream) {
        beforeSave();
        try {
            document.save(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(File file) {
        beforeSave();
        try {
            document.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void beforeSave() {
        guard.close();
    }
}
