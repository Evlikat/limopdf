package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.page.PageSpecification;
import io.evlikat.limopdf.paragraph.hyphenation.DefaultHyphenation;
import io.evlikat.limopdf.util.Box;
import io.evlikat.limopdf.util.Rule;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PdfParagraphProperties {

    @Builder.Default
    private Box margin = Box.of(0f);
    @Builder.Default
    private float firstLineIndent = 0f;
    @Builder.Default
    private float lineSpacing = 1f;
    @Builder.Default
    private HorizontalTextAlignment horizontalTextAlignment = HorizontalTextAlignment.LEFT;
    @Builder.Default
    private final PdfCharacterProperties characterProperties = new PdfCharacterProperties();
    @Builder.Default
    private boolean keepTogether = false;
    @Builder.Default
    private boolean keepWithNext = false;
    @Builder.Default
    private int maxOrphanLinesAllowed = 2;
    @Builder.Default
    private PageSpecification nextPageSpecification = null;
    @Builder.Default
    private HyphenationRules hyphenationRules = DefaultHyphenation.INSTANCE;

    @Rule
    public float getLineSpacingInPixels() {
        return (getLineSpacing() - 1f) * getCharacterProperties().getHeightInPixels();
    }
}
