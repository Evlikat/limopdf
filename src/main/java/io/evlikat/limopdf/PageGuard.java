package io.evlikat.limopdf;

import io.evlikat.limopdf.draw.DrawableArea;
import io.evlikat.limopdf.page.PageSpecification;
import io.evlikat.limopdf.page.PdfPage;
import io.evlikat.limopdf.paragraph.PdfParagraph;
import io.evlikat.limopdf.util.Box;
import io.evlikat.limopdf.util.Position;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class PageGuard {

    private final PDDocument document;
    private PdfPage currentPage;
    private final CurrentPositionHolder currentPosition = new CurrentPositionHolder(null, 0f);
    private PageSpecification pageSpecification = new PageSpecification();

    public PageGuard(PDDocument document) {
        this.document = document;
    }

    public PageGuard(PDDocument document, PageSpecification pageSpecification) {
        this.document = document;
        this.pageSpecification = pageSpecification;
    }

    @SneakyThrows
    public void addParagraph(PdfParagraph paragraph) {
        Drawer drawer = paragraph.drawer();
        DrawResult drawResult;
        while (true) {
            PdfPage page = getCurrentPage();
            DrawableArea drawableArea = page.prepareDrawableArea(currentPosition);
            drawResult = drawer.draw(drawableArea);
            if (drawResult == DrawResult.COMPLETE) {
                break;
            }
            newPage();
        }
    }

    public void newPage() {
        startNewPage();
    }

    @SneakyThrows
    private PdfPage getCurrentPage() {
        if (currentPage == null) {
            startNewPage();
        }
        return currentPage;
    }

    @SneakyThrows
    private void startNewPage() {
        if (currentPage != null) {
            currentPage.close();
        }
        Box margin = pageSpecification.getMargin();
        currentPosition.setPosition(Position.of(margin.getLeft(), pageSpecification.getSize().getHeight() - margin.getTop()));
        currentPage = new PdfPage(document, new PDPage(pageSpecification.getSize()), margin);
        document.addPage(currentPage.getNativePage());
    }

    @SneakyThrows
    public void close() {
        if (currentPage != null) {
            currentPage.close();
        }
    }
}
