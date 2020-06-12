package io.evlikat.limopdf.paragraph;

import org.junit.Test;

import java.util.List;

import static io.evlikat.limopdf.paragraph.TextUtils.splitByWords;
import static org.assertj.core.api.Assertions.assertThat;

public class TextUtilsTest {

    @Test
    public void testSplitSomeWords() {
        List<String> result = splitByWords("some text inside");

        assertThat(result).containsExactly(
            "some ",
            "text ",
            "inside"
        );
    }

    @Test
    public void testSplitSomeWordsDifferentSpaces() {
        List<String> result = splitByWords("some\ttext\tinside");

        assertThat(result).containsExactly(
            "some\t",
            "text\t",
            "inside"
        );
    }

    @Test
    public void testSplitSomeWordsPreservingSpaces() {
        List<String> result = splitByWords("some   text  inside");

        assertThat(result).containsExactly(
            "some   ",
            "text  ",
            "inside"
        );
    }
}