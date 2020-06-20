package io.evlikat.limopdf.util;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class TestIOUtils {
    public static String resourceToString(String resourceName, Charset defaultCharset) throws Exception {
        URI uri = Objects.requireNonNull(TestIOUtils.class.getClassLoader().getResource(resourceName)).toURI();
        return Files.readString(Path.of(uri), defaultCharset);
    }
}
