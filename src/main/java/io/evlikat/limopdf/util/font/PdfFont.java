package io.evlikat.limopdf.util.font;

import lombok.Getter;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@Getter
public class PdfFont {

    public static final PdfFont HELVETICA = new PdfFont(
        PDType1Font.HELVETICA,
        PDType1Font.HELVETICA_BOLD,
        PDType1Font.HELVETICA_BOLD_OBLIQUE,
        PDType1Font.HELVETICA_OBLIQUE
    );

    private final PDFont normalFont;
    private final PDFont boldFont;
    private final PDFont boldObliqueFont;
    private final PDFont obliqueFont;

    public PdfFont(PDFont normalFont, PDFont boldFont, PDFont boldObliqueFont, PDFont obliqueFont) {
        this.normalFont = normalFont;
        this.boldFont = boldFont;
        this.boldObliqueFont = boldObliqueFont;
        this.obliqueFont = obliqueFont;
    }
}
