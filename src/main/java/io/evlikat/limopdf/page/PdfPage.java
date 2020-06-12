package io.evlikat.limopdf.page;

import io.evlikat.limopdf.CurrentPositionHolder;
import io.evlikat.limopdf.draw.DrawableArea;
import io.evlikat.limopdf.util.Box;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

@Getter
public class PdfPage {

    private PDPageContentStream contentStream;

    private final PDDocument document;
    private final PDPage nativePage;
    private final Box margin;

    public PdfPage(PDDocument document, PDPage nativePage, Box margin) {
        this.document = document;
        this.nativePage = nativePage;
        this.margin = margin;
    }

    @SneakyThrows
    private PDPageContentStream getContentStream() {
        if (contentStream == null) {
            contentStream = new PDPageContentStream(document, nativePage);
        }
        return contentStream;
    }

    public DrawableArea prepareDrawableArea(CurrentPositionHolder positionHolder) {
        return new DrawableArea(
            getContentStream(),
            positionHolder,
            getDrawableWidth(),
            positionHolder.getY() - margin.getBottom()
        );
    }

    public float getDrawableWidth() {
        return nativePage.getMediaBox().getWidth() - margin.getLeft() - margin.getRight();
    }

    public float getDrawableHeight() {
        return nativePage.getMediaBox().getHeight() - margin.getTop() - margin.getBottom();
    }

    public void close() {
        IOUtils.closeQuietly(contentStream, () -> {
        });
    }
}
