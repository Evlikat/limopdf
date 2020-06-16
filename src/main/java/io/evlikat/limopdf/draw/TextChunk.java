package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.paragraph.PdfCharacterProperties;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

@Data
public class TextChunk {

    private final String text;
    private final PdfCharacterProperties characterProperties;
    private final float width;

    @SneakyThrows
    public void setUpContentStream(PDPageContentStream contentStream) {
        contentStream.setFont(characterProperties.getFont().getNativeFont(), characterProperties.getFontSize());
    }
}
