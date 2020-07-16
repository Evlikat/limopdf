package io.evlikat.limopdf;

import io.evlikat.limopdf.page.PageSpecification;
import io.evlikat.limopdf.structure.Drawable;
import io.evlikat.limopdf.structure.DrawableGroups;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PdfDocument {

    private final PDDocument document;
    private final PageGuard guard;

    private final List<Drawable> elements = new ArrayList<>();

    public PdfDocument() {
        this.document = new PDDocument();
        this.guard = new PageGuard(document);
    }

    public PdfDocument(PageSpecification pageSpecification) {
        this.document = new PDDocument();
        this.guard = new PageGuard(document, pageSpecification);
    }

    public void setPageSpecification(PageSpecification pageSpecification) {
        this.guard.setPageSpecification(pageSpecification);
    }

    public void add(Drawable drawable) {
        elements.add(drawable);
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

    @SneakyThrows
    public void close() {
        document.close();
    }

    private void beforeSave() {
        DrawableGroups.buildDrawableGroup(elements).forEach(drawableGroup -> drawableGroup.putTo(guard));
        guard.close();
    }
}