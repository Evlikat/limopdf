package io.evlikat.limopdf.util;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.nio.charset.Charset;

public class TextGenerator {

    @SneakyThrows
    public static String loremIpsum() {
        return IOUtils.resourceToString("/lorem-ipsum.txt", Charset.defaultCharset());
    }

    private TextGenerator() {
    }

}
