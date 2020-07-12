package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.util.Rule;
import io.evlikat.limopdf.util.color.Color;
import io.evlikat.limopdf.util.font.PdfFontProperties;
import lombok.*;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PdfCharacterProperties {

    @Builder.Default
    private PdfFontProperties fontProperties = new PdfFontProperties();
    @Builder.Default
    private float fontSize = 12f;
    @Builder.Default
    private boolean isOverline = false;
    @Builder.Default
    private boolean isUnderline = false;
    @Builder.Default
    private boolean isStrikethrough = false;
    @Builder.Default
    private Color color = Color.BLACK;
    @Builder.Default
    private float textRise = 0f;

    @SneakyThrows
    public float getHeightInPixels() {
        return fontProperties.getHeight().toPixels(fontSize);
    }

    @SneakyThrows
    public void setUpContentStream(PDPageContentStream contentStream) {
        contentStream.setNonStrokingColor(color.getNativeColor());
        contentStream.setStrokingColor(color.getNativeColor());
        contentStream.setFont(fontProperties.getNativeFont(), fontSize);
    }

    @Rule
    public float getTextDecorationLineWidth() {
        return fontSize / 16f;
    }

    @Rule
    public float getUnderlineOffset() {
        return -fontSize / 6f;
    }

    @Rule
    public float getStrikethroughOffset() {
        return fontSize / 4f;
    }

    @Rule
    public float getOverlineOffset() {
        return fontSize / 2f + fontSize / 6f;
    }
}
