package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.CurrentPositionHolder;
import io.evlikat.limopdf.paragraph.PdfCharacterProperties;
import io.evlikat.limopdf.util.IBlockElement;
import io.evlikat.limopdf.util.devtools.PrintMode;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.io.IOException;

public class DrawableArea {

    private final PDPageContentStream contentStream;
    private final CurrentPositionHolder position;
    @Getter
    private final float availableWidth;
    @Getter
    private float availableHeight;

    public DrawableArea(PDPageContentStream contentStream, CurrentPositionHolder position, float availableWidth, float availableHeight) {
        this.contentStream = contentStream;
        this.position = position;
        this.availableWidth = availableWidth;
        this.availableHeight = availableHeight;
    }

    public boolean canDraw(IBlockElement blockElement) {
        float margin = Math.max(position.getLastBottomMargin(), blockElement.getTopMargin());
        return availableHeight - blockElement.getTopPadding() >= (margin + blockElement.getHeight());
    }

    @SneakyThrows
    public void drawTextLine(DrawableTextLine drawableTextLine) {

        float topMargin = Math.max(position.getLastBottomMargin(), drawableTextLine.getTopMargin());

        float totalBlockHeight = drawableTextLine.getHeight() + topMargin + drawableTextLine.getTopPadding();

        float tx = position.getX() + drawableTextLine.getLeftIndent();
        float ty = position.getY() - totalBlockHeight;

        drawDebugLines(drawableTextLine, tx, ty);

        for (DrawableTextChunk textChunk : drawableTextLine.getChunks()) {
            PdfCharacterProperties characterProperties = textChunk.getCharacterProperties();

            characterProperties.setUpContentStream(contentStream);

            float xEffective = tx;
            float yEffective = ty + characterProperties.getTextRise();

            contentStream.beginText();
            contentStream.newLineAtOffset(xEffective, yEffective);
            contentStream.showText(textChunk.getText());
            contentStream.endText();

            float chunkWidth = textChunk.getWidth();

            addTextDecoration(characterProperties, xEffective, yEffective, chunkWidth);

            tx += chunkWidth;
        }

        position.minusY(totalBlockHeight);
        position.setLastBottomMargin(drawableTextLine.getBottomMargin());
        availableHeight -= totalBlockHeight;


        position.setBlank(false);
    }

    public boolean isBlank() {
        return position.isBlank();
    }

    private void addTextDecoration(PdfCharacterProperties characterProperties,
                                   float xEffective,
                                   float yEffective,
                                   float chunkWidth) throws IOException {

        if (characterProperties.isUnderline()) {
            float underlineWidth = characterProperties.getTextDecorationLineWidth();
            float underlineOffset = characterProperties.getUnderlineOffset();

            contentStream.setLineWidth(underlineWidth);
            contentStream.moveTo(xEffective, yEffective + underlineOffset);
            contentStream.lineTo(xEffective + chunkWidth, yEffective + underlineOffset);
            contentStream.stroke();
        }

        if (characterProperties.isStrikethrough()) {
            float underlineWidth = characterProperties.getTextDecorationLineWidth();
            float underlineOffset = characterProperties.getStrikethroughOffset();

            contentStream.setLineWidth(underlineWidth);
            contentStream.moveTo(xEffective, yEffective + underlineOffset);
            contentStream.lineTo(xEffective + chunkWidth, yEffective + underlineOffset);
            contentStream.stroke();
        }

        if (characterProperties.isOverline()) {
            float underlineWidth = characterProperties.getTextDecorationLineWidth();
            float overlineOffset = characterProperties.getOverlineOffset();

            contentStream.setLineWidth(underlineWidth);
            contentStream.moveTo(xEffective, yEffective + overlineOffset);
            contentStream.lineTo(xEffective + chunkWidth, yEffective + overlineOffset);
            contentStream.stroke();
        }
    }

    private void drawDebugLines(DrawableTextLine line, float tx, float ty) throws IOException {
        if (!PrintMode.INSTANCE.isDebug()) {
            return;
        }
        contentStream.setStrokingColor(Color.LIGHT_GRAY);
        contentStream.setLineWidth(1f);
        contentStream.addRect(tx, ty, line.getWidth(), line.getHeight());
        contentStream.stroke();
    }
}
