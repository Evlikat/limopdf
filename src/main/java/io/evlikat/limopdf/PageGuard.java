package io.evlikat.limopdf;

import io.evlikat.limopdf.draw.DrawableArea;
import io.evlikat.limopdf.page.PageSpecification;
import io.evlikat.limopdf.page.PageSpecifications;
import io.evlikat.limopdf.page.PdfPage;
import io.evlikat.limopdf.structure.Drawable;
import io.evlikat.limopdf.structure.StickyDrawable;
import io.evlikat.limopdf.util.Box;
import io.evlikat.limopdf.util.Position;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.util.Collection;

public class PageGuard implements DrawableSink {

    private final PDDocument document;
    private PdfPage currentPage;
    private final CurrentPositionHolder currentPosition = new CurrentPositionHolder();
    @Setter
    private PageSpecification pageSpecification = PageSpecifications.A4;

    public PageGuard(PDDocument document) {
        this.document = document;
    }

    public PageGuard(PDDocument document, PageSpecification pageSpecification) {
        this.document = document;
        this.pageSpecification = pageSpecification;
    }

    @SneakyThrows
    @Override
    public void add(Drawable drawable) {
        drawWith(drawable.drawer());
        drawable.pageBreak().ifPresent(this::newPage);
    }

    @SneakyThrows
    @Override
    public void add(StickyDrawable stickyDrawable, Collection<Drawable> otherDrawables) {
        StickyDrawer drawer = stickyDrawable.drawer();
        drawer.addFollowingDrawables(otherDrawables);
        drawWith(drawer);
        stickyDrawable.pageBreak().ifPresent(this::newPage);
    }

    private void drawWith(Drawer drawer) {
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

    public void newPage(PageSpecification nextPageSpecification) {
        this.pageSpecification = nextPageSpecification;
        startNewPage();
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

        currentPosition.reset(Position.of(margin.getLeft(), pageSpecification.getSize().getHeight() - margin.getTop()));

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
