package io.evlikat.limopdf.util.font;

import io.evlikat.limopdf.util.measurement.TsuPromille;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.font.PDFont;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PdfFontProperties {

    @Builder.Default
    private PdfFont font = PdfFont.HELVETICA;
    @Builder.Default
    private boolean isBold = false;
    @Builder.Default
    private boolean isItalic = false;

    @SneakyThrows
    public TsuPromille getHeight() {
        return TsuPromille.of(getNativeFont().getBoundingBox().getHeight());
    }

    @SneakyThrows
    public TsuPromille getStringWidth(String string) {
        return TsuPromille.of(getNativeFont().getStringWidth(string));
    }

    @SneakyThrows
    public PDFont getNativeFont() {
        if (isBold) {
            if (isItalic) {
                return font.getBoldObliqueFont();
            } else {
                return font.getBoldFont();
            }
        } else {
            if (isItalic) {
                return font.getObliqueFont();
            } else {
                return font.getNormalFont();
            }
        }
    }
}
