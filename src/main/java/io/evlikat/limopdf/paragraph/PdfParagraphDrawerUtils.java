package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.draw.TextChunk;
import io.evlikat.limopdf.draw.TextLine;
import io.evlikat.limopdf.util.font.PdfFont;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toCollection;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

class PdfParagraphDrawerUtils {

    static List<TextLine> wrapLines(List<PdfParagraphChunk> paragraphChunks, float availableWidth) {

        float remainingWidth = availableWidth;

        List<TextLine> lines = new ArrayList<>();
        TextLine currentLine = new TextLine();
        Deque<WrapCandidate<PdfParagraphChunk>> chunkQueue = paragraphChunks.stream()
            .map(WrapCandidate::forceWrapNotAllowed)
            .collect(toCollection(ArrayDeque::new));

        while (!chunkQueue.isEmpty()) {
            WrapCandidate<PdfParagraphChunk> item = chunkQueue.pop();
            boolean forceWrapAllowed = item.forceWrapAllowed;
            PdfParagraphChunk chunk = item.obj;

            StringWidthMeasurer measurer = new StringWidthMeasurer(
                chunk.getProperties().getFont(),
                chunk.getProperties().getFontSize()
            );

            String chunkText = chunk.getText();
            // Trailing spaces can be ignored
            float requiredWidth = measurer.measureString(StringUtils.stripEnd(chunkText, null));
            if (requiredWidth <= remainingWidth) {
                // Trailing spaces must be considered
                float chunkWidth = measurer.measureString(chunkText);
                remainingWidth -= chunkWidth;
                currentLine.addChunk(new TextChunk(chunkText, chunk.getProperties(), chunkWidth));
            } else {
                WrapResult wrapResult = wrap(chunk.getText(), remainingWidth, forceWrapAllowed, measurer::measureString);
                if (isNotBlank(wrapResult.currentLine)) {
                    currentLine.addChunk(new TextChunk(wrapResult.currentLine, chunk.getProperties(), wrapResult.width));
                }
                lines.add(currentLine);

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
    private static WrapResult wrap(String text,
                                   float availableWidth,
                                   boolean forceWrapAllowed,
                                   Function<String, Float> measurer) {
        // TODO: add spaces
        List<String> textParts = TextUtils.splitByWords(text);
        StringBuilder currentLineBuilder = new StringBuilder();
        StringBuilder nextLineBuilder = new StringBuilder();
        float remainingWidth = availableWidth;
        boolean wrapped = false;
        for (String textPart : textParts) {
            if (wrapped) {
                nextLineBuilder.append(textPart);
                continue;
            }
            float requiredWidth = measurer.apply(StringUtils.stripEnd(textPart, null));
            if (requiredWidth <= remainingWidth) {
                remainingWidth -= measurer.apply(textPart);
                currentLineBuilder.append(textPart);
            } else {
                if (forceWrapAllowed) {
                    WrapResult forceWrapResult = forceWrap(textPart, remainingWidth, measurer);
                    currentLineBuilder.append(forceWrapResult.currentLine);
                    nextLineBuilder.append(forceWrapResult.nextLine);
                } else {
                    nextLineBuilder.append(textPart);
                }
                wrapped = true;
            }
        }
        String currentLine = currentLineBuilder.toString();
        return new WrapResult(currentLine, nextLineBuilder.toString(), availableWidth - remainingWidth);
    }

    private static WrapResult forceWrap(String textPart,
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
            float width = measurer.apply(String.valueOf(c));
            if (width <= remainingWidth) {
                currentLineBuilder.append(c);
                remainingWidth -= width;
            } else {
                wrapped = true;
            }
        }
        return new WrapResult(currentLineBuilder.toString(), nextLineBuilder.toString(), availableWidth - remainingWidth);
    }

    @AllArgsConstructor
    static final class WrapResult {
        final String currentLine;
        final String nextLine;
        final float width;
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
        private final PdfFont font;
        private final float fontSize;

        @SneakyThrows
        public float measureString(String string) {
            return font.getStringWidth(string).toPixels(fontSize);
        }
    }

    private PdfParagraphDrawerUtils() {
    }
}
