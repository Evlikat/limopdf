package io.evlikat.limopdf.util.font;

import io.evlikat.limopdf.util.measurement.TsuPromille;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@Getter
public class PdfFont {

    public static final PdfFont HELVETICA = new PdfFont(PDType1Font.HELVETICA);

    private final PDFont nativeFont;

    private PdfFont(PDFont nativeFont) {
        this.nativeFont = nativeFont;
    }

    @SneakyThrows
    public TsuPromille getHeight() {
        return TsuPromille.of(nativeFont.getBoundingBox().getHeight());
    }

    @SneakyThrows
    public TsuPromille getStringWidth(String string) {
        return TsuPromille.of(nativeFont.getStringWidth(string));
    }
}
