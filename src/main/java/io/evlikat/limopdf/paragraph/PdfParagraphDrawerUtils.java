package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.draw.TextChunk;
import io.evlikat.limopdf.draw.TextLine;
import io.evlikat.limopdf.util.font.PdfFontProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toCollection;
import static org.apache.commons.lang3.StringUtils.*;

class PdfParagraphDrawerUtils {

    static List<TextLine> wrapLines(List<PdfParagraphChunk> paragraphChunks,
                                    float availableWidth,
                                    float firstLineIndent) {

        float remainingWidth = availableWidth - firstLineIndent;

        List<TextLine> lines = new ArrayList<>();
        TextLine currentLine = new TextLine();
        Deque<WrapCandidate<PdfParagraphChunk>> chunkQueue = paragraphChunks.stream()
            .map(WrapCandidate::forceWrapNotAllowed)
            .collect(toCollection(ArrayDeque::new));

        while (!chunkQueue.isEmpty()) {
            WrapCandidate<PdfParagraphChunk> item = chunkQueue.pop();
            if (item.obj.getText().isEmpty()) {
                continue;
            }
            boolean forceWrapAllowed = item.forceWrapAllowed;
            PdfParagraphChunk chunk = item.obj;

            StringWidthMeasurer measurer = new StringWidthMeasurer(
                chunk.getProperties().getFontProperties(),
                chunk.getProperties().getFontSize()
            );

            String chunkText = chunk.getText();
            // Trailing spaces can be ignored
            float requiredWidth = measurer.measureString(stripEnd(chunkText, null));
            if (requiredWidth <= remainingWidth) {
                // Trailing spaces must be considered
                float chunkWidth = measurer.measureString(chunkText);
                remainingWidth -= chunkWidth;
                currentLine.addChunk(new TextChunk(chunkText, chunk.getProperties(), chunkWidth, requiredWidth));
            } else {
                WrapResult wrapResult = wrapText(chunkText, remainingWidth, forceWrapAllowed, measurer::measureString);
                if (isNotBlank(wrapResult.currentLine)) {
                    currentLine.addChunk(
                        new TextChunk(
                            wrapResult.currentLine,
                            chunk.getProperties(),
                            wrapResult.width,
                            wrapResult.strippedWidth));
                }
                lines.add(currentLine);
                remainingWidth = availableWidth;

                PdfParagraphChunk newLineChunk = new PdfParagraphChunk(wrapResult.nextLine, chunk.getProperties());
                WrapCandidate<PdfParagraphChunk> e =
                    !currentLine.anyChunk() && isBlank(wrapResult.currentLine)
                        ? WrapCandidate.forceWrapAllowed(newLineChunk)
                        : WrapCandidate.forceWrapNotAllowed(newLineChunk);
                chunkQueue.addFirst(e);

                currentLine = new TextLine();
            }
        }
        if (currentLine.anyChunk()) {
            lines.add(currentLine);
        }

        return lines;
    }

    @SneakyThrows
    private static WrapResult wrapText(String text,
                                       float availableWidth,
                                       boolean forceWrapAllowed,
                                       Function<String, Float> measurer) {
        // TODO: add spaces
        List<String> textParts = TextUtils.splitByWords(text);
        StringBuilder currentLineBuilder = new StringBuilder();
        StringBuilder nextLineBuilder = new StringBuilder();
        float remainingWidth = availableWidth;
        boolean wrapped = false;
        float lastStripWidth = 0f;
        for (String textPart : textParts) {
            if (wrapped) {
                nextLineBuilder.append(textPart);
                continue;
            }
            float requiredWidth = measurer.apply(stripEnd(textPart, null));
            if (requiredWidth <= remainingWidth) {
                float actualWidth = measurer.apply(textPart);
                remainingWidth -= actualWidth;
                lastStripWidth = actualWidth - requiredWidth;
                currentLineBuilder.append(textPart);
            } else {
                if (forceWrapAllowed) {
                    WrapResult forceWrapResult = wrapWords(textPart, remainingWidth, measurer);
                    currentLineBuilder.append(forceWrapResult.currentLine);
                    nextLineBuilder.append(forceWrapResult.nextLine);
                } else {
                    nextLineBuilder.append(textPart);
                }
                wrapped = true;
            }
        }
        String currentLine = currentLineBuilder.toString();
        return new WrapResult(
            stripEnd(currentLine, null),
            nextLineBuilder.toString(),
            availableWidth - remainingWidth - lastStripWidth,
            availableWidth - remainingWidth
        );
    }

    private static WrapResult wrapWords(String textPart,
                                        float availableWidth,
                                        Function<String, Float> measurer) {
        StringBuilder currentLineBuilder = new StringBuilder();
        StringBuilder nextLineBuilder = new StringBuilder();
        float remainingWidth = availableWidth;
        boolean wrapped = false;
        for (char c : textPart.toCharArray()) {
            if (wrapped) {
                nextLineBuilder.append(c);
                continue;
            }
            float requiredWidth = measurer.apply(String.valueOf(c));
            if (requiredWidth <= remainingWidth) {
                currentLineBuilder.append(c);
                float actualWidth = measurer.apply(String.valueOf(c));
                remainingWidth -= actualWidth;
            } else {
                wrapped = true;
            }
        }
        return new WrapResult(
            stripEnd(currentLineBuilder.toString(), null),
            nextLineBuilder.toString(),
            availableWidth - remainingWidth,
            availableWidth - remainingWidth
        );
    }

    @AllArgsConstructor
    static final class WrapResult {
        final String currentLine;
        final String nextLine;
        final float width;
        final float strippedWidth;
    }

    @Data
    @AllArgsConstructor
    private static final class WrapCandidate<T> {
        private final T obj;
        private final boolean forceWrapAllowed;

        public static <T> WrapCandidate<T> forceWrapAllowed(T obj) {
            return new WrapCandidate<>(obj, true);
        }

        public static <T> WrapCandidate<T> forceWrapNotAllowed(T obj) {
            return new WrapCandidate<>(obj, false);
        }
    }

    @AllArgsConstructor
    private static final class StringWidthMeasurer {
        private final PdfFontProperties fontProperties;
        private final float fontSize;

        @SneakyThrows
        public float measureString(String string) {
            return fontProperties.getStringWidth(string).toPixels(fontSize);
        }
    }

    private PdfParagraphDrawerUtils() {
    }
}
