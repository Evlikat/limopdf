package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.util.font.PdfFont;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
public class PdfCharacterProperties {

    private PdfFont font = PdfFont.HELVETICA;
    private float fontSize = 12f;

    @SneakyThrows
    public float getHeightInPixels() {
        return font.getHeight().toPixels(fontSize);
    }
}
