package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.util.Box;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PdfParagraphProperties {

    @Builder.Default
    private Box margin = Box.of(0f);
    @Builder.Default
    private float lineSpacing = 1f;
    @Builder.Default
    private HorizontalTextAlignment horizontalTextAlignment = HorizontalTextAlignment.LEFT;
    @Builder.Default
    private final PdfCharacterProperties characterProperties = new PdfCharacterProperties();

    public float getLineSpacingInPixels() {
        return (getLineSpacing() - 1f) * getCharacterProperties().getHeightInPixels();
    }
}
