package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.CurrentPosition;
import io.evlikat.limopdf.paragraph.PdfCharacterProperties;
import io.evlikat.limopdf.util.devtools.PrintMode;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.io.IOException;

public class DrawableTextLineArea implements DrawableContentArea {

    private final PDPageContentStream contentStream;
    private final CurrentPosition position;

    private final DrawableTextLine drawableTextLine;

    @Getter
    private final float totalBlockHeight;

    public DrawableTextLineArea(PDPageContentStream contentStream, CurrentPosition position, DrawableTextLine drawableTextLine) {
        this.contentStream = contentStream;
        this.position = position;
        this.drawableTextLine = drawableTextLine;

        float topMargin = Math.max(position.getLastBottomMargin(), drawableTextLine.getTopMargin());
        this.totalBlockHeight = drawableTextLine.getHeight() + topMargin + drawableTextLine.getTopPadding();
    }

    @Override
    public float getAreaWidth() {
        return drawableTextLine.getWidth();
    }

    @SneakyThrows
    public void fill() {

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
