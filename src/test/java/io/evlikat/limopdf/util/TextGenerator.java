package io.evlikat.limopdf.util;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.nio.charset.Charset;

public class TextGenerator {

    public static String loremIpsum() {
        return loremIpsum(1);
    }

    @SneakyThrows
    public static String loremIpsum(int index) {
        return IOUtils.resourceToString("/lorem-ipsum-" + index + ".txt", Charset.defaultCharset());
    }

    @SneakyThrows
    public static String loremIpsum(int fromIndex, int toIndex) {
        StringBuilder sb = new StringBuilder();
        for (int index = fromIndex; index <= toIndex; index++) {
            sb.append(IOUtils.resourceToString("/lorem-ipsum-" + index + ".txt", Charset.defaultCharset()));
        }
        return sb.toString();
    }

    private TextGenerator() {
    }

}
