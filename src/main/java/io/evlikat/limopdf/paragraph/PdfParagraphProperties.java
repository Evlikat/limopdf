package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.util.Box;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PdfParagraphProperties {

    private Box margin = Box.of(0f);
    private HorizontalTextAlignment horizontalTextAlignment = HorizontalTextAlignment.LEFT;
    private final PdfCharacterProperties characterProperties = new PdfCharacterProperties();
}
